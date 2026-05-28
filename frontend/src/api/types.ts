export type TitleType = 'MOVIE' | 'SERIES';
export type IntroPolicy = 'ALWAYS' | 'FIRST_TIME' | 'SKIP';

export interface TitleSummary {
  id: string;
  type: TitleType;
  name: string;
  description: string;
  runtimeSec: number;
  posterUrl: string;
  previewManifestUrl: string;
  introPolicy: IntroPolicy;
  creditsStartSec: number;
}

export interface BrowseRow {
  id: string;
  label: string;
  titles: TitleSummary[];
}

export interface BrowseResponse {
  hero: TitleSummary;
  rows: BrowseRow[];
}

export interface UpNextCandidate {
  id: string;
  name: string;
  posterUrl: string;
  previewManifestUrl: string;
  reason: string;
  score: number;
}

export interface PlaybackStartResponse {
  sessionId: string;
  titleId: string;
  introUrl: string | null;
  featureManifestUrl: string;
  creditsStartSec: number;
  runtimeSec: number;
  upNext: UpNextCandidate[];
}

export interface ProgressResponse {
  percentComplete: number;
  showUpNext: boolean;
}
