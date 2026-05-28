#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
MEDIA="$ROOT/media"
INTRO_DIR="$MEDIA/intro"
mkdir -p "$INTRO_DIR"

echo "==> Downloading shared intro clip"
curl -fsSL -A "Mozilla/5.0" -o "$INTRO_DIR/intro.mp4" \
  "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4"

TITLES=(big-buck-bunny sintel elephants-dream tears-of-steel)
POSTER_URLS=(
  "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Big_buck_bunny_poster_big.jpg/440px-Big_buck_bunny_poster_big.jpg"
  "https://upload.wikimedia.org/wikipedia/commons/thumb/3/31/Sintel_poster.jpg/440px-Sintel_poster.jpg"
  "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/Elephants_Dream_s5_both.jpg/440px-Elephants_Dream_s5_both.jpg"
  "https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Tears_of_Steel_poster.jpg/440px-Tears_of_Steel_poster.jpg"
)

for i in "${!TITLES[@]}"; do
  ID="${TITLES[$i]}"
  DIR="$MEDIA/titles/$ID"
  mkdir -p "$DIR/preview" "$DIR/feature"
  echo "==> Poster for $ID"
  curl -fsSL -o "$DIR/poster.jpg" "https://picsum.photos/seed/${ID}/440/660" || \
    curl -fsSL -o "$DIR/poster.jpg" "${POSTER_URLS[$i]}"
done

# Local HLS master playlists (point to Apple public samples for demo ABR)
FEATURE_MASTER='https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8'
PREVIEW_MASTER='https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8'

for ID in "${TITLES[@]}"; do
  DIR="$MEDIA/titles/$ID"
  cat > "$DIR/feature/master.m3u8" <<EOF
#EXTM3U
#EXT-X-STREAM-INF:BANDWIDTH=1280000,RESOLUTION=1280x720
$FEATURE_MASTER
EOF
  cat > "$DIR/preview/preview.m3u8" <<EOF
#EXTM3U
#EXT-X-STREAM-INF:BANDWIDTH=640000,RESOLUTION=640x480
$PREVIEW_MASTER
EOF
done

echo "==> Done. Start nginx: cd infra && docker compose up -d"
