'use client';
import { useState, useEffect } from 'react';
import { DAY_OPTIONS } from '@/lib/utils';

export default function ScheduleForm({ initialData, classes = [], subjects = [], teachers = [], onSubmit, loading }) {
  const [form, setForm] = useState({
    day: 'Monday',
    start_time: '',
    end_time: '',
    semester: '1',
    school_class_id: '',
    subject_id: '',
    teacher_id: '',
  });
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (initialData) {
      setForm({
        day: initialData.day || 'Monday',
        start_time: initialData.start_time || '',
        end_time: initialData.end_time || '',
        semester: initialData.semester || '1',
        school_class_id: initialData.school_class?.secure_id || '',
        subject_id: initialData.subject?.secure_id || '',
        teacher_id: initialData.teacher?.secure_id || '',
      });
    }
  }, [initialData]);

  const validate = () => {
    const errs = {};
    if (!form.day) errs.day = 'Hari wajib dipilih';
    if (!form.start_time) errs.start_time = 'Jam mulai wajib diisi';
    if (!form.end_time) errs.end_time = 'Jam selesai wajib diisi';
    if (form.start_time && form.end_time && form.start_time >= form.end_time) errs.end_time = 'Jam selesai harus setelah jam mulai';
    if (!['1', '2'].includes(form.semester)) errs.semester = 'Semester harus 1 atau 2';
    if (!form.school_class_id) errs.school_class_id = 'Kelas wajib dipilih';
    if (!form.subject_id) errs.subject_id = 'Mata pelajaran wajib dipilih';
    if (!form.teacher_id) errs.teacher_id = 'Guru wajib dipilih';
    setErrors(errs);
    return Object.keys(errs).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validate()) return;
    const payload = {
      day: form.day,
      start_time: form.start_time,
      end_time: form.end_time,
      semester: form.semester,
      school_class: { secure_id: form.school_class_id },
      subject: { secure_id: form.subject_id },
      teacher: { secure_id: form.teacher_id },
    };
    onSubmit(payload);
  };

  const inputClass = (field) =>
    `w-full px-3 py-2.5 rounded-xl bg-white/[0.04] border text-sm text-white placeholder-white/25 focus:outline-none transition-all ${
      errors[field] ? 'border-rose-500/50 focus:border-rose-500/70' : 'border-white/[0.08] focus:border-indigo-500/40'
    }`;

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-xs font-medium text-white/50 mb-1.5">Hari *</label>
          <select value={form.day} onChange={(e) => setForm({ ...form, day: e.target.value })} className={inputClass('day')}>
            {DAY_OPTIONS.map((d) => (
              <option key={d} value={d} className="bg-[#13131d]">{d}</option>
            ))}
          </select>
          {errors.day && <p className="text-xs text-rose-400 mt-1">{errors.day}</p>}
        </div>
        <div>
          <label className="block text-xs font-medium text-white/50 mb-1.5">Semester *</label>
          <select value={form.semester} onChange={(e) => setForm({ ...form, semester: e.target.value })} className={inputClass('semester')}>
            <option value="1" className="bg-[#13131d]">Semester 1</option>
            <option value="2" className="bg-[#13131d]">Semester 2</option>
          </select>
          {errors.semester && <p className="text-xs text-rose-400 mt-1">{errors.semester}</p>}
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-xs font-medium text-white/50 mb-1.5">Jam Mulai *</label>
          <input type="time" value={form.start_time} onChange={(e) => setForm({ ...form, start_time: e.target.value })} className={inputClass('start_time')} />
          {errors.start_time && <p className="text-xs text-rose-400 mt-1">{errors.start_time}</p>}
        </div>
        <div>
          <label className="block text-xs font-medium text-white/50 mb-1.5">Jam Selesai *</label>
          <input type="time" value={form.end_time} onChange={(e) => setForm({ ...form, end_time: e.target.value })} className={inputClass('end_time')} />
          {errors.end_time && <p className="text-xs text-rose-400 mt-1">{errors.end_time}</p>}
        </div>
      </div>

      <div>
        <label className="block text-xs font-medium text-white/50 mb-1.5">Kelas *</label>
        <select value={form.school_class_id} onChange={(e) => setForm({ ...form, school_class_id: e.target.value })} className={inputClass('school_class_id')}>
          <option value="" className="bg-[#13131d]">Pilih kelas</option>
          {classes.map((c) => (
            <option key={c.secure_id} value={c.secure_id} className="bg-[#13131d]">
              {c.class_name} - {c.academic_year}
            </option>
          ))}
        </select>
        {errors.school_class_id && <p className="text-xs text-rose-400 mt-1">{errors.school_class_id}</p>}
      </div>

      <div>
        <label className="block text-xs font-medium text-white/50 mb-1.5">Mata Pelajaran *</label>
        <select value={form.subject_id} onChange={(e) => setForm({ ...form, subject_id: e.target.value })} className={inputClass('subject_id')}>
          <option value="" className="bg-[#13131d]">Pilih mata pelajaran</option>
          {subjects.map((s) => (
            <option key={s.secure_id} value={s.secure_id} className="bg-[#13131d]">{s.name}</option>
          ))}
        </select>
        {errors.subject_id && <p className="text-xs text-rose-400 mt-1">{errors.subject_id}</p>}
      </div>

      <div>
        <label className="block text-xs font-medium text-white/50 mb-1.5">Guru *</label>
        <select value={form.teacher_id} onChange={(e) => setForm({ ...form, teacher_id: e.target.value })} className={inputClass('teacher_id')}>
          <option value="" className="bg-[#13131d]">Pilih guru</option>
          {teachers.map((t) => (
            <option key={t.secure_id} value={t.secure_id} className="bg-[#13131d]">{t.teacher_name}</option>
          ))}
        </select>
        {errors.teacher_id && <p className="text-xs text-rose-400 mt-1">{errors.teacher_id}</p>}
      </div>

      <div className="flex justify-end gap-3 pt-3 border-t border-white/[0.06]">
        <button type="submit" disabled={loading} className="px-5 py-2.5 rounded-xl text-sm font-medium bg-gradient-to-r from-indigo-500 to-violet-500 text-white hover:shadow-lg hover:shadow-indigo-500/25 transition-all disabled:opacity-50">
          {loading ? 'Menyimpan...' : initialData ? 'Perbarui' : 'Simpan'}
        </button>
      </div>
    </form>
  );
}
