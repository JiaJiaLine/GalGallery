import request from './request'

export function pageGames(params) {
  return request.get('/games', { params })
}

export function getGame(id) {
  return request.get(`/games/${id}`)
}

export function createGame(data) {
  return request.post('/games', data)
}
