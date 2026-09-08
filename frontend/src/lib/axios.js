// ==========================================
// AXIOS INSTANCE WITH INTERCEPTORS
// ==========================================
import axios from 'axios';
import { getToken, clearToken } from './auth';

const getBaseUrl = () => {
  if (typeof window !== 'undefined' && window.location.hostname !== 'localhost' && window.location.hostname !== '127.0.0.1') {
    return '';
  }
  return process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8090';
};

const api = axios.create({
  baseURL: getBaseUrl(),
  headers: {
    'Content-Type': 'application/json',
  },
});

// --- Request Interceptor: Attach token ---
api.interceptors.request.use(
  (config) => {
    if (typeof window !== 'undefined' && window.location.hostname !== 'localhost' && window.location.hostname !== '127.0.0.1') {
      if (config.baseURL && config.baseURL.includes('localhost')) {
        config.baseURL = '';
      }
    }
    const token = getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// --- Response Interceptor: Handle errors ---
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (!error.response) {
      // Network error
      const networkError = new Error('Koneksi ke server gagal. Periksa koneksi internet Anda.');
      networkError.errorCode = 3;
      networkError.details = ['Tidak dapat terhubung ke server'];
      return Promise.reject(networkError);
    }

    const { status, data } = error.response;

    if (status === 401) {
      clearToken();
      if (typeof window !== 'undefined') {
        window.location.href = '/login';
      }
      const authError = new Error('Sesi Anda telah berakhir. Silakan login kembali.');
      authError.errorCode = 401;
      authError.details = ['Unauthorized - Token expired atau tidak valid'];
      return Promise.reject(authError);
    }

    if (status === 400) {
      const badRequestError = new Error(data.message || 'Data tidak valid');
      badRequestError.errorCode = data.error_code || 1;
      badRequestError.details = data.details || ['Terjadi kesalahan validasi data'];
      badRequestError.status = data.status;
      return Promise.reject(badRequestError);
    }

    if (status === 404) {
      const notFoundError = new Error('Data tidak ditemukan');
      notFoundError.errorCode = 5;
      notFoundError.details = data.details || ['Data yang dicari tidak ditemukan'];
      return Promise.reject(notFoundError);
    }

    // 500 or other errors
    const serverError = new Error('Terjadi kesalahan pada server');
    serverError.errorCode = 2;
    serverError.details = ['Internal server error. Silakan coba lagi nanti.'];
    return Promise.reject(serverError);
  }
);

export default api;
