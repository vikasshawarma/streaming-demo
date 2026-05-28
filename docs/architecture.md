# Architecture

## Layers

1. **Web client** — React + hls.js. Handles browse previews, intro→feature playback FSM, and Up Next overlay.
2. **API** — Spring Boot. Catalog, signed media URLs, playback sessions, progress, rule-based recommendations.
3. **Media** — Static HLS/MP4 served via nginx (CDN simulation).

## Playback FSM

```
Idle → Preview (browse focus)
Watch: LoadingIntro → PlayingIntro → LoadingFeature → PlayingFeature
PlayingFeature → UpNextVisible (credits / 90% threshold)
UpNextVisible → next title or dismiss
```

## Signed URLs

API returns URLs with `exp` and `sig` query parameters. Public Apple sample streams are passed through unsigned for local dev without nginx.

## Recommendations (v1)

Rule-based scoring: genre overlap, shared collection, popularity. Returns human-readable `reason` for UI.

## Credits / Up Next

Each title has `creditsStartSec`. Client also shows Up Next when `position >= 90%` of duration or server `showUpNext` from progress API.
