'use client';
import { useState, useEffect, useCallback } from 'react';
import { useToast } from '@/context/ToastProvider';
import { scoreService } from '@/services/scoreService';
import { studentService } from '@/services/studentService';
import { subjectService } from '@/services/subjectService';
import DataTable from '@/components/ui/DataTable';
import Pagination from '@/components/ui/Pagination';
import Modal from '@/components/ui/Modal';
import ConfirmDialog from '@/components/ui/ConfirmDialog';
import FilterPanel, { FilterField, FilterInput, FilterSelect } from '@/components/ui/FilterPanel';
import ScoreForm from '@/components/forms/ScoreForm';
import { getGradeColor } from '@/lib/utils';

export default function ScoresPage() {
  const toast = useToast();
  const [data, setData] = useState([]);
  const [metadata, setMetadata] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [students, setStudents] = useState([]);
  const [subjects, setSubjects] = useState([]);
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [sortBy, setSortBy] = useState('createdAt');
  const [sortDir, setSortDir] = useState('DESC');
  const [searchName, setSearchName] = useState('');
  const [filters, setFilters] = useState({ semester: '', minScore: '', maxScore: '', grade: '', isPassing: '' });
  const [modalOpen, setModalOpen] = useState(false);
  const [editItem, setEditItem] = useState(null);
  const [deleteItem, setDeleteItem] = useState(null);

  const columns = [
    { key: 'student_name', label: 'Siswa' },
    { key: 'subject_name', label: 'Mata Pelajaran' },
    { key: 'score', label: 'Nilai', render: (v) => (
      <span className="font-mono font-semibold text-white">{v}</span>
    )},
    { key: 'grade', label: 'Grade', render: (v) => (
      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-md text-xs font-bold border ${getGradeColor(v)}`}>{v}</span>
    )},
    { key: 'semester', label: 'Semester', render: (v) => (
      <span className="inline-flex items-center px-2 py-0.5 rounded-md text-xs font-medium bg-indigo-500/15 text-indigo-400">Sem {v}</span>
    )},
    { key: 'is_passing', label: 'Status', render: (v) => (
      <span className={`inline-flex items-center gap-1 px-2 py-0.5 rounded-md text-xs font-medium ${v ? 'bg-emerald-500/15 text-emerald-400' : 'bg-rose-500/15 text-rose-400'}`}>
        {v ? '✓ Lulus' : '✕ Tidak Lulus'}
      </span>
    )},
  ];

  const fetchRefs = useCallback(async () => {
    try {
      const [stRes, suRes] = await Promise.allSettled([
        studentService.getPaged({ page: 0, size: 1000, sortBy: 'name', sortDirection: 'ASC' }),
        subjectService.getAll(),
      ]);
      if (stRes.status === 'fulfilled') setStudents(stRes.value.data?.content || []);
      if (suRes.status === 'fulfilled') setSubjects(suRes.value.data?.content || suRes.value.data || []);
    } catch { /* ignore */ }
  }, []);

  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      const params = { page, size: pageSize, sortBy, sortDirection: sortDir };
      if (filters.semester) params.semester = filters.semester;
      if (filters.minScore) params.minScore = filters.minScore;
      if (filters.maxScore) params.maxScore = filters.maxScore;
      if (filters.grade) params.grade = filters.grade;
      if (filters.isPassing !== '') params.isPassing = filters.isPassing;

      const hasFilters = Object.values(filters).some(v => v !== '');
      const res = hasFilters
        ? await scoreService.filter(params)
        : await scoreService.getPaged(params);
      setData(res.data.content || []);
      setMetadata(res.data.metadata || null);
    } catch (err) {
      toast.error(err.details?.[0] || 'Gagal memuat data nilai');
    } finally {
      setLoading(false);
    }
  }, [page, pageSize, sortBy, sortDir, filters, toast]);

  useEffect(() => { fetchRefs(); }, [fetchRefs]);
  useEffect(() => { fetchData(); }, [fetchData]);

  const handleCreate = async (formData) => {
    setSaving(true);
    try {
      await scoreService.create([formData]);
      toast.success('Nilai berhasil ditambahkan');
      setModalOpen(false); fetchData();
    } catch (err) { toast.error(err.details?.join(', ') || 'Gagal menambahkan nilai'); }
    finally { setSaving(false); }
  };

  const handleUpdate = async (formData) => {
    setSaving(true);
    try {
      await scoreService.update(editItem.secure_id, formData);
      toast.success('Nilai berhasil diperbarui');
      setModalOpen(false); setEditItem(null); fetchData();
    } catch (err) { toast.error(err.details?.join(', ') || 'Gagal memperbarui nilai'); }
    finally { setSaving(false); }
  };

  const handleDelete = async () => {
    setDeleting(true);
    try {
      await scoreService.delete(deleteItem.secure_id);
      toast.success('Nilai berhasil dihapus');
      setDeleteItem(null); fetchData();
    } catch (err) { toast.error(err.details?.join(', ') || 'Gagal menghapus nilai'); }
    finally { setDeleting(false); }
  };

  return (
    <div className="space-y-4 animate-fade-in">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
        <div>
          <h1 className="text-xl font-bold text-white">Data Nilai</h1>
          <p className="text-sm text-white/40">Kelola nilai siswa</p>
        </div>
        <button onClick={() => { setEditItem(null); setModalOpen(true); }}
          className="inline-flex items-center gap-2 px-4 py-2.5 rounded-xl text-sm font-medium bg-gradient-to-r from-indigo-500 to-violet-500 text-white hover:shadow-lg hover:shadow-indigo-500/25 hover:scale-[1.02] active:scale-[0.98] transition-all">
          <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}><path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" /></svg>
          Input Nilai
        </button>
      </div>

      <FilterPanel searchValue={searchName} onSearchChange={(v) => setSearchName(v)} searchPlaceholder="Cari nilai..." onApply={() => setPage(0)} onReset={() => { setFilters({ semester: '', minScore: '', maxScore: '', grade: '', isPassing: '' }); setSearchName(''); setPage(0); }}>
        <FilterField label="Semester">
          <FilterSelect value={filters.semester} onChange={(e) => setFilters({ ...filters, semester: e.target.value })} options={[{ value: '', label: 'Semua' }, { value: '1', label: 'Semester 1' }, { value: '2', label: 'Semester 2' }]} />
        </FilterField>
        <FilterField label="Grade">
          <FilterSelect value={filters.grade} onChange={(e) => setFilters({ ...filters, grade: e.target.value })} options={[{ value: '', label: 'Semua' }, { value: 'A', label: 'A' }, { value: 'B', label: 'B' }, { value: 'C', label: 'C' }, { value: 'D', label: 'D' }, { value: 'E', label: 'E' }]} />
        </FilterField>
        <FilterField label="Nilai Min"><FilterInput type="number" value={filters.minScore} onChange={(e) => setFilters({ ...filters, minScore: e.target.value })} placeholder="0" /></FilterField>
        <FilterField label="Nilai Max"><FilterInput type="number" value={filters.maxScore} onChange={(e) => setFilters({ ...filters, maxScore: e.target.value })} placeholder="100" /></FilterField>
        <FilterField label="Status">
          <FilterSelect value={filters.isPassing} onChange={(e) => setFilters({ ...filters, isPassing: e.target.value })} options={[{ value: '', label: 'Semua' }, { value: 'true', label: 'Lulus' }, { value: 'false', label: 'Tidak Lulus' }]} />
        </FilterField>
      </FilterPanel>

      <DataTable columns={columns} data={data} loading={loading} onEdit={(row) => { setEditItem(row); setModalOpen(true); }} onDelete={(row) => setDeleteItem(row)} />
      <Pagination metadata={metadata} onPageChange={setPage} onPageSizeChange={(size) => { setPageSize(size); setPage(0); }} />

      <Modal isOpen={modalOpen} onClose={() => { setModalOpen(false); setEditItem(null); }} title={editItem ? 'Edit Nilai' : 'Input Nilai'}>
        <ScoreForm initialData={editItem} students={students} subjects={subjects} onSubmit={editItem ? handleUpdate : handleCreate} loading={saving} />
      </Modal>

      <ConfirmDialog isOpen={!!deleteItem} onClose={() => setDeleteItem(null)} onConfirm={handleDelete} loading={deleting} message={`Apakah Anda yakin ingin menghapus nilai ini?`} />
    </div>
  );
}
