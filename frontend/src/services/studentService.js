import api from '@/lib/axios';

export const studentService = {
  create: (data) => api.post('/v1/student', data),
  getById: (id) => api.get(`/v1/student/${id}`),
  update: (id, data) => api.put(`/v1/student/${id}`, data),
  delete: (id) => api.delete(`/v1/student/${id}`),
  getPaged: (params) => api.get('/v1/students/paged', { params }),
  search: (params) => api.get('/v1/students/search', { params }),
  filter: (params) => api.get('/v1/students/filter', { params }),
};
