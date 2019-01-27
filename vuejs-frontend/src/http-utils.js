import Vue from 'vue'
import axios from 'axios'
import router from './router'

const axiosConfig = {
  // baseURL: 'http://localhost:8080/api',
  baseURL: process.env.VUE_APP_API_BASE_URL || '/api',
  timeout: 30000
}

export const HTTP = axios.create(axiosConfig)

// Setting up Axios on Vue Instance, for use via this.$axios
Vue.prototype.$http = HTTP

// Default vars set up from localStorage (ie, user has come back)
// Vue.prototype.$http.defaults.headers.common.XAuthorization = `Bearer ${localStorage.getItem('access_token')}`
// Vue.prototype.$http.defaults.headers.common['Access-Token'] = localStorage.getItem('auth_token')

// Set the AUTH token for any request
HTTP.interceptors.request.use(function (config) {
  // console.log('before:', config)
  const accessToken = localStorage.getItem('access_token')
  if (!config.headers.Authorization && accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`
  }
  // console.log('after:', config)
  return config
})

// Add a response interceptor
HTTP.interceptors.response.use(function (response) {
  // console.log('Response', response)
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
