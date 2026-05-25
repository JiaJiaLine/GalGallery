import request from './request'

export function pageTags(params) {
  return request.get('/tags', { params })
}

export function listAllTags() {
  return request.get('/tags/all')
}

export function createTag(data) {
  return request.post('/tags', data)
}
