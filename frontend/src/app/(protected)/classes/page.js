'use client';
import { useState, useEffect, useCallback } from 'react';
import { useToast } from '@/context/ToastProvider';
import { classService } from '@/services/classService';
import { teacherService } from '@/services/teacherService';
import DataTable from '@/components/ui/DataTable';
import Pagination from '@/components/ui/Pagination';
import Modal from '@/components/ui/Modal';
import ConfirmDialog from '@/components/ui/ConfirmDialog';
import FilterPanel, { FilterField, FilterInput } from '@/components/ui/FilterPanel';
import ClassForm from '@/components/forms/ClassForm';

export default function ClassesPage() {
  const toast = useToast();
  const [data, setData] = useState([]);
  const [metadata, setMetadata] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [teachers, setTeachers] = useState([]);
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [sortBy, setSortBy] = useState('createdAt');
  const [sortDir, setSortDir] = useState('DESC');
  const [searchName, setSearchName] = useState('');
  const [filters, setFilters] = useState({ className: '', gradeLevel: '', academicYear: '', minCapacity: '', maxCapacity: '' });
  const [modalOpen, setModalOpen] = useState(false);
  const [editItem, setEditItem] = useState(null);
  const [deleteItem, setDeleteItem] = useState(null);

  const columns = [
    { key: 'class_name', label: 'Nama Kelas' },
    { key: 'grade_level', label: 'Tingkat', render: (v) => (
      <span className="inline-flex items-center px-2.5 py-0.5 rounded-md text-xs font-semibold bg-indigo-500/15 text-indigo-400">{v}</span>
    )},
    { key: 'academic_year', label: 'Tahun Ajaran' },
    { key: 'homeroom_teacher_name', label: 'Wali Kelas', render: (v) => v || '-' },
    { key: 'student_count', label: 'Jumlah Siswa', render: (v) => (
      <span className="inline-flex items-center px-2 py-0.5 rounded-md text-xs font-medium bg-emerald-500/15 text-emerald-400">{v ?? 0}</span>
    )},
  ];

  const fetchTeachers = useCallback(async () => {
    try {
      const res = await teacherService.getPaged({ page: 0, size: 1000, sortBy: 'name', sortDirection: 'ASC' });
      setTeachers(res.data.content || []);
    } catch { /* ignore */ }
  }, []);

  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      let res;
      if (searchName.trim()) {
        res = await classService.searchByName({ className: searchName, page, size: pageSize, sortBy, sortDirection: sortDir });
      } else if (Object.values(filters).some(v => v !== '')) {
        const params = { page, size: pageSize, sortBy, sortDirection: sortDir };
        if (filters.className) params.className = filters.className;
        if (filters.gradeLevel) params.gradeLevel = filters.gradeLevel;
        if (filters.academicYear) params.academicYear = filters.academicYear;
        if (filters.minCapacity) params.minCapacity = filters.minCapacity;
        if (filters.maxCapacity) params.maxCapacity = filters.maxCapacity;
        res = await classService.filter(params);
      } else {
        res = await classService.getPaged({ page, size: pageSize, sortBy, sortDirection: sortDir });
      }
      setData(res.data.content || []);
      setMetadata(res.data.metadata || null);
    } catch (err) {
      toast.error(err.details?.[0] || 'Gagal memuat data kelas');
    } finally {
      setLoading(false);
    }
  }, [page, pageSize, sortBy, sortDir, searchName, filters, toast]);

  useEffect(() => { fetchTeachers(); }, [fetchTeachers]);
  useEffect(() => { fetchData(); }, [fetchData]);

  const handleCreate = async (formData) => {
    setSaving(true);
    try {
      await classService.create([formData]);
      toast.success('Kelas berhasil ditambahkan');
      setModalOpen(false); fetchData();
    } catch (err) { toast.error(err.details?.join(', ') || 'Gagal menambahkan kelas'); }
    finally { setSaving(false); }
  };

  const handleUpdate = async (formData) => {
    setSaving(true);
    try {
      await classService.update(editItem.secure_id, formData);
      toast.success('Data kelas berhasil diperbarui');
      setModalOpen(false); setEditItem(null); fetchData();
    } catch (err) { toast.error(err.details?.join(', ') || 'Gagal memperbarui data kelas'); }
    finally { setSaving(false); }
  };

  const handleDelete = async () => {
    setDeleting(true);
    try {
      await classService.delete(deleteItem.secure_id);
      toast.success('Kelas berhasil dihapus');
      setDeleteItem(null); fetchData();
    } catch (err) { toast.error(err.details?.join(', ') || 'Gagal menghapus kelas'); }
    finally { setDeleting(false); }
  };

  return (
    <div className="space-y-4 animate-fade-in">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
        <div>
          <h1 className="text-xl font-bold text-white">Data Kelas</h1>
          <p className="text-sm text-white/40">Kelola data kelas sekolah</p>
        </div>
        <button onClick={() => { setEditItem(null); setModalOpen(true); }}
          className="inline-flex items-center gap-2 px-4 py-2.5 rounded-xl text-sm font-medium bg-gradient-to-r from-indigo-500 to-violet-500 text-white hover:shadow-lg hover:shadow-indigo-500/25 hover:scale-[1.02] active:scale-[0.98] transition-all">
          <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}><path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" /></svg>
          Tambah Kelas
        </button>
      </div>

      <FilterPanel searchValue={searchName} onSearchChange={(v) => { setSearchName(v); setPage(0); }} searchPlaceholder="Cari nama kelas..." onApply={() => setPage(0)} onReset={() => { setFilters({ className: '', gradeLevel: '', academicYear: '', minCapacity: '', maxCapacity: '' }); setSearchName(''); setPage(0); }}>
        <FilterField label="Tingkat"><FilterInput type="text" value={filters.gradeLevel} onChange={(e) => setFilters({ ...filters, gradeLevel: e.target.value })} placeholder="Contoh: 10" /></FilterField>
        <FilterField label="Tahun Ajaran"><FilterInput type="text" value={filters.academicYear} onChange={(e) => setFilters({ ...filters, academicYear: e.target.value })} placeholder="2024/2025" /></FilterField>
      </FilterPanel>

      <DataTable columns={columns} data={data} loading={loading} onEdit={(row) => { setEditItem(row); setModalOpen(true); }} onDelete={(row) => setDeleteItem(row)} />
      <Pagination metadata={metadata} onPageChange={setPage} onPageSizeChange={(size) => { setPageSize(size); setPage(0); }} />

      <Modal isOpen={modalOpen} onClose={() => { setModalOpen(false); setEditItem(null); }} title={editItem ? 'Edit Kelas' : 'Tambah Kelas'}>
        <ClassForm initialData={editItem} teachers={teachers} onSubmit={editItem ? handleUpdate : handleCreate} loading={saving} />
      </Modal>

      <ConfirmDialog isOpen={!!deleteItem} onClose={() => setDeleteItem(null)} onConfirm={handleDelete} loading={deleting} message={`Apakah Anda yakin ingin menghapus kelas "${deleteItem?.class_name}"?`} />
    </div>
  );
}
