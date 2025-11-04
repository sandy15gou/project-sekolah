// Modal Management System
let currentModal = null;

// Show Modal
function showModal(modalId) {
    // Load modal content if not exists
    loadModalContent(modalId);

    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = 'flex';
        currentModal = modalId;
        document.body.style.overflow = 'hidden';
    }
}

// Hide Modal
function hideModal(modalId = null) {
    const targetModalId = modalId || currentModal;
    const modal = document.getElementById(targetModalId);
    if (modal) {
        modal.style.display = 'none';
        document.body.style.overflow = 'auto';
        currentModal = null;

        // Clear form data
        const forms = modal.querySelectorAll('form');
        forms.forEach(form => form.reset());
    }
}

// Load Modal Content
function loadModalContent(modalId) {
    const modalsContainer = document.getElementById('modalsContainer');

    // Check if modal already exists
    if (document.getElementById(modalId)) {
        return;
    }

    let modalHTML = '';

    switch(modalId) {
        case 'classModal':
            modalHTML = createClassModal();
            break;
        case 'studentModal':
            modalHTML = createStudentModal();
            break;
        case 'teacherModal':
            modalHTML = createTeacherModal();
            break;
        case 'subjectModal':
            modalHTML = createSubjectModal();
            break;
        case 'scheduleModal':
            modalHTML = createScheduleModal();
            break;
        case 'classDetailModal':
            modalHTML = createClassDetailModal();
            break;
        case 'studentDetailModal':
            modalHTML = createStudentDetailModal();
            break;
        case 'teacherDetailModal':
            modalHTML = createTeacherDetailModal();
            break;
        case 'subjectDetailModal':
            modalHTML = createSubjectDetailModal();
            break;
        case 'scheduleDetailModal':
            modalHTML = createScheduleDetailModal();
            break;
    }

    modalsContainer.innerHTML += modalHTML;
}

// Class Modal
function createClassModal() {
    return `
        <div id="classModal" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h3 id="classModalTitle">Tambah Kelas Baru</h3>
                    <span class="close" onclick="hideModal('classModal')">&times;</span>
                </div>
                <form id="classForm">
                    <div class="form-group">
                        <label for="className">Nama Kelas:</label>
                        <input type="text" id="className" name="className" required>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="gradeLevel">Tingkat:</label>
                            <select id="gradeLevel" name="gradeLevel" required>
                                <option value="">Pilih Tingkat</option>
                                <option value="1">Kelas 1</option>
                                <option value="2">Kelas 2</option>
                                <option value="3">Kelas 3</option>
                                <option value="4">Kelas 4</option>
                                <option value="5">Kelas 5</option>
                                <option value="6">Kelas 6</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="academicYear">Tahun Ajaran:</label>
                            <input type="text" id="academicYear" name="academicYear" placeholder="2024/2025" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="homeroomTeacher">Wali Kelas:</label>
                        <select id="homeroomTeacher" name="homeroomTeacher" required>
                            <option value="">Pilih Guru</option>
                        </select>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="maxCapacity">Kapasitas Maksimal:</label>
                            <input type="number" id="maxCapacity" name="maxCapacity" value="30" min="1" max="50">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="description">Deskripsi:</label>
                        <textarea id="description" name="description" rows="3"></textarea>
                    </div>
                    <div class="modal-actions">
                        <button type="button" class="btn-secondary" onclick="hideModal('classModal')">Batal</button>
                        <button type="submit" class="btn-primary">Simpan</button>
                    </div>
                </form>
            </div>
        </div>
    `;
}

// Student Modal
function createStudentModal() {
    return `
        <div id="studentModal" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h3 id="studentModalTitle">Tambah Murid Baru</h3>
                    <span class="close" onclick="hideModal('studentModal')">&times;</span>
                </div>
                <form id="studentForm">
                    <div class="form-group">
                        <label for="studentName">Nama Lengkap:</label>
                        <input type="text" id="studentName" name="studentName" required>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="studentBirthDate">Tanggal Lahir:</label>
                            <input type="date" id="studentBirthDate" name="studentBirthDate" required>
                        </div>
                        <div class="form-group">
                            <label for="studentGender">Jenis Kelamin:</label>
                            <select id="studentGender" name="studentGender" required>
                                <option value="">Pilih</option>
                                <option value="L">Laki-laki</option>
                                <option value="P">Perempuan</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="studentAddress">Alamat:</label>
                        <textarea id="studentAddress" name="studentAddress" rows="3" required></textarea>
                    </div>
                    <div class="modal-actions">
                        <button type="button" class="btn-secondary" onclick="hideModal('studentModal')">Batal</button>
                        <button type="submit" class="btn-primary">Simpan</button>
                    </div>
                </form>
            </div>
        </div>
    `;
}

