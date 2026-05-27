import axios from 'axios'

const API_BASE_URL = '/api'

const instance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
})

// 请求拦截器
instance.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器
instance.interceptors.response.use(
  response => response.data,
  error => {
    return Promise.reject(error)
  }
)

export default instance
