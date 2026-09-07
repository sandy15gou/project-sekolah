import api from '@/lib/axios';

export const subjectService = {
  create: (data) => api.post('/v1/subjects', data),
  getAll: () => api.get('/v1/subjects'),
  getById: (id) => api.get(`/v1/subjects/${id}`),
  update: (id, data) => api.put(`/v1/subjects/${id}`, data),
  delete: (id) => api.delete(`/v1/subjects/${id}`),
  addTeacher: (subjectId, teacherId) => api.post(`/v1/subjects/${subjectId}/teachers/${teacherId}`),
  removeTeacher: (subjectId, teacherId) => api.delete(`/v1/subjects/${subjectId}/teachers/${teacherId}`),
  filter: (params) => api.get('/v1/subjects/filter', { params }),
};
