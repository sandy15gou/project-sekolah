import api from '@/lib/axios';

export const scoreService = {
  create: (data) => api.post('/v1/scores', data),
  getAll: () => api.get('/v1/scores'),
  getById: (id) => api.get(`/v1/scores/${id}`),
  update: (id, data) => api.put(`/v1/scores/${id}`, data),
  delete: (id) => api.delete(`/v1/scores/${id}`),
  getByStudent: (studentId) => api.get(`/v1/scores/student/${studentId}`),
  getByStudentSemester: (studentId, semester) => api.get(`/v1/scores/student/${studentId}/semester/${semester}`),
  getPaged: (params) => api.get('/v1/scores/paged', { params }),
  searchByStudent: (params) => api.get('/v1/scores/search/by-student', { params }),
  searchBySubject: (params) => api.get('/v1/scores/search/by-subject', { params }),
  searchBySemester: (params) => api.get('/v1/scores/search/by-semester', { params }),
  searchByStudentAndSemester: (params) => api.get('/v1/scores/search/by-student-and-semester', { params }),
  searchBySubjectAndSemester: (params) => api.get('/v1/scores/search/by-subject-and-semester', { params }),
  filter: (params) => api.get('/v1/scores/filter', { params }),
};
