# Local Dev Setup Notes

## Postgres (dev, via Docker)
Port 5432 and 5433 were occupied by other local projects on this machine,
so edumonitor's Postgres runs on host port **5555**.

```bash
docker run -d --name edumonitor-postgres \
  -e POSTGRES_USER=edumonitor \
  -e POSTGRES_PASSWORD=edumonitor \
  -e POSTGRES_DB=edumonitor \
  -p 5555:5432 \
  -v edumonitor_pgdata_v2:/var/lib/postgresql/data \
  postgres:16
```

Connection string used in `application.yml`:
`jdbc:postgresql://localhost:5555/edumonitor`

## Loading schema
```bash
docker exec -i edumonitor-postgres psql -U edumonitor -d edumonitor < database/schema.sql
docker exec -i edumonitor-postgres psql -U edumonitor -d edumonitor < database/indexes.sql
docker exec -i edumonitor-postgres psql -U edumonitor -d edumonitor < database/seed-data.sql
```

## Known gotcha
If Postgres auth fails with "password authentication failed" despite correct
credentials, check `docker ps` for **other projects'** Postgres containers
that may already occupy the port you're trying to use. Named volumes also
retain their original password permanently — removing the container alone
does NOT reset it; the volume must be removed too (`docker volume rm`).

Note: in Phase 9 (docker-compose), all services will talk over Docker's
internal network using container names (e.g. `edumonitor-postgres:5432`),
so this host-port juggling won't be relevant in the final setup.
