import api from '@/lib/axios';

export const teacherService = {
  create: (data) => api.post('/v1/teacher', data),
  getById: (id) => api.get(`/v1/teacher/${id}`),
  update: (id, data) => api.put(`/v1/teacher/${id}`, data),
  delete: (id) => api.delete(`/v1/teacher/${id}`),
  getPaged: (params) => api.get('/v1/teachers/paged', { params }),
  search: (params) => api.get('/v1/teachers/search', { params }),
  filter: (params) => api.get('/v1/teachers/filter', { params }),
};