// Teacher Modal
function createTeacherModal() {
    return `
        <div id="teacherModal" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h3 id="teacherModalTitle">Tambah Guru Baru</h3>
                    <span class="close" onclick="hideModal('teacherModal')">&times;</span>
                </div>
                <form id="teacherForm">
                    <div class="form-group">
                        <label for="teacherName">Nama Lengkap:</label>
                        <input type="text" id="teacherName" name="teacherName" required>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="teacherBirthDate">Tanggal Lahir:</label>
                            <input type="date" id="teacherBirthDate" name="teacherBirthDate" required>
                        </div>
                        <div class="form-group">
                            <label for="teacherGender">Jenis Kelamin:</label>
                            <select id="teacherGender" name="teacherGender" required>
                                <option value="">Pilih</option>
                                <option value="L">Laki-laki</option>
                                <option value="P">Perempuan</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="teacherAddress">Alamat:</label>
                        <textarea id="teacherAddress" name="teacherAddress" rows="3" required></textarea>
                    </div>
                    <div class="modal-actions">
                        <button type="button" class="btn-secondary" onclick="hideModal('teacherModal')">Batal</button>
                        <button type="submit" class="btn-primary">Simpan</button>
                    </div>
                </form>
            </div>
        </div>
    `;
}

// Subject Modal
function createSubjectModal() {
    return `
        <div id="subjectModal" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h3 id="subjectModalTitle">Tambah Mata Pelajaran Baru</h3>
                    <span class="close" onclick="hideModal('subjectModal')">&times;</span>
                </div>
                <form id="subjectForm">
                    <div class="form-group">
                        <label for="subjectName">Nama Mata Pelajaran:</label>
                        <input type="text" id="subjectName" name="subjectName" required>
                    </div>
                    <div class="form-group">
                        <label for="subjectDescription">Deskripsi:</label>
                        <textarea id="subjectDescription" name="subjectDescription" rows="3"></textarea>
                    </div>
                    <div class="form-group">
                        <label for="eligibleTeachers">Guru Yang Dapat Mengajar:</label>
                        <select id="eligibleTeachers" name="eligibleTeachers" multiple>
                        </select>
                        <small>Tahan Ctrl untuk memilih lebih dari satu guru</small>
                    </div>
                    <div class="modal-actions">
                        <button type="button" class="btn-secondary" onclick="hideModal('subjectModal')">Batal</button>
                        <button type="submit" class="btn-primary">Simpan</button>
                    </div>
                </form>
            </div>
        </div>
    `;
}

// Schedule Modal
function createScheduleModal() {
    return `
        <div id="scheduleModal" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h3 id="scheduleModalTitle">Tambah Jadwal Baru</h3>
                    <span class="close" onclick="hideModal('scheduleModal')">&times;</span>
                </div>
                <form id="scheduleForm">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="scheduleDay">Hari:</label>
                            <select id="scheduleDay" name="scheduleDay" required>
                                <option value="">Pilih Hari</option>
                                <option value="Senin">Senin</option>
                                <option value="Selasa">Selasa</option>
                                <option value="Rabu">Rabu</option>
                                <option value="Kamis">Kamis</option>
                                <option value="Jumat">Jumat</option>
                                <option value="Sabtu">Sabtu</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="scheduleSemester">Semester:</label>
                            <select id="scheduleSemester" name="scheduleSemester" required>
                                <option value="">Pilih Semester</option>
                                <option value="1">Semester 1</option>
                                <option value="2">Semester 2</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="startTime">Waktu Mulai:</label>
                            <input type="time" id="startTime" name="startTime" required>
                        </div>
                        <div class="form-group">
                            <label for="endTime">Waktu Selesai:</label>
                            <input type="time" id="endTime" name="endTime" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="scheduleClass">Kelas:</label>
                        <select id="scheduleClass" name="scheduleClass" required>
                            <option value="">Pilih Kelas</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="scheduleSubject">Mata Pelajaran:</label>
                        <select id="scheduleSubject" name="scheduleSubject" required>
                            <option value="">Pilih Mata Pelajaran</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="scheduleTeacher">Guru:</label>
                        <select id="scheduleTeacher" name="scheduleTeacher" required>
                            <option value="">Pilih Guru</option>
                        </select>
                    </div>
                    <div class="modal-actions">
                        <button type="button" class="btn-secondary" onclick="hideModal('scheduleModal')">Batal</button>
                        <button type="submit" class="btn-primary">Simpan</button>
                    </div>
                </form>
            </div>
        </div>
    `;
}

