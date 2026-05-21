# GalGallery

GalGallery is a personal GalGame image gallery project with a separated Spring Boot backend and Vue 3 frontend.

## Ports

- Backend: `http://localhost:8090`
- Frontend: `http://localhost:5173`

## Uploads

Uploaded files are stored under:

```text
backend/uploads/
```

The backend exposes them through:

```text
http://localhost:8090/uploads/**
```

## Development

Start the backend:

```bash
cd backend
mvn spring-boot:run
```

Optional database environment variables:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Start the frontend:

```bash
cd frontend
npm install
npm run dev
```
