const SKIP_INTRO_KEY = 'streaming-demo:skip-intro';

export function getSkipIntroPreference(): boolean {
  return localStorage.getItem(SKIP_INTRO_KEY) === 'true';
}

export function setSkipIntroPreference(skip: boolean) {
  localStorage.setItem(SKIP_INTRO_KEY, String(skip));
}

export function shouldSkipIntroForTitle(titleId: string, introPolicy: string): boolean {
  if (getSkipIntroPreference()) {
    return true;
  }
  if (introPolicy === 'SKIP') {
    return true;
  }
  if (introPolicy === 'FIRST_TIME') {
    const key = `streaming-demo:watched:${titleId}`;
    return localStorage.getItem(key) === 'true';
  }
  return false;
}

export function markTitleWatched(titleId: string) {
  localStorage.setItem(`streaming-demo:watched:${titleId}`, 'true');
}
