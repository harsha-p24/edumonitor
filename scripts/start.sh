#!/usr/bin/env bash
set -euo pipefail

# Always run from the repo root, wherever this script is called from
cd "$(dirname "$0")/.."

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

# 2. Build (cached after the first run) and start everything
docker compose up -d --build

# 3. Wait until the API answers through Nginx
PORT=$(grep -E '^WEB_PORT=' .env | cut -d= -f2 || true)
PORT=${PORT:-8081}

echo "Waiting for the app to respond on port ${PORT}..."
for i in $(seq 1 60); do
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
