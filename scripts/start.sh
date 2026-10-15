#!/usr/bin/env bash
set -euo pipefail

# Always run from the repo root, wherever this script is called from
cd "$(dirname "$0")/.."

# Usage:
#   bash scripts/start.sh            start; build images only if they don't exist yet
#   bash scripts/start.sh --rebuild  rebuild images first (use after changing code)
REBUILD=0
[ "${1:-}" = "--rebuild" ] && REBUILD=1

# 1. Create .env with fresh random secrets ONLY if it doesn't exist yet.
#    (Don't delete .env while the database volume exists: the Postgres
#    password is fixed when the volume is first created.)
if [ ! -f .env ]; then
  echo "Creating .env with fresh random secrets..."
  cat > .env << EOF
POSTGRES_DB=edumonitor
POSTGRES_USER=edumonitor
POSTGRES_PASSWORD=$(openssl rand -hex 16)
JWT_SECRET=$(openssl rand -hex 48)
WEB_PORT=8081
CORS_ALLOWED_ORIGINS=http://localhost:8081
EOF
fi

images_present() {
  docker image inspect edumonitor-api-service edumonitor-web >/dev/null 2>&1
}

wait_for_dns() {
  # Right after Docker Desktop starts, WSL's DNS can take a moment to come up
  for _ in $(seq 1 15); do
    getent hosts registry-1.docker.io >/dev/null 2>&1 && return 0
    sleep 2
  done
  return 1
}

# 2. Build ONLY when needed. A build asks Docker Hub about the base images even
#    when they are cached locally, so a DNS hiccup would otherwise block a start
#    that needs no network at all.
if [ "$REBUILD" -eq 1 ] || ! images_present; then
  echo "Building images (needs internet access to Docker Hub)..."
  if ! wait_for_dns; then
    echo ""
    echo "ERROR: cannot resolve registry-1.docker.io - DNS is not working inside WSL."
    echo "  1. In Windows PowerShell run:  wsl --shutdown   then reopen Docker Desktop and retry"
    echo "  2. If it still fails, try another network (e.g. a phone hotspot)"
    echo "  3. If images were built before, you can skip building:  docker compose up -d"
    exit 1
  fi
  for attempt in 1 2 3; do
    docker compose build && break
    if [ "$attempt" -eq 3 ]; then
      echo "ERROR: build failed after 3 attempts."
      exit 1
    fi
    echo "Build failed (attempt ${attempt}/3) - retrying in 10 seconds..."
    sleep 10
  done
else
  echo "Images already built - skipping the build."
  echo "(After changing code, run: bash scripts/start.sh --rebuild)"
fi

# 3. Start everything (uses the images above; no registry access needed)
docker compose up -d

# 4. Wait until the API answers through Nginx
PORT=$(grep -E '^WEB_PORT=' .env | cut -d= -f2 || true)
PORT=${PORT:-8081}

echo "Waiting for the app to respond on port ${PORT}..."
for _ in $(seq 1 60); do
  code=$(curl -s -o /dev/null -w "%{http_code}" -X POST "http://localhost:${PORT}/api/auth/login" \
    -H "Content-Type: application/json" -d '{}' || true)
  case "$code" in
    200|400|401|403)
      echo "App is up (API answered with HTTP ${code})."
      exit 0
      ;;
  esac
  sleep 2
done

echo "ERROR: the app did not become ready in time. Recent logs:"
docker compose logs --tail 40
exit 1
