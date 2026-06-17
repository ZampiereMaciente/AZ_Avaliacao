import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8081', // Altere para a porta real do seu backend se for diferente
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json'
  }
})

export default api