// API Configuration
const API_BASE_URL = 'http://localhost:8080/api';

// API Utility Class
class ApiService {
    constructor() {
        this.baseURL = API_BASE_URL;
    }

    async request(endpoint, options = {}) {
        const url = `${this.baseURL}${endpoint}`;
        const config = {
            headers: {
                'Content-Type': 'application/json',
                ...authManager.getAuthHeader(),
                ...options.headers
            },
            ...options
        };

        try {
            const response = await fetch(url, config);

            if (response.status === 401) {
                authManager.logout();
                throw new Error('Session expired');
            }

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.message || `HTTP ${response.status}`);
            }

            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                return await response.json();
            }

            return await response.text();
        } catch (error) {
            console.error(`API request failed: ${endpoint}`, error);
            throw error;
        }
    }

    // Classes API
    async getClasses() {
        return await this.request('/classes');
    }

    async getClassDetail(classId) {
        return await this.request(`/classes/${classId}`);
    }

    async createClass(classData) {
        return await this.request('/classes', {
            method: 'POST',
            body: JSON.stringify([classData])
        });
    }

    async updateClass(classId, classData) {
        return await this.request(`/classes/${classId}`, {
            method: 'PUT',
            body: JSON.stringify(classData)
        });
    }

    async deleteClass(classId) {
        return await this.request(`/classes/${classId}`, {
            method: 'DELETE'
        });
    }

    async addStudentToClass(classId, studentId) {
        return await this.request(`/classes/${classId}/students/${studentId}`, {
            method: 'POST'
        });
    }

    async removeStudentFromClass(classId, studentId) {
        return await this.request(`/classes/${classId}/students/${studentId}`, {
            method: 'DELETE'
        });
    }

    // Students API
    async getStudents() {
        return await this.request('/students');
    }

    async getStudentDetail(studentId) {
        return await this.request(`/students/${studentId}`);
    }

    async createStudent(studentData) {
        return await this.request('/students', {
            method: 'POST',
            body: JSON.stringify([studentData])
        });
    }

    async updateStudent(studentId, studentData) {
        return await this.request(`/students/${studentId}`, {
            method: 'PUT',
            body: JSON.stringify(studentData)
        });
    }

    async deleteStudent(studentId) {
        return await this.request(`/students/${studentId}`, {
            method: 'DELETE'
        });
    }

    // Teachers API
    async getTeachers() {
        return await this.request('/teachers');
    }

    async getTeacherDetail(teacherId) {
        return await this.request(`/teachers/${teacherId}`);
    }

    async createTeacher(teacherData) {
        return await this.request('/teachers', {
            method: 'POST',
            body: JSON.stringify([teacherData])
        });
    }

    async updateTeacher(teacherId, teacherData) {
        return await this.request(`/teachers/${teacherId}`, {
            method: 'PUT',
            body: JSON.stringify(teacherData)
        });
    }

    async deleteTeacher(teacherId) {
        return await this.request(`/teachers/${teacherId}`, {
            method: 'DELETE'
        });
    }

    // Subjects API
    async getSubjects() {
        return await this.request('/subjects');
    }

    async getSubjectDetail(subjectId) {
        return await this.request(`/subjects/${subjectId}`);
    }

    async createSubject(subjectData) {
        return await this.request('/subjects', {
            method: 'POST',
            body: JSON.stringify([subjectData])
        });
    }

    async updateSubject(subjectId, subjectData) {
        return await this.request(`/subjects/${subjectId}`, {
            method: 'PUT',
            body: JSON.stringify(subjectData)
        });
    }

    async deleteSubject(subjectId) {
        return await this.request(`/subjects/${subjectId}`, {
            method: 'DELETE'
        });
    }

    // Schedules API
    async getSchedules() {
        return await this.request('/schedules');
    }

    async getScheduleDetail(scheduleId) {
        return await this.request(`/schedules/${scheduleId}`);
    }

    async createSchedule(scheduleData) {
        return await this.request('/schedules', {
            method: 'POST',
            body: JSON.stringify([scheduleData])
        });
    }

    async updateSchedule(scheduleId, scheduleData) {
        return await this.request(`/schedules/${scheduleId}`, {
            method: 'PUT',
            body: JSON.stringify(scheduleData)
        });
    }

    async deleteSchedule(scheduleId) {
        return await this.request(`/schedules/${scheduleId}`, {
            method: 'DELETE'
        });
    }
}

// Global API service instance
const apiService = new ApiService();

// Utility functions for API calls with error handling
async function safeApiCall(apiCall, errorMessage = 'Terjadi kesalahan') {
    try {
        showLoading(true);
        return await apiCall();
    } catch (error) {
        console.error('API Error:', error);
        showMessage(errorMessage + ': ' + error.message, 'error');
        throw error;
    } finally {
        showLoading(false);
    }
}

// Show loading spinner
function showLoading(show = true) {
    const spinner = document.getElementById('loadingSpinner');
    if (spinner) {
        spinner.style.display = show ? 'flex' : 'none';
    }
}

// Show success/error messages
function showMessage(message, type = 'success') {
    // Remove existing messages
    const existingMessages = document.querySelectorAll('.success-message, .error-message');
    existingMessages.forEach(msg => msg.remove());

    // Create new message
    const messageDiv = document.createElement('div');
    messageDiv.className = type === 'success' ? 'success-message' : 'error-message';
    messageDiv.textContent = message;

    // Insert at the top of main content
    const mainContent = document.getElementById('mainContent');
    if (mainContent) {
        mainContent.insertBefore(messageDiv, mainContent.firstChild);

        // Auto remove after 5 seconds
        setTimeout(() => {
            if (messageDiv.parentNode) {
                messageDiv.remove();
            }
        }, 5000);
    }
}

// Format date utility
function formatDate(epochDay) {
    if (!epochDay) return '-';
    const date = new Date(epochDay * 24 * 60 * 60 * 1000);
    return date.toLocaleDateString('id-ID');
}

// Format time utility
function formatTime(timeString) {
    if (!timeString) return '-';
    return timeString;
}
