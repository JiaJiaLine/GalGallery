# GalGallery Database Notes

The default database configured in `backend/src/main/resources/application.yml` is:

```text
galgallery
```

`database.sql` is the development database initialization script. Running it will drop and recreate the entire
`galgallery` database, including all tables and data.

Do not run this script directly in production.

The image storage table is named:

```text
gallery_image
```

When backend entities are added, keep the Java entity name as:

```text
GalleryImage
```