// Detail Modals
function createClassDetailModal() {
    return `
        <div id="classDetailModal" class="modal">
            <div class="modal-content large">
                <div class="modal-header">
                    <h3>Detail Kelas</h3>
                    <span class="close" onclick="hideModal('classDetailModal')">&times;</span>
                </div>
                <div id="classDetailContent" class="detail-content">
                    <!-- Content will be populated by JavaScript -->
                </div>
            </div>
        </div>
    `;
}

function createStudentDetailModal() {
    return `
        <div id="studentDetailModal" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h3>Detail Murid</h3>
                    <span class="close" onclick="hideModal('studentDetailModal')">&times;</span>
                </div>
                <div id="studentDetailContent" class="detail-content">
                    <!-- Content will be populated by JavaScript -->
                </div>
            </div>
        </div>
    `;
}

function createTeacherDetailModal() {
    return `
        <div id="teacherDetailModal" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h3>Detail Guru</h3>
                    <span class="close" onclick="hideModal('teacherDetailModal')">&times;</span>
                </div>
                <div id="teacherDetailContent" class="detail-content">
                    <!-- Content will be populated by JavaScript -->
                </div>
            </div>
        </div>
    `;
}

function createSubjectDetailModal() {
    return `
        <div id="subjectDetailModal" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h3>Detail Mata Pelajaran</h3>
                    <span class="close" onclick="hideModal('subjectDetailModal')">&times;</span>
                </div>
                <div id="subjectDetailContent" class="detail-content">
                    <!-- Content will be populated by JavaScript -->
                </div>
            </div>
        </div>
    `;
}

function createScheduleDetailModal() {
    return `
        <div id="scheduleDetailModal" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h3>Detail Jadwal</h3>
                    <span class="close" onclick="hideModal('scheduleDetailModal')">&times;</span>
                </div>
                <div id="scheduleDetailContent" class="detail-content">
                    <!-- Content will be populated by JavaScript -->
                </div>
            </div>
        </div>
    `;
}

// Form Handlers
document.addEventListener('DOMContentLoaded', function() {
    // Class Form Handler
    document.addEventListener('submit', async function(e) {
        if (e.target.id === 'classForm') {
            e.preventDefault();
            await handleClassFormSubmit(e.target);
        } else if (e.target.id === 'studentForm') {
            e.preventDefault();
            await handleStudentFormSubmit(e.target);
        } else if (e.target.id === 'teacherForm') {
            e.preventDefault();
            await handleTeacherFormSubmit(e.target);
        } else if (e.target.id === 'subjectForm') {
            e.preventDefault();
            await handleSubjectFormSubmit(e.target);
        } else if (e.target.id === 'scheduleForm') {
            e.preventDefault();
            await handleScheduleFormSubmit(e.target);
        }
    });
});

// Form Submit Handlers
async function handleClassFormSubmit(form) {
    const formData = new FormData(form);
    const classData = {
        className: formData.get('className'),
        gradeLevel: formData.get('gradeLevel'),
        academicYear: formData.get('academicYear'),
        homeroomTeacher: formData.get('homeroomTeacher'),
        maxCapacity: parseInt(formData.get('maxCapacity')),
        description: formData.get('description')
    };

    try {
        const isEdit = form.dataset.mode === 'edit';
        if (isEdit) {
            await safeApiCall(() => apiService.updateClass(form.dataset.classId, classData), 'Gagal mengupdate kelas');
            showMessage('Kelas berhasil diupdate', 'success');
        } else {
            await safeApiCall(() => apiService.createClass(classData), 'Gagal membuat kelas');
            showMessage('Kelas berhasil dibuat', 'success');
        }

        hideModal('classModal');
        loadClassesData();
    } catch (error) {
        console.error('Error submitting class form:', error);
    }
}

