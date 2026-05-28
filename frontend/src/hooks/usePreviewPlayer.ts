import { useCallback, useEffect, useRef, useState } from 'react';
import { useHlsPlayer } from './useHlsPlayer';

const DEBOUNCE_MS = 300;

export function usePreviewPlayer() {
  const videoRef = useRef<HTMLVideoElement>(null);
  const { load, destroy } = useHlsPlayer(videoRef);
  const [focusedId, setFocusedId] = useState<string | null>(null);
  const timerRef = useRef<ReturnType<typeof setTimeout>>();

  const focusTitle = useCallback((id: string, previewUrl: string) => {
    clearTimeout(timerRef.current);
    timerRef.current = setTimeout(() => {
      setFocusedId(id);
      load(previewUrl, true, true);
    }, DEBOUNCE_MS);
  }, [load]);

  const blurTitle = useCallback(() => {
    clearTimeout(timerRef.current);
    setFocusedId(null);
    destroy();
  }, [destroy]);

  useEffect(() => () => clearTimeout(timerRef.current), []);

  return { videoRef, focusedId, focusTitle, blurTitle };
}
