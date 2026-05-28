#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/../frontend"
export PATH="/opt/homebrew/bin:$PATH"
npm install
npm run dev
