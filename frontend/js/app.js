// Main Application Logic
let currentPage = 'dashboard';
let currentData = {
    classes: [],
    students: [],
    teachers: [],
    subjects: [],
    schedules: []
};

// Page Navigation
function showPage(pageName) {
    // Hide all pages
    const pages = document.querySelectorAll('.page');
    pages.forEach(page => page.classList.remove('active'));

    // Show selected page
    const targetPage = document.getElementById(pageName + 'Page');
    if (targetPage) {
        targetPage.classList.add('active');
        currentPage = pageName;

        // Load data for the page
        loadPageData(pageName);
    }
}

// Load data based on current page
async function loadPageData(pageName) {
    switch(pageName) {
        case 'dashboard':
            await loadDashboardData();
            break;
        case 'classes':
            await loadClassesData();
            break;
        case 'students':
            await loadStudentsData();
            break;
        case 'teachers':
            await loadTeachersData();
            break;
        case 'subjects':
            await loadSubjectsData();
            break;
        case 'schedules':
            await loadSchedulesData();
            break;
    }
}

// Dashboard Data Loading
async function loadDashboardData() {
    try {
        showLoading(true);

        // Load all data for dashboard stats
        const [classes, students, teachers, subjects] = await Promise.all([
            safeApiCall(() => apiService.getClasses(), 'Gagal memuat data kelas'),
            safeApiCall(() => apiService.getStudents(), 'Gagal memuat data murid'),
            safeApiCall(() => apiService.getTeachers(), 'Gagal memuat data guru'),
            safeApiCall(() => apiService.getSubjects(), 'Gagal memuat data mata pelajaran')
        ]);

        // Update dashboard stats
        document.getElementById('totalClasses').textContent = classes.length;
        document.getElementById('totalStudents').textContent = students.length;
        document.getElementById('totalTeachers').textContent = teachers.length;
        document.getElementById('totalSubjects').textContent = subjects.length;

    } catch (error) {
        console.error('Error loading dashboard data:', error);
    } finally {
        showLoading(false);
    }
}

// Classes Data Loading and Management
async function loadClassesData() {
    try {
        const classes = await safeApiCall(() => apiService.getClasses(), 'Gagal memuat data kelas');
        currentData.classes = classes;
        renderClassesTable(classes);
    } catch (error) {
        console.error('Error loading classes:', error);
    }
}

