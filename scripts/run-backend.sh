#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/../backend"
export JAVA_HOME="${JAVA_HOME:-$(/usr/libexec/java_home 2>/dev/null || true)}"
./mvnw spring-boot:run -Dspring-boot.run.profiles=local-h2 "$@"
