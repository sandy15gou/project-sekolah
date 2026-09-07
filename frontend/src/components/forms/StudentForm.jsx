'use client';
import { useState, useEffect } from 'react';
import { fromEpochDays, toEpochDays } from '@/lib/utils';

export default function StudentForm({ initialData, onSubmit, loading }) {
  const [form, setForm] = useState({
    student_name: '',
    student_id: '',
    student_birth_date: '',
    student_gender: 'M',
    student_address: '',
  });
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (initialData) {
      setForm({
        student_name: initialData.student_name || '',
        student_id: initialData.student_id || '',
        student_birth_date: initialData.student_birth_date ? fromEpochDays(initialData.student_birth_date) : '',
        student_gender: initialData.student_gender || 'M',
        student_address: initialData.student_address || '',
      });
    }
  }, [initialData]);

  const validate = () => {
    const errs = {};
    if (!form.student_name.trim()) errs.student_name = 'Nama siswa wajib diisi';
    if (!form.student_id.trim()) errs.student_id = 'NIS wajib diisi';
    if (form.student_id.trim().toLowerCase() === 'tedy') errs.student_id = 'NIS tidak boleh "Tedy"';
    if (!form.student_birth_date) errs.student_birth_date = 'Tanggal lahir wajib diisi';
    if (!form.student_gender) errs.student_gender = 'Jenis kelamin wajib dipilih';
    if (!form.student_address.trim()) errs.student_address = 'Alamat wajib diisi';
    setErrors(errs);
    return Object.keys(errs).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validate()) return;
    const payload = {
      ...form,
      student_birth_date: toEpochDays(form.student_birth_date),
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
        <label className="block text-xs font-medium text-white/50 mb-1.5">Nama Siswa *</label>
        <input type="text" value={form.student_name} onChange={(e) => setForm({ ...form, student_name: e.target.value })} className={inputClass('student_name')} placeholder="Masukkan nama siswa" />
        {errors.student_name && <p className="text-xs text-rose-400 mt-1">{errors.student_name}</p>}
      </div>

      <div>
        <label className="block text-xs font-medium text-white/50 mb-1.5">NIS *</label>
        <input type="text" value={form.student_id} onChange={(e) => setForm({ ...form, student_id: e.target.value })} className={inputClass('student_id')} placeholder="Masukkan NIS" />
        {errors.student_id && <p className="text-xs text-rose-400 mt-1">{errors.student_id}</p>}
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-xs font-medium text-white/50 mb-1.5">Tanggal Lahir *</label>
          <input type="date" value={form.student_birth_date} onChange={(e) => setForm({ ...form, student_birth_date: e.target.value })} className={inputClass('student_birth_date')} />
          {errors.student_birth_date && <p className="text-xs text-rose-400 mt-1">{errors.student_birth_date}</p>}
        </div>
        <div>
          <label className="block text-xs font-medium text-white/50 mb-1.5">Jenis Kelamin *</label>
          <select value={form.student_gender} onChange={(e) => setForm({ ...form, student_gender: e.target.value })} className={inputClass('student_gender')}>
            <option value="M" className="bg-[#13131d]">Laki-laki</option>
            <option value="F" className="bg-[#13131d]">Perempuan</option>
          </select>
          {errors.student_gender && <p className="text-xs text-rose-400 mt-1">{errors.student_gender}</p>}
        </div>
      </div>

      <div>
        <label className="block text-xs font-medium text-white/50 mb-1.5">Alamat *</label>
        <textarea value={form.student_address} onChange={(e) => setForm({ ...form, student_address: e.target.value })} className={`${inputClass('student_address')} resize-none`} rows={3} placeholder="Masukkan alamat" />
        {errors.student_address && <p className="text-xs text-rose-400 mt-1">{errors.student_address}</p>}
      </div>

      <div className="flex justify-end gap-3 pt-3 border-t border-white/[0.06]">
        <button type="submit" disabled={loading} className="px-5 py-2.5 rounded-xl text-sm font-medium bg-gradient-to-r from-indigo-500 to-violet-500 text-white hover:shadow-lg hover:shadow-indigo-500/25 transition-all disabled:opacity-50">
          {loading ? 'Menyimpan...' : initialData ? 'Perbarui' : 'Simpan'}
        </button>
      </div>
    </form>
  );
}
