import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  state: () => ({
    backendStatus: ''
  }),
  actions: {
    setBackendStatus(status) {
      this.backendStatus = status
    }
  }
})