function renderClassesTable(classes) {
    const tbody = document.querySelector('#classesTable tbody');
    tbody.innerHTML = '';

    classes.forEach(kelas => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${kelas.className || '-'}</td>
            <td>${kelas.gradeLevel || '-'}</td>
            <td>${kelas.academicYear || '-'}</td>
            <td>${kelas.homeroomTeacher?.teacherName || '-'}</td>
            <td>${kelas.currentStudentCount || 0}</td>
            <td>
                <button class="btn-secondary" onclick="viewClassDetail('${kelas.secureId}')">Detail</button>
                <button class="btn-secondary" onclick="editClass('${kelas.secureId}')">Edit</button>
                <button class="btn-danger" onclick="deleteClass('${kelas.secureId}')">Hapus</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

// Students Data Loading and Management
async function loadStudentsData() {
    try {
        const students = await safeApiCall(() => apiService.getStudents(), 'Gagal memuat data murid');
        currentData.students = students;
        renderStudentsTable(students);
    } catch (error) {
        console.error('Error loading students:', error);
    }
}

function renderStudentsTable(students) {
    const tbody = document.querySelector('#studentsTable tbody');
    tbody.innerHTML = '';

    students.forEach(student => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${student.studentName || '-'}</td>
            <td>${formatDate(student.studentBirthDate)}</td>
            <td>${student.studentGender || '-'}</td>
            <td>${student.studentAddress || '-'}</td>
            <td>-</td>
            <td>
                <button class="btn-secondary" onclick="viewStudentDetail('${student.secureId}')">Detail</button>
                <button class="btn-secondary" onclick="editStudent('${student.secureId}')">Edit</button>
                <button class="btn-danger" onclick="deleteStudent('${student.secureId}')">Hapus</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

// Teachers Data Loading and Management
async function loadTeachersData() {
    try {
        const teachers = await safeApiCall(() => apiService.getTeachers(), 'Gagal memuat data guru');
        currentData.teachers = teachers;
        renderTeachersTable(teachers);
    } catch (error) {
        console.error('Error loading teachers:', error);
    }
}

function renderTeachersTable(teachers) {
    const tbody = document.querySelector('#teachersTable tbody');
    tbody.innerHTML = '';

    teachers.forEach(teacher => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${teacher.teacherName || '-'}</td>
            <td>${formatDate(teacher.teacherBirthDate)}</td>
            <td>${teacher.teacherGender || '-'}</td>
            <td>${teacher.teacherAddress || '-'}</td>
            <td>-</td>
            <td>
                <button class="btn-secondary" onclick="viewTeacherDetail('${teacher.secureId}')">Detail</button>
                <button class="btn-secondary" onclick="editTeacher('${teacher.secureId}')">Edit</button>
                <button class="btn-danger" onclick="deleteTeacher('${teacher.secureId}')">Hapus</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

// Subjects Data Loading and Management
async function loadSubjectsData() {
    try {
        const subjects = await safeApiCall(() => apiService.getSubjects(), 'Gagal memuat data mata pelajaran');
        currentData.subjects = subjects;
        renderSubjectsTable(subjects);
    } catch (error) {
        console.error('Error loading subjects:', error);
    }
}

function renderSubjectsTable(subjects) {
    const tbody = document.querySelector('#subjectsTable tbody');
    tbody.innerHTML = '';

    subjects.forEach(subject => {
        const row = document.createElement('tr');
        const teacherNames = subject.eligibleTeachers?.map(t => t.teacherName).join(', ') || '-';

        row.innerHTML = `
            <td>${subject.name || '-'}</td>
            <td>${subject.description || '-'}</td>
            <td>${teacherNames}</td>
            <td>
                <button class="btn-secondary" onclick="viewSubjectDetail('${subject.secureId}')">Detail</button>
                <button class="btn-secondary" onclick="editSubject('${subject.secureId}')">Edit</button>
                <button class="btn-danger" onclick="deleteSubject('${subject.secureId}')">Hapus</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

// Schedules Data Loading and Management
async function loadSchedulesData() {
    try {
        const schedules = await safeApiCall(() => apiService.getSchedules(), 'Gagal memuat data jadwal');
        currentData.schedules = schedules;
        renderSchedulesTable(schedules);
    } catch (error) {
        console.error('Error loading schedules:', error);
    }
}

function renderSchedulesTable(schedules) {
    const tbody = document.querySelector('#schedulesTable tbody');
    tbody.innerHTML = '';

    schedules.forEach(schedule => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${schedule.day || '-'}</td>
            <td>${schedule.startTime || '-'} - ${schedule.endTime || '-'}</td>
            <td>${schedule.schoolClass?.className || '-'}</td>
            <td>${schedule.subject?.name || '-'}</td>
            <td>${schedule.teacher?.teacherName || '-'}</td>
            <td>${schedule.semester || '-'}</td>
            <td>
                <button class="btn-secondary" onclick="viewScheduleDetail('${schedule.secureId}')">Detail</button>
                <button class="btn-secondary" onclick="editSchedule('${schedule.secureId}')">Edit</button>
                <button class="btn-danger" onclick="deleteSchedule('${schedule.secureId}')">Hapus</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

// Detail View Functions
async function viewClassDetail(classId) {
    try {
        const classDetail = await safeApiCall(() => apiService.getClassDetail(classId), 'Gagal memuat detail kelas');
        showClassDetailModal(classDetail);
    } catch (error) {
        console.error('Error viewing class detail:', error);
    }
}

async function viewStudentDetail(studentId) {
    try {
        const studentDetail = await safeApiCall(() => apiService.getStudentDetail(studentId), 'Gagal memuat detail murid');
        showStudentDetailModal(studentDetail);
    } catch (error) {
        console.error('Error viewing student detail:', error);
    }
}

async function viewTeacherDetail(teacherId) {
    try {
        const teacherDetail = await safeApiCall(() => apiService.getTeacherDetail(teacherId), 'Gagal memuat detail guru');
        showTeacherDetailModal(teacherDetail);
    } catch (error) {
        console.error('Error viewing teacher detail:', error);
    }
}

async function viewSubjectDetail(subjectId) {
    try {
        const subjectDetail = await safeApiCall(() => apiService.getSubjectDetail(subjectId), 'Gagal memuat detail mata pelajaran');
        showSubjectDetailModal(subjectDetail);
    } catch (error) {
        console.error('Error viewing subject detail:', error);
    }
}

async function viewScheduleDetail(scheduleId) {
    try {
        const scheduleDetail = await safeApiCall(() => apiService.getScheduleDetail(scheduleId), 'Gagal memuat detail jadwal');
        showScheduleDetailModal(scheduleDetail);
    } catch (error) {
        console.error('Error viewing schedule detail:', error);
    }
}

// Delete Functions
async function deleteClass(classId) {
    if (confirm('Apakah Anda yakin ingin menghapus kelas ini?')) {
        try {
            await safeApiCall(() => apiService.deleteClass(classId), 'Gagal menghapus kelas');
            showMessage('Kelas berhasil dihapus', 'success');
            loadClassesData();
        } catch (error) {
            console.error('Error deleting class:', error);
        }
    }
}

async function deleteStudent(studentId) {
    if (confirm('Apakah Anda yakin ingin menghapus murid ini?')) {
        try {
            await safeApiCall(() => apiService.deleteStudent(studentId), 'Gagal menghapus murid');
            showMessage('Murid berhasil dihapus', 'success');
            loadStudentsData();
        } catch (error) {
            console.error('Error deleting student:', error);
        }
    }
}

async function deleteTeacher(teacherId) {
    if (confirm('Apakah Anda yakin ingin menghapus guru ini?')) {
        try {
            await safeApiCall(() => apiService.deleteTeacher(teacherId), 'Gagal menghapus guru');
            showMessage('Guru berhasil dihapus', 'success');
            loadTeachersData();
        } catch (error) {
            console.error('Error deleting teacher:', error);
        }
    }
}

async function deleteSubject(subjectId) {
    if (confirm('Apakah Anda yakin ingin menghapus mata pelajaran ini?')) {
        try {
            await safeApiCall(() => apiService.deleteSubject(subjectId), 'Gagal menghapus mata pelajaran');
            showMessage('Mata pelajaran berhasil dihapus', 'success');
            loadSubjectsData();
        } catch (error) {
            console.error('Error deleting subject:', error);
        }
    }
}

async function deleteSchedule(scheduleId) {
    if (confirm('Apakah Anda yakin ingin menghapus jadwal ini?')) {
        try {
            await safeApiCall(() => apiService.deleteSchedule(scheduleId), 'Gagal menghapus jadwal');
            showMessage('Jadwal berhasil dihapus', 'success');
            loadSchedulesData();
        } catch (error) {
            console.error('Error deleting schedule:', error);
        }
    }
}

// Edit Functions (will be implemented in modals.js)
function editClass(classId) {
    const classData = currentData.classes.find(c => c.secureId === classId);
    if (classData) {
        showEditClassModal(classData);
    }
}

function editStudent(studentId) {
    const studentData = currentData.students.find(s => s.secureId === studentId);
    if (studentData) {
        showEditStudentModal(studentData);
    }
}

function editTeacher(teacherId) {
    const teacherData = currentData.teachers.find(t => t.secureId === teacherId);
    if (teacherData) {
        showEditTeacherModal(teacherData);
    }
}

function editSubject(subjectId) {
    const subjectData = currentData.subjects.find(s => s.secureId === subjectId);
    if (subjectData) {
        showEditSubjectModal(subjectData);
    }
}

function editSchedule(scheduleId) {
    const scheduleData = currentData.schedules.find(s => s.secureId === scheduleId);
    if (scheduleData) {
        showEditScheduleModal(scheduleData);
    }
}
