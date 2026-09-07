'use client';
import { useState, useEffect } from 'react';

export default function ScoreForm({ initialData, students = [], subjects = [], onSubmit, loading }) {
  const [form, setForm] = useState({
    student_id: '',
    subject_id: '',
    score: '',
    semester: '1',
  });
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (initialData) {
      setForm({
        student_id: initialData.student_id || '',
        subject_id: initialData.subject_id || '',
        score: initialData.score?.toString() || '',
        semester: initialData.semester || '1',
      });
    }
  }, [initialData]);

  const validate = () => {
    const errs = {};
    if (!form.student_id) errs.student_id = 'Siswa wajib dipilih';
    if (!form.subject_id) errs.subject_id = 'Mata pelajaran wajib dipilih';
    if (!form.score.toString().trim()) {
      errs.score = 'Nilai wajib diisi';
    } else {
      const scoreNum = Number(form.score);
      if (isNaN(scoreNum) || scoreNum < 0 || scoreNum > 100) errs.score = 'Nilai harus antara 0-100';
    }
    if (!['1', '2'].includes(form.semester)) errs.semester = 'Semester harus 1 atau 2';
    setErrors(errs);
    return Object.keys(errs).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validate()) return;
    const payload = {
      student_id: form.student_id,
      subject_id: form.subject_id,
      score: form.score.toString(),
      semester: form.semester,
    };
    if (initialData?.secure_id) {
      payload.score_id = initialData.secure_id;
    }
    onSubmit(payload);
  };

  const inputClass = (field) =>
    `w-full px-3 py-2.5 rounded-xl bg-white/[0.04] border text-sm text-white placeholder-white/25 focus:outline-none transition-all ${
      errors[field] ? 'border-rose-500/50 focus:border-rose-500/70' : 'border-white/[0.08] focus:border-indigo-500/40'
    }`;

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <label className="block text-xs font-medium text-white/50 mb-1.5">Siswa *</label>
        <select value={form.student_id} onChange={(e) => setForm({ ...form, student_id: e.target.value })} className={inputClass('student_id')}>
          <option value="" className="bg-[#13131d]">Pilih siswa</option>
          {students.map((s) => (
            <option key={s.secure_id} value={s.secure_id} className="bg-[#13131d]">
              {s.student_name} ({s.student_id})
            </option>
          ))}
        </select>
        {errors.student_id && <p className="text-xs text-rose-400 mt-1">{errors.student_id}</p>}
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

      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-xs font-medium text-white/50 mb-1.5">Nilai (0-100) *</label>
          <input type="number" min="0" max="100" value={form.score} onChange={(e) => setForm({ ...form, score: e.target.value })} className={inputClass('score')} placeholder="0-100" />
          {errors.score && <p className="text-xs text-rose-400 mt-1">{errors.score}</p>}
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

      <div className="flex justify-end gap-3 pt-3 border-t border-white/[0.06]">
        <button type="submit" disabled={loading} className="px-5 py-2.5 rounded-xl text-sm font-medium bg-gradient-to-r from-indigo-500 to-violet-500 text-white hover:shadow-lg hover:shadow-indigo-500/25 transition-all disabled:opacity-50">
          {loading ? 'Menyimpan...' : initialData ? 'Perbarui' : 'Simpan'}
        </button>
      </div>
    </form>
  );
}
