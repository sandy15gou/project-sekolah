import api from '@/lib/axios';

export const authService = {
  login: (username, password) => api.post('/v1/login', { username, password }),
};
