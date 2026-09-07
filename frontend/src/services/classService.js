import api from '@/lib/axios';

export const classService = {
  create: (data) => api.post('/v1/classes', data),
  getAll: () => api.get('/v1/classes'),
  getById: (id) => api.get(`/v1/classes/${id}`),
  update: (id, data) => api.put(`/v1/classes/${id}`, data),
  delete: (id) => api.delete(`/v1/classes/${id}`),
  addStudent: (classId, studentId) => api.post(`/v1/classes/${classId}/students/${studentId}`),
  removeStudent: (classId, studentId) => api.delete(`/v1/classes/${classId}/students/${studentId}`),
  getPaged: (params) => api.get('/v1/classes/paged', { params }),
  searchByName: (params) => api.get('/v1/classes/search/by-name', { params }),
  searchByAcademicYear: (params) => api.get('/v1/classes/search/by-academic-year', { params }),
  searchByGradeLevel: (params) => api.get('/v1/classes/search/by-grade-level', { params }),
  filter: (params) => api.get('/v1/classes/filter', { params }),
};
