'use client';
import { useState, useEffect } from 'react';
import { fromEpochDays, toEpochDays } from '@/lib/utils';

export default function TeacherForm({ initialData, onSubmit, loading }) {
  const [form, setForm] = useState({
    teacher_name: '',
    teacher_id: '',
    teacher_birth_date: '',
    teacher_gender: 'M',
    teacher_address: '',
  });
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (initialData) {
      setForm({
        teacher_name: initialData.teacher_name || '',
        teacher_id: initialData.teacher_id || '',
        teacher_birth_date: initialData.teacher_birth_date ? fromEpochDays(initialData.teacher_birth_date) : '',
        teacher_gender: initialData.teacher_gender || 'M',
        teacher_address: initialData.teacher_address || '',
      });
    }
  }, [initialData]);

  const validate = () => {
    const errs = {};
    if (!form.teacher_name.trim()) errs.teacher_name = 'Nama guru wajib diisi';
    if (!form.teacher_id.trim()) errs.teacher_id = 'NIP wajib diisi';
    if (form.teacher_id.trim().toLowerCase() === 'tedy') errs.teacher_id = 'NIP tidak boleh "Tedy"';
    if (!form.teacher_birth_date) errs.teacher_birth_date = 'Tanggal lahir wajib diisi';
    if (!form.teacher_gender) errs.teacher_gender = 'Jenis kelamin wajib dipilih';
    if (!form.teacher_address.trim()) errs.teacher_address = 'Alamat wajib diisi';
    setErrors(errs);
    return Object.keys(errs).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validate()) return;
    const payload = {
      ...form,
      teacher_birth_date: toEpochDays(form.teacher_birth_date),
    };
    onSubmit(payload);
  };

  const inputClass = (field) =>
    `w-full px-3 py-2.5 rounded-xl bg-white/[0.04] border text-sm text-white placeholder-white/25 focus:outline-none transition-all ${
      errors[field] ? 'border-rose-500/50 focus:border-rose-500/70' : 'border-white/[0.08] focus:border-indigo-500/40'
    }`;

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <label className="block text-xs font-medium text-white/50 mb-1.5">Nama Guru *</label>
        <input type="text" value={form.teacher_name} onChange={(e) => setForm({ ...form, teacher_name: e.target.value })} className={inputClass('teacher_name')} placeholder="Masukkan nama guru" />
        {errors.teacher_name && <p className="text-xs text-rose-400 mt-1">{errors.teacher_name}</p>}
      </div>

      <div>
        <label className="block text-xs font-medium text-white/50 mb-1.5">NIP *</label>
        <input type="text" value={form.teacher_id} onChange={(e) => setForm({ ...form, teacher_id: e.target.value })} className={inputClass('teacher_id')} placeholder="Masukkan NIP" />
        {errors.teacher_id && <p className="text-xs text-rose-400 mt-1">{errors.teacher_id}</p>}
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-xs font-medium text-white/50 mb-1.5">Tanggal Lahir *</label>
          <input type="date" value={form.teacher_birth_date} onChange={(e) => setForm({ ...form, teacher_birth_date: e.target.value })} className={inputClass('teacher_birth_date')} />
          {errors.teacher_birth_date && <p className="text-xs text-rose-400 mt-1">{errors.teacher_birth_date}</p>}
        </div>
        <div>
          <label className="block text-xs font-medium text-white/50 mb-1.5">Jenis Kelamin *</label>
          <select value={form.teacher_gender} onChange={(e) => setForm({ ...form, teacher_gender: e.target.value })} className={inputClass('teacher_gender')}>
            <option value="M" className="bg-[#13131d]">Laki-laki</option>
            <option value="F" className="bg-[#13131d]">Perempuan</option>
          </select>
          {errors.teacher_gender && <p className="text-xs text-rose-400 mt-1">{errors.teacher_gender}</p>}
        </div>
      </div>

      <div>
        <label className="block text-xs font-medium text-white/50 mb-1.5">Alamat *</label>
        <textarea value={form.teacher_address} onChange={(e) => setForm({ ...form, teacher_address: e.target.value })} className={`${inputClass('teacher_address')} resize-none`} rows={3} placeholder="Masukkan alamat" />
        {errors.teacher_address && <p className="text-xs text-rose-400 mt-1">{errors.teacher_address}</p>}
      </div>

      <div className="flex justify-end gap-3 pt-3 border-t border-white/[0.06]">
        <button type="submit" disabled={loading} className="px-5 py-2.5 rounded-xl text-sm font-medium bg-gradient-to-r from-indigo-500 to-violet-500 text-white hover:shadow-lg hover:shadow-indigo-500/25 transition-all disabled:opacity-50">
          {loading ? 'Menyimpan...' : initialData ? 'Perbarui' : 'Simpan'}
        </button>
      </div>
    </form>
  );
}
