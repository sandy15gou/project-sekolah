'use client';
import { useState, useEffect } from 'react';

export default function ClassForm({ initialData, teachers = [], onSubmit, loading }) {
  const [form, setForm] = useState({
    class_name: '',
    grade_level: '',
    homeroom_teacher: '',
    academic_year: '',
  });
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (initialData) {
      setForm({
        class_name: initialData.class_name || '',
        grade_level: initialData.grade_level || '',
        homeroom_teacher: initialData.homeroom_teacher_id || initialData.homeroom_teacher || '',
        academic_year: initialData.academic_year || '',
      });
    }
  }, [initialData]);

  const validate = () => {
    const errs = {};
    if (!form.class_name.trim()) errs.class_name = 'Nama kelas wajib diisi';
    if (!form.grade_level.trim()) errs.grade_level = 'Tingkat kelas wajib diisi';
    if (!form.homeroom_teacher) errs.homeroom_teacher = 'Wali kelas wajib dipilih';
    if (!form.academic_year.trim()) errs.academic_year = 'Tahun ajaran wajib diisi';
    setErrors(errs);
    return Object.keys(errs).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validate()) return;
    onSubmit(form);
  };

  const inputClass = (field) =>
    `w-full px-3 py-2.5 rounded-xl bg-white/[0.04] border text-sm text-white placeholder-white/25 focus:outline-none transition-all ${
      errors[field] ? 'border-rose-500/50 focus:border-rose-500/70' : 'border-white/[0.08] focus:border-indigo-500/40'
    }`;

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <label className="block text-xs font-medium text-white/50 mb-1.5">Nama Kelas *</label>
        <input type="text" value={form.class_name} onChange={(e) => setForm({ ...form, class_name: e.target.value })} className={inputClass('class_name')} placeholder="Contoh: X-IPA-1" />
        {errors.class_name && <p className="text-xs text-rose-400 mt-1">{errors.class_name}</p>}
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-xs font-medium text-white/50 mb-1.5">Tingkat Kelas *</label>
          <input type="text" value={form.grade_level} onChange={(e) => setForm({ ...form, grade_level: e.target.value })} className={inputClass('grade_level')} placeholder="Contoh: 10" />
          {errors.grade_level && <p className="text-xs text-rose-400 mt-1">{errors.grade_level}</p>}
        </div>
        <div>
          <label className="block text-xs font-medium text-white/50 mb-1.5">Tahun Ajaran *</label>
          <input type="text" value={form.academic_year} onChange={(e) => setForm({ ...form, academic_year: e.target.value })} className={inputClass('academic_year')} placeholder="Contoh: 2024/2025" />
          {errors.academic_year && <p className="text-xs text-rose-400 mt-1">{errors.academic_year}</p>}
        </div>
      </div>

      <div>
        <label className="block text-xs font-medium text-white/50 mb-1.5">Wali Kelas *</label>
        <select value={form.homeroom_teacher} onChange={(e) => setForm({ ...form, homeroom_teacher: e.target.value })} className={inputClass('homeroom_teacher')}>
          <option value="" className="bg-[#13131d]">Pilih wali kelas</option>
          {teachers.map((t) => (
            <option key={t.secure_id} value={t.secure_id} className="bg-[#13131d]">
              {t.teacher_name} ({t.teacher_id})
            </option>
          ))}
        </select>
        {errors.homeroom_teacher && <p className="text-xs text-rose-400 mt-1">{errors.homeroom_teacher}</p>}
      </div>

      <div className="flex justify-end gap-3 pt-3 border-t border-white/[0.06]">
        <button type="submit" disabled={loading} className="px-5 py-2.5 rounded-xl text-sm font-medium bg-gradient-to-r from-indigo-500 to-violet-500 text-white hover:shadow-lg hover:shadow-indigo-500/25 transition-all disabled:opacity-50">
          {loading ? 'Menyimpan...' : initialData ? 'Perbarui' : 'Simpan'}
        </button>
      </div>
    </form>
  );
}
