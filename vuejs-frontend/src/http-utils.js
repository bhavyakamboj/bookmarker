import axios from 'axios'
import router from './router'

const axiosConfig = {
  // baseURL: 'http://localhost:8080/api',
  baseURL: process.env.VUE_APP_API_BASE_URL || '/api',
  timeout: 30000
}

export const HTTP = axios.create(axiosConfig)

// Set the AUTH token for any request
HTTP.interceptors.request.use(function (config) {
  const accessToken = localStorage.getItem('access_token')
  if (!config.headers.Authorization && accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`
  }
  return config
})

// Add a response interceptor
HTTP.interceptors.response.use(function (response) {
  return response
}, function (error) {
  console.log(error)
  if (error.response.status === 401) {
    window.eventBus.$emit('logout')
    router.push('/login')
  } else {
    return Promise.reject(error)
  }
})
