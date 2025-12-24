#!/usr/bin/env bash
set -euo pipefail
# Usage:
#  ./scripts/pg_dump.sh [service] [db] [user] [format] [output]
# Defaults: service=postgres, db=yourdb, user=postgres, format=c (custom), output=backup.dump
# Examples:
#  ./scripts/pg_dump.sh postgres mydb myuser c mydb.dump
#  ./scripts/pg_dump.sh postgres mydb myuser p mydb.sql

SERVICE=${1:-postgres}
DB=${2:-yourdb}
USER=${3:-postgres}
FORMAT=${4:-c}
OUT=${5:-backup.dump}

# Try docker-compose exec with no tty. This redirects the remote pg_dump output to a local file.
# If your docker-compose service name differs, pass it as first arg (e.g. "db" or "postgres").

echo "Running pg_dump from service '$SERVICE' for database '$DB' as user '$USER' (format: $FORMAT) ..."

docker-compose exec -T "$SERVICE" pg_dump -U "$USER" -F "$FORMAT" "$DB" > "$OUT"

if [ $? -eq 0 ]; then
  echo "Dump saved to $OUT"
else
  echo "pg_dump failed"
  exit 1
fi

