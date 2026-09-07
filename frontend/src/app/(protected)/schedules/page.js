'use client';
import { useState, useEffect, useCallback } from 'react';
import { useToast } from '@/context/ToastProvider';
import { scheduleService } from '@/services/scheduleService';
import { classService } from '@/services/classService';
import { subjectService } from '@/services/subjectService';
import { teacherService } from '@/services/teacherService';
import DataTable from '@/components/ui/DataTable';
import Pagination from '@/components/ui/Pagination';
import Modal from '@/components/ui/Modal';
import ConfirmDialog from '@/components/ui/ConfirmDialog';
import FilterPanel, { FilterField, FilterSelect, FilterInput } from '@/components/ui/FilterPanel';
import ScheduleForm from '@/components/forms/ScheduleForm';
import { DAY_OPTIONS } from '@/lib/utils';

export default function SchedulesPage() {
  const toast = useToast();
  const [data, setData] = useState([]);
  const [metadata, setMetadata] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [classes, setClasses] = useState([]);
  const [subjects, setSubjects] = useState([]);
  const [teachers, setTeachers] = useState([]);
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [sortBy, setSortBy] = useState('createdAt');
  const [sortDir, setSortDir] = useState('DESC');
  const [searchName, setSearchName] = useState('');
  const [filters, setFilters] = useState({ day: '', semester: '', startTime: '', endTime: '' });
  const [modalOpen, setModalOpen] = useState(false);
  const [editItem, setEditItem] = useState(null);
  const [deleteItem, setDeleteItem] = useState(null);

  const dayColors = {
    Monday: 'bg-blue-500/15 text-blue-400',
    Tuesday: 'bg-emerald-500/15 text-emerald-400',
    Wednesday: 'bg-amber-500/15 text-amber-400',
    Thursday: 'bg-violet-500/15 text-violet-400',
    Friday: 'bg-rose-500/15 text-rose-400',
    Saturday: 'bg-cyan-500/15 text-cyan-400',
    Sunday: 'bg-orange-500/15 text-orange-400',
  };

  const columns = [
    { key: 'day', label: 'Hari', render: (v) => (
      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-md text-xs font-semibold ${dayColors[v] || 'bg-white/10 text-white/50'}`}>{v}</span>
    )},
    { key: 'start_time', label: 'Mulai' },
    { key: 'end_time', label: 'Selesai' },
    { key: 'semester', label: 'Semester', render: (v) => (
      <span className="inline-flex items-center px-2 py-0.5 rounded-md text-xs font-medium bg-indigo-500/15 text-indigo-400">Sem {v}</span>
    )},
    { key: 'school_class', label: 'Kelas', render: (_, row) => row.school_class?.class_name || row.class_name || '-' },
    { key: 'subject', label: 'Mapel', render: (_, row) => row.subject?.name || row.subject_name || '-' },
    { key: 'teacher', label: 'Guru', render: (_, row) => row.teacher?.teacher_name || row.teacher_name || '-' },
  ];

  const fetchRefs = useCallback(async () => {
    try {
      const [cRes, sRes, tRes] = await Promise.allSettled([
        classService.getAll(),
        subjectService.getAll(),
        teacherService.getPaged({ page: 0, size: 1000, sortBy: 'name', sortDirection: 'ASC' }),
      ]);
      if (cRes.status === 'fulfilled') setClasses(cRes.value.data?.content || cRes.value.data || []);
      if (sRes.status === 'fulfilled') setSubjects(sRes.value.data?.content || sRes.value.data || []);
      if (tRes.status === 'fulfilled') setTeachers(tRes.value.data?.content || []);
    } catch { /* ignore */ }
  }, []);

  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      const params = { page, size: pageSize, sortBy, sortDirection: sortDir };
      if (filters.day) params.day = filters.day;
      if (filters.semester) params.semester = filters.semester;
      if (filters.startTime) params.startTime = filters.startTime;
      if (filters.endTime) params.endTime = filters.endTime;

      const hasFilters = Object.values(filters).some(v => v !== '');
      const res = hasFilters
        ? await scheduleService.filter(params)
        : await scheduleService.getPaged(params);
      setData(res.data.content || []);
      setMetadata(res.data.metadata || null);
    } catch (err) {
      toast.error(err.details?.[0] || 'Gagal memuat data jadwal');
    } finally {
      setLoading(false);
    }
  }, [page, pageSize, sortBy, sortDir, filters, toast]);

  useEffect(() => { fetchRefs(); }, [fetchRefs]);
  useEffect(() => { fetchData(); }, [fetchData]);

  const handleCreate = async (formData) => {
    setSaving(true);
    try {
      await scheduleService.create(formData);
      toast.success('Jadwal berhasil ditambahkan');
      setModalOpen(false); fetchData();
    } catch (err) { toast.error(err.details?.join(', ') || 'Gagal menambahkan jadwal'); }
    finally { setSaving(false); }
  };

  const handleUpdate = async (formData) => {
    setSaving(true);
    try {
      await scheduleService.update(editItem.secure_id, formData);
      toast.success('Jadwal berhasil diperbarui');
      setModalOpen(false); setEditItem(null); fetchData();
    } catch (err) { toast.error(err.details?.join(', ') || 'Gagal memperbarui jadwal'); }
    finally { setSaving(false); }
  };

  const handleDelete = async () => {
    setDeleting(true);
    try {
      await scheduleService.delete(deleteItem.secure_id);
      toast.success('Jadwal berhasil dihapus');
      setDeleteItem(null); fetchData();
    } catch (err) { toast.error(err.details?.join(', ') || 'Gagal menghapus jadwal'); }
    finally { setDeleting(false); }
  };

  return (
    <div className="space-y-4 animate-fade-in">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
        <div>
          <h1 className="text-xl font-bold text-white">Jadwal Pelajaran</h1>
          <p className="text-sm text-white/40">Kelola jadwal pelajaran sekolah</p>
        </div>
        <button onClick={() => { setEditItem(null); setModalOpen(true); }}
          className="inline-flex items-center gap-2 px-4 py-2.5 rounded-xl text-sm font-medium bg-gradient-to-r from-indigo-500 to-violet-500 text-white hover:shadow-lg hover:shadow-indigo-500/25 hover:scale-[1.02] active:scale-[0.98] transition-all">
          <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}><path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" /></svg>
          Tambah Jadwal
        </button>
      </div>

      <FilterPanel searchValue={searchName} onSearchChange={(v) => setSearchName(v)} searchPlaceholder="Cari jadwal..." onApply={() => setPage(0)} onReset={() => { setFilters({ day: '', semester: '', startTime: '', endTime: '' }); setSearchName(''); setPage(0); }}>
        <FilterField label="Hari">
          <FilterSelect value={filters.day} onChange={(e) => setFilters({ ...filters, day: e.target.value })} options={[{ value: '', label: 'Semua' }, ...DAY_OPTIONS.map(d => ({ value: d, label: d }))]} />
        </FilterField>
        <FilterField label="Semester">
          <FilterSelect value={filters.semester} onChange={(e) => setFilters({ ...filters, semester: e.target.value })} options={[{ value: '', label: 'Semua' }, { value: '1', label: 'Semester 1' }, { value: '2', label: 'Semester 2' }]} />
        </FilterField>
        <FilterField label="Jam Mulai"><FilterInput type="time" value={filters.startTime} onChange={(e) => setFilters({ ...filters, startTime: e.target.value })} /></FilterField>
        <FilterField label="Jam Selesai"><FilterInput type="time" value={filters.endTime} onChange={(e) => setFilters({ ...filters, endTime: e.target.value })} /></FilterField>
      </FilterPanel>

      <DataTable columns={columns} data={data} loading={loading} onEdit={(row) => { setEditItem(row); setModalOpen(true); }} onDelete={(row) => setDeleteItem(row)} />
      <Pagination metadata={metadata} onPageChange={setPage} onPageSizeChange={(size) => { setPageSize(size); setPage(0); }} />

      <Modal isOpen={modalOpen} onClose={() => { setModalOpen(false); setEditItem(null); }} title={editItem ? 'Edit Jadwal' : 'Tambah Jadwal'} size="lg">
        <ScheduleForm initialData={editItem} classes={classes} subjects={subjects} teachers={teachers} onSubmit={editItem ? handleUpdate : handleCreate} loading={saving} />
      </Modal>

      <ConfirmDialog isOpen={!!deleteItem} onClose={() => setDeleteItem(null)} onConfirm={handleDelete} loading={deleting} message={`Apakah Anda yakin ingin menghapus jadwal ini?`} />
    </div>
  );
}
