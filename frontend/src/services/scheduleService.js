import api from '@/lib/axios';

export const scheduleService = {
  create: (data) => api.post('/v1/schedules', data),
  getAll: () => api.get('/v1/schedules'),
  getById: (id) => api.get(`/v1/schedules/${id}`),
  update: (id, data) => api.put(`/v1/schedules/${id}`, data),
  delete: (id) => api.delete(`/v1/schedules/${id}`),
  getPaged: (params) => api.get('/v1/schedules/paged', { params }),
  searchByDay: (params) => api.get('/v1/schedules/search/by-day', { params }),
  searchBySemester: (params) => api.get('/v1/schedules/search/by-semester', { params }),
  searchByClass: (params) => api.get('/v1/schedules/search/by-class', { params }),
  searchBySubject: (params) => api.get('/v1/schedules/search/by-subject', { params }),
  searchByTeacher: (params) => api.get('/v1/schedules/search/by-teacher', { params }),
  searchByDayAndSemester: (params) => api.get('/v1/schedules/search/by-day-and-semester', { params }),
  filter: (params) => api.get('/v1/schedules/filter', { params }),
};
