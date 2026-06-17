import axios from 'axios'

// Instancia do Axios para comunicacao centralizada com o backend
const api = axios.create({
  baseURL: 'http://localhost:8081', // URL base do servidor backend de APIs
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json'
  }
})

export default api