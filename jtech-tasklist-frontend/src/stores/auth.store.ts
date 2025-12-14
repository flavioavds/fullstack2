import { defineStore } from 'pinia'
import http from '@/api/http'

interface User {
  id: string
  name: string
  email: string
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') as string | null,
    user: null as User | null,
  }),

  actions: {
    async login(email: string, password: string) {
      const { data } = await http.post('/auth/login', { email, password })

      const token = data.access_token as string

      this.token = token
      localStorage.setItem('token', token)

      await this.fetchMe()
    },

    async fetchMe() {
      const { data } = await http.get('/user/me')
      this.user = data
    },

    logout() {
      this.token = null
      this.user = null
      localStorage.removeItem('token')
    },
    clear() {
      this.user = null
      this.token = ''
    },
  },
})
