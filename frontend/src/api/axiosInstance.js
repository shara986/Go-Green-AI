import axios from 'axios';

// Create an axios instance with the base URL of the backend API.
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// ─── Request interceptor: attach Bearer token ────────────────────────────────
api.interceptors.request.use((config) => {
  let token = localStorage.getItem('accessToken');
  if (!token) {
    try {
      const authObj = JSON.parse(localStorage.getItem('auth') || '{}');
      token = authObj.accessToken;
    } catch (_) { /* ignore */ }
  }
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`;
  }
  return config;
});

// ─── Response interceptor: silent token refresh on 401 ───────────────────────
let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });
  failedQueue = [];
};

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // Only attempt refresh for 401 errors that haven't been retried yet
    // and are NOT the login / refresh-token endpoints themselves
    const isAuthEndpoint =
      originalRequest.url?.includes('/auth/login') ||
      originalRequest.url?.includes('/auth/refresh-token') ||
      originalRequest.url?.includes('/auth/register');

    if (error.response?.status === 401 && !originalRequest._retry && !isAuthEndpoint) {
      if (isRefreshing) {
        // Queue the request until refresh completes
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        })
          .then((token) => {
            originalRequest.headers['Authorization'] = `Bearer ${token}`;
            return api(originalRequest);
          })
          .catch((err) => Promise.reject(err));
      }

      originalRequest._retry = true;
      isRefreshing = true;

      const refreshToken = localStorage.getItem('refreshToken');

      if (!refreshToken) {
        // No refresh token stored → force logout
        clearAuthAndRedirect();
        return Promise.reject(error);
      }

      try {
        const { data } = await axios.post(
          `${import.meta.env.VITE_API_URL || '/api'}/auth/refresh-token`,
          { refreshToken }
        );

        // The backend wraps in ApiResponse: { success, message, data: { accessToken, refreshToken, … } }
        const authData = data?.data || data;
        const newAccessToken = authData?.accessToken;
        const newRefreshToken = authData?.refreshToken;

        if (!newAccessToken) throw new Error('No access token in refresh response');

        // Persist updated tokens
        localStorage.setItem('accessToken', newAccessToken);
        if (newRefreshToken) localStorage.setItem('refreshToken', newRefreshToken);

        // Update stored auth object
        try {
          const storedAuth = JSON.parse(localStorage.getItem('auth') || '{}');
          storedAuth.accessToken = newAccessToken;
          if (newRefreshToken) storedAuth.refreshToken = newRefreshToken;
          localStorage.setItem('auth', JSON.stringify(storedAuth));
        } catch (_) { /* ignore */ }

        processQueue(null, newAccessToken);
        originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
        return api(originalRequest);
      } catch (refreshError) {
        processQueue(refreshError, null);
        clearAuthAndRedirect();
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  }
);

function clearAuthAndRedirect() {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
  localStorage.removeItem('user');
  localStorage.removeItem('auth');
  // Redirect to login without using React Router (works outside component tree)
  if (window.location.pathname !== '/login') {
    window.location.href = '/login?session=expired';
  }
}

export default api;