async function handleStudentFormSubmit(form) {
    const formData = new FormData(form);
    const birthDate = new Date(formData.get('studentBirthDate'));
    const epochDay = Math.floor(birthDate.getTime() / (1000 * 60 * 60 * 24));

    const studentData = {
        studentName: formData.get('studentName'),
        studentBirthDate: epochDay,
        studentGender: formData.get('studentGender'),
        studentAddress: formData.get('studentAddress')
    };

    try {
        const isEdit = form.dataset.mode === 'edit';
        if (isEdit) {
            await safeApiCall(() => apiService.updateStudent(form.dataset.studentId, studentData), 'Gagal mengupdate murid');
            showMessage('Murid berhasil diupdate', 'success');
        } else {
            await safeApiCall(() => apiService.createStudent(studentData), 'Gagal membuat murid');
            showMessage('Murid berhasil ditambahkan', 'success');
        }

        hideModal('studentModal');
        loadStudentsData();
    } catch (error) {
        console.error('Error submitting student form:', error);
    }
}

async function handleTeacherFormSubmit(form) {
    const formData = new FormData(form);
    const birthDate = new Date(formData.get('teacherBirthDate'));
    const epochDay = Math.floor(birthDate.getTime() / (1000 * 60 * 60 * 24));

    const teacherData = {
        teacherName: formData.get('teacherName'),
        teacherBirthDate: epochDay,
        teacherGender: formData.get('teacherGender'),
        teacherAddress: formData.get('teacherAddress')
    };

    try {
        const isEdit = form.dataset.mode === 'edit';
        if (isEdit) {
            await safeApiCall(() => apiService.updateTeacher(form.dataset.teacherId, teacherData), 'Gagal mengupdate guru');
            showMessage('Guru berhasil diupdate', 'success');
        } else {
            await safeApiCall(() => apiService.createTeacher(teacherData), 'Gagal membuat guru');
            showMessage('Guru berhasil ditambahkan', 'success');
        }

        hideModal('teacherModal');
        loadTeachersData();
    } catch (error) {
        console.error('Error submitting teacher form:', error);
    }
}

async function handleSubjectFormSubmit(form) {
    const formData = new FormData(form);
    const selectedTeachers = Array.from(formData.getAll('eligibleTeachers'));

    const subjectData = {
        name: formData.get('subjectName'),
        description: formData.get('subjectDescription'),
        eligibleTeachers: selectedTeachers
    };

    try {
        const isEdit = form.dataset.mode === 'edit';
        if (isEdit) {
            await safeApiCall(() => apiService.updateSubject(form.dataset.subjectId, subjectData), 'Gagal mengupdate mata pelajaran');
            showMessage('Mata pelajaran berhasil diupdate', 'success');
        } else {
            await safeApiCall(() => apiService.createSubject(subjectData), 'Gagal membuat mata pelajaran');
            showMessage('Mata pelajaran berhasil ditambahkan', 'success');
        }

        hideModal('subjectModal');
        loadSubjectsData();
    } catch (error) {
        console.error('Error submitting subject form:', error);
    }
}

async function handleScheduleFormSubmit(form) {
    const formData = new FormData(form);

    const scheduleData = {
        day: formData.get('scheduleDay'),
        startTime: formData.get('startTime'),
        endTime: formData.get('endTime'),
        semester: formData.get('scheduleSemester'),
        classId: formData.get('scheduleClass'),
        subjectId: formData.get('scheduleSubject'),
        teacherId: formData.get('scheduleTeacher')
    };

    try {
        const isEdit = form.dataset.mode === 'edit';
        if (isEdit) {
            await safeApiCall(() => apiService.updateSchedule(form.dataset.scheduleId, scheduleData), 'Gagal mengupdate jadwal');
            showMessage('Jadwal berhasil diupdate', 'success');
        } else {
            await safeApiCall(() => apiService.createSchedule(scheduleData), 'Gagal membuat jadwal');
            showMessage('Jadwal berhasil ditambahkan', 'success');
        }

        hideModal('scheduleModal');
        loadSchedulesData();
    } catch (error) {
        console.error('Error submitting schedule form:', error);
    }
}

