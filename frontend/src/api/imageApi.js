import request from './request'

export function pageImages(params) {
  return request.get('/images', { params })
}

export function uploadImage(formData) {
  return request.post('/images/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    timeout: 60000
  })
}
