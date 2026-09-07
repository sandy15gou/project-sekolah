'use client';
import { useState, useEffect } from 'react';

export default function SubjectForm({ initialData, teachers = [], onSubmit, loading }) {
  const [form, setForm] = useState({
    name: '',
    description: '',
    eligible_teacher_ids: [],
  });
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (initialData) {
      setForm({
        name: initialData.name || '',
        description: initialData.description || '',
        eligible_teacher_ids: initialData.eligible_teacher_ids || (initialData.eligible_teachers?.map(t => t.secure_id) || []),
      });
    }
  }, [initialData]);

  const validate = () => {
    const errs = {};
    if (!form.name.trim()) errs.name = 'Nama mata pelajaran wajib diisi';
    setErrors(errs);
    return Object.keys(errs).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validate()) return;
    onSubmit(form);
  };

  const toggleTeacher = (teacherId) => {
    setForm((prev) => ({
      ...prev,
      eligible_teacher_ids: prev.eligible_teacher_ids.includes(teacherId)
        ? prev.eligible_teacher_ids.filter((id) => id !== teacherId)
        : [...prev.eligible_teacher_ids, teacherId],
    }));
  };

  const inputClass = (field) =>
    `w-full px-3 py-2.5 rounded-xl bg-white/[0.04] border text-sm text-white placeholder-white/25 focus:outline-none transition-all ${
      errors[field] ? 'border-rose-500/50 focus:border-rose-500/70' : 'border-white/[0.08] focus:border-indigo-500/40'
    }`;

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <label className="block text-xs font-medium text-white/50 mb-1.5">Nama Mata Pelajaran *</label>
        <input type="text" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} className={inputClass('name')} placeholder="Contoh: Matematika" />
        {errors.name && <p className="text-xs text-rose-400 mt-1">{errors.name}</p>}
      </div>

      <div>
        <label className="block text-xs font-medium text-white/50 mb-1.5">Deskripsi</label>
        <textarea value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} className={`${inputClass('description')} resize-none`} rows={3} placeholder="Deskripsi mata pelajaran" />
      </div>

      <div>
        <label className="block text-xs font-medium text-white/50 mb-1.5">Guru Pengampu</label>
        <div className="max-h-48 overflow-y-auto rounded-xl border border-white/[0.08] bg-white/[0.02] p-2 space-y-1 scrollbar-thin">
          {teachers.length === 0 ? (
            <p className="text-xs text-white/30 p-2 text-center">Tidak ada guru tersedia</p>
          ) : (
            teachers.map((t) => (
              <label key={t.secure_id} className="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-white/[0.04] cursor-pointer transition-colors">
                <input
                  type="checkbox"
                  checked={form.eligible_teacher_ids.includes(t.secure_id)}
                  onChange={() => toggleTeacher(t.secure_id)}
                  className="w-4 h-4 rounded border-white/20 bg-white/[0.04] text-indigo-500 focus:ring-indigo-500/30"
                />
                <span className="text-sm text-white/70">{t.teacher_name}</span>
                <span className="text-xs text-white/30">({t.teacher_id})</span>
              </label>
            ))
          )}
        </div>
      </div>

      <div className="flex justify-end gap-3 pt-3 border-t border-white/[0.06]">
        <button type="submit" disabled={loading} className="px-5 py-2.5 rounded-xl text-sm font-medium bg-gradient-to-r from-indigo-500 to-violet-500 text-white hover:shadow-lg hover:shadow-indigo-500/25 transition-all disabled:opacity-50">
          {loading ? 'Menyimpan...' : initialData ? 'Perbarui' : 'Simpan'}
        </button>
      </div>
    </form>
  );
}