// Populate form dropdowns
async function populateFormDropdowns() {
    try {
        // Load teachers for class form
        const teachers = await apiService.getTeachers();
        const teacherSelect = document.getElementById('homeroomTeacher');
        if (teacherSelect) {
            teacherSelect.innerHTML = '<option value="">Pilih Guru</option>';
            teachers.forEach(teacher => {
                teacherSelect.innerHTML += `<option value="${teacher.secureId}">${teacher.teacherName}</option>`;
            });
        }

        // Load teachers for eligible teachers in subject form
        const eligibleTeachersSelect = document.getElementById('eligibleTeachers');
        if (eligibleTeachersSelect) {
            eligibleTeachersSelect.innerHTML = '';
            teachers.forEach(teacher => {
                eligibleTeachersSelect.innerHTML += `<option value="${teacher.secureId}">${teacher.teacherName}</option>`;
            });
        }

        // Load classes for schedule form
        const classes = await apiService.getClasses();
        const classSelect = document.getElementById('scheduleClass');
        if (classSelect) {
            classSelect.innerHTML = '<option value="">Pilih Kelas</option>';
            classes.forEach(kelas => {
                classSelect.innerHTML += `<option value="${kelas.secureId}">${kelas.className}</option>`;
            });
        }

        // Load subjects for schedule form
        const subjects = await apiService.getSubjects();
        const subjectSelect = document.getElementById('scheduleSubject');
        if (subjectSelect) {
            subjectSelect.innerHTML = '<option value="">Pilih Mata Pelajaran</option>';
            subjects.forEach(subject => {
                subjectSelect.innerHTML += `<option value="${subject.secureId}">${subject.name}</option>`;
            });
        }

        // Load teachers for schedule form
        const scheduleTeacherSelect = document.getElementById('scheduleTeacher');
        if (scheduleTeacherSelect) {
            scheduleTeacherSelect.innerHTML = '<option value="">Pilih Guru</option>';
            teachers.forEach(teacher => {
                scheduleTeacherSelect.innerHTML += `<option value="${teacher.secureId}">${teacher.teacherName}</option>`;
            });
        }

    } catch (error) {
        console.error('Error populating dropdowns:', error);
    }
}

// Show detail modals
function showClassDetailModal(classDetail) {
    showModal('classDetailModal');
    const content = document.getElementById('classDetailContent');

    let studentsHTML = '';
    if (classDetail.students && classDetail.students.length > 0) {
        studentsHTML = classDetail.students.map(student =>
            `<li>${student.studentName} (${student.studentGender === 'L' ? 'Laki-laki' : 'Perempuan'})</li>`
        ).join('');
    } else {
        studentsHTML = '<li>Belum ada murid</li>';
    }

    let schedulesHTML = '';
    if (classDetail.schedules && classDetail.schedules.length > 0) {
        schedulesHTML = classDetail.schedules.map(schedule =>
            `<li>${schedule.day} ${schedule.startTime}-${schedule.endTime}: ${schedule.subject?.name || '-'} (${schedule.teacher?.teacherName || '-'})</li>`
        ).join('');
    } else {
        schedulesHTML = '<li>Belum ada jadwal</li>';
    }

    content.innerHTML = `
        <div class="detail-section">
            <h4>Informasi Kelas</h4>
            <p><strong>Nama Kelas:</strong> ${classDetail.className}</p>
            <p><strong>Tingkat:</strong> Kelas ${classDetail.gradeLevel}</p>
            <p><strong>Tahun Ajaran:</strong> ${classDetail.academicYear}</p>
            <p><strong>Wali Kelas:</strong> ${classDetail.homeroomTeacher?.teacherName || '-'}</p>
            <p><strong>Kapasitas:</strong> ${classDetail.currentStudentCount || 0}/${classDetail.maxCapacity || 30}</p>
            <p><strong>Deskripsi:</strong> ${classDetail.description || '-'}</p>
        </div>
        
        <div class="detail-section">
            <h4>Daftar Murid (${classDetail.currentStudentCount || 0})</h4>
            <ul>${studentsHTML}</ul>
        </div>
        
        <div class="detail-section">
            <h4>Jadwal Pelajaran</h4>
            <ul>${schedulesHTML}</ul>
        </div>
        
        <div class="detail-section">
            <h4>Statistik</h4>
            <p><strong>Total Murid:</strong> ${classDetail.currentStudentCount || 0}</p>
            <p><strong>Total Mata Pelajaran:</strong> ${classDetail.totalSubjects || 0}</p>
            <p><strong>Total Guru:</strong> ${classDetail.totalTeachers || 0}</p>
        </div>
    `;
}

