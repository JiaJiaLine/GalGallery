# GalGallery Bruno Collection

## Open Collection

1. Open Bruno.
2. Choose `Open Collection`.
3. Select the `docs/bruno` directory.

The collection name is `GalGallery API`.

## Environment

Select the `local` environment before sending requests.

`baseUrl` points to the local backend server:

```text
http://localhost:8090
```

Requests use `{{baseUrl}}` so the backend address can be changed in one place.

## Suggested Test Order

1. `GET /api/health`
2. 新增游戏
3. 新增标签
4. 上传图片
5. 查询图片列表
