import axios from 'axios'
import router from '@/router'

const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
})

http.interceptors.request.use(async (config) => {
  const { useAuthStore } = await import('@/stores/auth.store')
  const authStore = useAuthStore()

  if (authStore.token) {
    config.headers.Authorization = `Bearer ${authStore.token}`
  }

  return config
})

http.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      const { useAuthStore } = await import('@/stores/auth.store')
      const authStore = useAuthStore()

      authStore.logout()
      router.push('/login')
    }

    return Promise.reject(error)
  }
)

export default http