function showStudentDetailModal(studentDetail) {
    showModal('studentDetailModal');
    const content = document.getElementById('studentDetailContent');

    content.innerHTML = `
        <div class="detail-section">
            <h4>Informasi Murid</h4>
            <p><strong>Nama:</strong> ${studentDetail.studentName}</p>
            <p><strong>Tanggal Lahir:</strong> ${formatDate(studentDetail.studentBirthDate)}</p>
            <p><strong>Jenis Kelamin:</strong> ${studentDetail.studentGender === 'L' ? 'Laki-laki' : 'Perempuan'}</p>
            <p><strong>Alamat:</strong> ${studentDetail.studentAddress}</p>
        </div>
    `;
}

function showTeacherDetailModal(teacherDetail) {
    showModal('teacherDetailModal');
    const content = document.getElementById('teacherDetailContent');

    content.innerHTML = `
        <div class="detail-section">
            <h4>Informasi Guru</h4>
            <p><strong>Nama:</strong> ${teacherDetail.teacherName}</p>
            <p><strong>Tanggal Lahir:</strong> ${formatDate(teacherDetail.teacherBirthDate)}</p>
            <p><strong>Jenis Kelamin:</strong> ${teacherDetail.teacherGender === 'L' ? 'Laki-laki' : 'Perempuan'}</p>
            <p><strong>Alamat:</strong> ${teacherDetail.teacherAddress}</p>
        </div>
    `;
}

function showSubjectDetailModal(subjectDetail) {
    showModal('subjectDetailModal');
    const content = document.getElementById('subjectDetailContent');

    let teachersHTML = '';
    if (subjectDetail.eligibleTeachers && subjectDetail.eligibleTeachers.length > 0) {
        teachersHTML = subjectDetail.eligibleTeachers.map(teacher =>
            `<li>${teacher.teacherName}</li>`
        ).join('');
    } else {
        teachersHTML = '<li>Belum ada guru yang bisa mengajar</li>';
    }

    content.innerHTML = `
        <div class="detail-section">
            <h4>Informasi Mata Pelajaran</h4>
            <p><strong>Nama:</strong> ${subjectDetail.name}</p>
            <p><strong>Deskripsi:</strong> ${subjectDetail.description || '-'}</p>
        </div>
        
        <div class="detail-section">
            <h4>Guru Yang Dapat Mengajar</h4>
            <ul>${teachersHTML}</ul>
        </div>
    `;
}

function showScheduleDetailModal(scheduleDetail) {
    showModal('scheduleDetailModal');
    const content = document.getElementById('scheduleDetailContent');

    content.innerHTML = `
        <div class="detail-section">
            <h4>Informasi Jadwal</h4>
            <p><strong>Hari:</strong> ${scheduleDetail.day}</p>
            <p><strong>Waktu:</strong> ${scheduleDetail.startTime} - ${scheduleDetail.endTime}</p>
            <p><strong>Semester:</strong> ${scheduleDetail.semester}</p>
            <p><strong>Kelas:</strong> ${scheduleDetail.schoolClass?.className || '-'}</p>
            <p><strong>Mata Pelajaran:</strong> ${scheduleDetail.subject?.name || '-'}</p>
            <p><strong>Guru:</strong> ${scheduleDetail.teacher?.teacherName || '-'}</p>
        </div>
    `;
}

