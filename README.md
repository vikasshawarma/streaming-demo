# Streaming Demo

A Netflix-style streaming demo: browse previews on focus, branded intro before playback, and **Up Next** recommendations at end of title.

## Architecture

See [docs/architecture.md](docs/architecture.md) for flows, APIs, and media layout.

## Stack

| Layer | Tech |
|-------|------|
| API | Java 21, Spring Boot 3, PostgreSQL |
| Web | React 18, TypeScript, Vite, hls.js |
| Media | HLS (pre-encoded), nginx CDN simulation |

## Quick start

### 1. Infrastructure

```bash
cd infra
docker compose up -d
```

### 2. Media assets

```bash
./media/scripts/setup-media.sh
```

This downloads sample HLS manifests and intro clip. Without Docker/ffmpeg, the API falls back to public Apple HLS sample streams.

### 3. Backend

```bash
cd backend
./mvnw spring-boot:run
```

API: http://localhost:8080

### 4. Frontend

```bash
cd frontend
npm install
npm run dev
```

UI: http://localhost:5173

## Demo user

- User ID header: `X-User-Id: demo-user` (default in frontend)

## Key endpoints

- `GET /api/v1/browse/rows` — catalog rows + hero
- `GET /api/v1/titles/{id}` — title detail with signed URLs
- `POST /api/v1/playback/start` — start session (intro + feature)
- `PUT /api/v1/progress` — watch progress heartbeat
- `GET /api/v1/titles/{id}/up-next` — recommendations
