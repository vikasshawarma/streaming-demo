import type {
  BrowseResponse,
  PlaybackStartResponse,
  ProgressResponse,
  TitleSummary,
  UpNextCandidate,
} from './types';

const USER_ID = 'demo-user';

async function api<T>(path: string, init?: RequestInit): Promise<T> {
  const res = await fetch(path, {
    ...init,
    headers: {
      'Content-Type': 'application/json',
      'X-User-Id': USER_ID,
      ...init?.headers,
    },
  });
  if (!res.ok) {
    throw new Error(`API ${res.status}: ${await res.text()}`);
  }
  return res.json() as Promise<T>;
}

export function fetchBrowse() {
  return api<BrowseResponse>('/api/v1/browse/rows');
}

export function fetchTitle(id: string) {
  return api<TitleSummary & { featureManifestUrl: string; introUrl: string }>(
    `/api/v1/titles/${id}`,
  );
}

export function startPlayback(titleId: string, skipIntro: boolean) {
  return api<PlaybackStartResponse>('/api/v1/playback/start', {
    method: 'POST',
    body: JSON.stringify({ titleId, skipIntro, device: 'web' }),
  });
}

export function sendProgress(body: {
  sessionId: string;
  titleId: string;
  positionSec: number;
  durationSec: number;
  completed: boolean;
}) {
  return api<ProgressResponse>('/api/v1/progress', {
    method: 'PUT',
    body: JSON.stringify(body),
  });
}

export function fetchUpNext(titleId: string) {
  return api<UpNextCandidate[]>(`/api/v1/titles/${titleId}/up-next?limit=5`);
}
