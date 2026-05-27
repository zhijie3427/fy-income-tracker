import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '../utils/request'

export const useUserStore = defineStore('user', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('token'))

  const login = async (username, password) => {
    try {
      const response = await request.post('/auth/login', {
        username,
        password
      })
      token.value = response.token
      user.value = response
      localStorage.setItem('token', response.token)
      localStorage.setItem('user', JSON.stringify(response))
      return response
    } catch (error) {
      console.error('登录失败:', error)
      throw error
    }
  }

  const register = async (username, password, email, phone) => {
    try {
      const response = await request.post('/auth/register', {
        username,
        password,
        email,
        phone
      })
      return response
    } catch (error) {
      console.error('注册失败:', error)
      throw error
    }
  }

  const logout = () => {
    user.value = null
    token.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  return {
    user,
    token,
    login,
    register,
    logout
  }
})
