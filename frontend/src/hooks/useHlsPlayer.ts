import Hls from 'hls.js';
import { useCallback, useEffect, useRef } from 'react';

export function useHlsPlayer(videoRef: React.RefObject<HTMLVideoElement | null>) {
  const hlsRef = useRef<Hls | null>(null);

  const destroy = useCallback(() => {
    if (hlsRef.current) {
      hlsRef.current.destroy();
      hlsRef.current = null;
    }
    const video = videoRef.current;
    if (video) {
      video.pause();
      video.removeAttribute('src');
      video.load();
    }
  }, [videoRef]);

  const load = useCallback(
    (url: string, autoplay = false, muted = false) => {
      const video = videoRef.current;
      if (!video || !url) {
        return;
      }

      destroy();
      video.muted = muted;
      video.playsInline = true;

      const isHls = url.includes('.m3u8');

      if (isHls && Hls.isSupported()) {
        const hls = new Hls({ enableWorker: true });
        hlsRef.current = hls;
        hls.loadSource(url);
        hls.attachMedia(video);
        hls.on(Hls.Events.MANIFEST_PARSED, () => {
          if (autoplay) {
            void video.play().catch(() => undefined);
          }
        });
      } else if (isHls && video.canPlayType('application/vnd.apple.mpegurl')) {
        video.src = url;
        video.addEventListener(
          'loadedmetadata',
          () => {
            if (autoplay) {
              void video.play().catch(() => undefined);
            }
          },
          { once: true },
        );
      } else {
        video.src = url;
        if (autoplay) {
          void video.play().catch(() => undefined);
        }
      }
    },
    [destroy, videoRef],
  );

  useEffect(() => () => destroy(), [destroy]);

  return { load, destroy };
}
