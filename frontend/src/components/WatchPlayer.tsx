import { useCallback, useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { sendProgress, startPlayback } from '../api/client';
import type { PlaybackStartResponse, UpNextCandidate } from '../api/types';
import { useHlsPlayer } from '../hooks/useHlsPlayer';
import { markTitleWatched, setSkipIntroPreference } from '../lib/preferences';
import UpNextOverlay from './UpNextOverlay';

export type PlayerPhase =
  | 'idle'
  | 'loadingIntro'
  | 'playingIntro'
  | 'loadingFeature'
  | 'playingFeature'
  | 'upNext'
  | 'error';

const HEARTBEAT_MS = 15_000;
const UP_NEXT_COUNTDOWN = 15;

interface Props {
  titleId: string;
  titleName: string;
  skipIntro: boolean;
}

export default function WatchPlayer({ titleId, titleName, skipIntro }: Props) {
  const navigate = useNavigate();
  const videoRef = useRef<HTMLVideoElement>(null);
  const { load, destroy } = useHlsPlayer(videoRef);
  const [phase, setPhase] = useState<PlayerPhase>('idle');
  const [session, setSession] = useState<PlaybackStartResponse | null>(null);
  const [upNextList, setUpNextList] = useState<UpNextCandidate[]>([]);
  const [countdown, setCountdown] = useState(UP_NEXT_COUNTDOWN);
  const [error, setError] = useState<string | null>(null);
  const heartbeatRef = useRef<ReturnType<typeof setInterval>>();
  const countdownRef = useRef<ReturnType<typeof setInterval>>();

  const reportProgress = useCallback(
    async (position: number, duration: number, completed = false) => {
      if (!session) return;
      try {
        const res = await sendProgress({
          sessionId: session.sessionId,
          titleId,
          positionSec: position,
          durationSec: duration,
          completed,
        });
        if (res.showUpNext && phase === 'playingFeature') {
          setPhase('upNext');
        }
      } catch {
        /* ignore heartbeat errors in demo */
      }
    },
    [session, titleId, phase],
  );

  const beginFeature = useCallback(
    (playback: PlaybackStartResponse) => {
      setPhase('loadingFeature');
      load(playback.featureManifestUrl, true, false);
      setPhase('playingFeature');
    },
    [load],
  );

  const initPlayback = useCallback(async () => {
    try {
      setError(null);
      const playback = await startPlayback(titleId, skipIntro);
      setSession(playback);
      setUpNextList(playback.upNext);

      if (playback.introUrl) {
        setPhase('loadingIntro');
        const video = videoRef.current;
        if (!video) return;
        destroy();
        video.src = playback.introUrl;
        video.muted = false;
        const onIntroEnd = () => {
          video.removeEventListener('ended', onIntroEnd);
          beginFeature(playback);
        };
        video.addEventListener('ended', onIntroEnd);
        setPhase('playingIntro');
        await video.play();
      } else {
        beginFeature(playback);
      }
    } catch (e) {
      setPhase('error');
      setError(e instanceof Error ? e.message : 'Playback failed');
    }
  }, [titleId, skipIntro, destroy, beginFeature]);

  useEffect(() => {
    void initPlayback();
    return () => {
      destroy();
      clearInterval(heartbeatRef.current);
      clearInterval(countdownRef.current);
    };
  }, [titleId]); // eslint-disable-line react-hooks/exhaustive-deps

  useEffect(() => {
    const video = videoRef.current;
    if (!video || phase !== 'playingFeature' || !session) return;

    const onTimeUpdate = () => {
      const remaining = video.duration - video.currentTime;
      const atCredits = video.currentTime >= session.creditsStartSec;
      const nearEnd = remaining <= 30 || video.currentTime / video.duration >= 0.9;
      if (atCredits || nearEnd) {
        setPhase('upNext');
      }
    };

    video.addEventListener('timeupdate', onTimeUpdate);
    return () => video.removeEventListener('timeupdate', onTimeUpdate);
  }, [phase, session]);

  useEffect(() => {
    const video = videoRef.current;
    if (!video || !session || phase !== 'playingFeature') return;

    heartbeatRef.current = setInterval(() => {
      if (video.duration && !video.paused) {
        void reportProgress(video.currentTime, video.duration);
      }
    }, HEARTBEAT_MS);

    return () => clearInterval(heartbeatRef.current);
  }, [phase, session, reportProgress]);

  useEffect(() => {
    if (phase !== 'upNext' || upNextList.length === 0) return;

    setCountdown(UP_NEXT_COUNTDOWN);
    countdownRef.current = setInterval(() => {
      setCountdown((c) => {
        if (c <= 1) {
          clearInterval(countdownRef.current);
          const next = upNextList[0];
          navigate(`/watch/${next.id}`, { replace: true });
          return 0;
        }
        return c - 1;
      });
    }, 1000);

    const cancel = () => {
      clearInterval(countdownRef.current);
      setPhase('playingFeature');
    };
    window.addEventListener('keydown', cancel);
    window.addEventListener('mousemove', cancel, { once: true });

    return () => {
      clearInterval(countdownRef.current);
      window.removeEventListener('keydown', cancel);
      window.removeEventListener('mousemove', cancel);
    };
  }, [phase, upNextList, navigate]);

  const handleEnded = () => {
    const video = videoRef.current;
    if (video?.duration) {
      void reportProgress(video.duration, video.duration, true);
      markTitleWatched(titleId);
    }
    if (upNextList.length > 0) {
      setPhase('upNext');
    }
  };

  const handleSkipIntroChange = (checked: boolean) => {
    setSkipIntroPreference(checked);
  };

  if (error) {
    return <div className="watch-error">{error}</div>;
  }

  return (
    <div className="watch-container">
      <video
        ref={videoRef}
        className="watch-video"
        controls={phase === 'playingFeature' || phase === 'upNext'}
        onEnded={handleEnded}
        playsInline
      />
      <div className="watch-meta">
        <h1>{titleName}</h1>
        <p className="phase-indicator">Phase: {phase}</p>
        <label className="skip-intro-label">
          <input
            type="checkbox"
            defaultChecked={skipIntro}
            onChange={(e) => handleSkipIntroChange(e.target.checked)}
          />
          Skip intro next time
        </label>
      </div>
      {phase === 'upNext' && upNextList[0] && (
        <UpNextOverlay
          candidate={upNextList[0]}
          countdown={countdown}
          onPlayNow={() => navigate(`/watch/${upNextList[0].id}`, { replace: true })}
          onCancel={() => setPhase('playingFeature')}
        />
      )}
    </div>
  );
}