// Edit Functions
function showEditClassModal(classData) {
    showModal('classModal');
    document.getElementById('classModalTitle').textContent = 'Edit Kelas';

    const form = document.getElementById('classForm');
    form.dataset.mode = 'edit';
    form.dataset.classId = classData.secureId;

    populateFormDropdowns().then(() => {
        document.getElementById('className').value = classData.className || '';
        document.getElementById('gradeLevel').value = classData.gradeLevel || '';
        document.getElementById('academicYear').value = classData.academicYear || '';
        document.getElementById('homeroomTeacher').value = classData.homeroomTeacher?.secureId || '';
        document.getElementById('maxCapacity').value = classData.maxCapacity || 30;
        document.getElementById('description').value = classData.description || '';
    });
}

function showEditStudentModal(studentData) {
    showModal('studentModal');
    document.getElementById('studentModalTitle').textContent = 'Edit Murid';

    const form = document.getElementById('studentForm');
    form.dataset.mode = 'edit';
    form.dataset.studentId = studentData.secureId;

    document.getElementById('studentName').value = studentData.studentName || '';
    if (studentData.studentBirthDate) {
        const date = new Date(studentData.studentBirthDate * 24 * 60 * 60 * 1000);
        document.getElementById('studentBirthDate').value = date.toISOString().split('T')[0];
    }
    document.getElementById('studentGender').value = studentData.studentGender || '';
    document.getElementById('studentAddress').value = studentData.studentAddress || '';
}

function showEditTeacherModal(teacherData) {
    showModal('teacherModal');
    document.getElementById('teacherModalTitle').textContent = 'Edit Guru';

    const form = document.getElementById('teacherForm');
    form.dataset.mode = 'edit';
    form.dataset.teacherId = teacherData.secureId;

    document.getElementById('teacherName').value = teacherData.teacherName || '';
    if (teacherData.teacherBirthDate) {
        const date = new Date(teacherData.teacherBirthDate * 24 * 60 * 60 * 1000);
        document.getElementById('teacherBirthDate').value = date.toISOString().split('T')[0];
    }
    document.getElementById('teacherGender').value = teacherData.teacherGender || '';
    document.getElementById('teacherAddress').value = teacherData.teacherAddress || '';
}

function showEditSubjectModal(subjectData) {
    showModal('subjectModal');
    document.getElementById('subjectModalTitle').textContent = 'Edit Mata Pelajaran';

    const form = document.getElementById('subjectForm');
    form.dataset.mode = 'edit';
    form.dataset.subjectId = subjectData.secureId;

    populateFormDropdowns().then(() => {
        document.getElementById('subjectName').value = subjectData.name || '';
        document.getElementById('subjectDescription').value = subjectData.description || '';

        if (subjectData.eligibleTeachers) {
            const teacherIds = subjectData.eligibleTeachers.map(t => t.secureId);
            const select = document.getElementById('eligibleTeachers');
            Array.from(select.options).forEach(option => {
                option.selected = teacherIds.includes(option.value);
            });
        }
    });
}

function showEditScheduleModal(scheduleData) {
    showModal('scheduleModal');
    document.getElementById('scheduleModalTitle').textContent = 'Edit Jadwal';

    const form = document.getElementById('scheduleForm');
    form.dataset.mode = 'edit';
    form.dataset.scheduleId = scheduleData.secureId;

    populateFormDropdowns().then(() => {
        document.getElementById('scheduleDay').value = scheduleData.day || '';
        document.getElementById('startTime').value = scheduleData.startTime || '';
        document.getElementById('endTime').value = scheduleData.endTime || '';
        document.getElementById('scheduleSemester').value = scheduleData.semester || '';
        document.getElementById('scheduleClass').value = scheduleData.schoolClass?.secureId || '';
        document.getElementById('scheduleSubject').value = scheduleData.subject?.secureId || '';
        document.getElementById('scheduleTeacher').value = scheduleData.teacher?.secureId || '';
    });
}

// Initialize dropdowns when modals are shown
document.addEventListener('click', function(e) {
    if (e.target.classList.contains('btn-primary') && e.target.textContent.includes('Tambah')) {
        setTimeout(populateFormDropdowns, 100);
    }
});

// Close modal when clicking outside
window.addEventListener('click', function(e) {
    if (e.target.classList.contains('modal')) {
        hideModal();
    }
});

// Keyboard shortcuts
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape' && currentModal) {
        hideModal();
    }
});
