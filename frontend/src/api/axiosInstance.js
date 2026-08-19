import axios from 'axios';

// Create an axios instance with the base URL of the backend API.
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add the Authorization header if a token is stored.
api.interceptors.request.use((config) => {
  let token = localStorage.getItem('accessToken');
  if (!token) {
    try {
      const authObj = JSON.parse(localStorage.getItem('auth') || '{}');
      token = authObj.accessToken;
    } catch (e) {
      // Ignore JSON parse errors
    }
  }
  
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`;
  }
  return config;
});

export default api;

