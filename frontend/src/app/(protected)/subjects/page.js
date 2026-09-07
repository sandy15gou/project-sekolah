'use client';
import { useState, useEffect, useCallback } from 'react';
import { useToast } from '@/context/ToastProvider';
import { subjectService } from '@/services/subjectService';
import { teacherService } from '@/services/teacherService';
import DataTable from '@/components/ui/DataTable';
import Pagination from '@/components/ui/Pagination';
import Modal from '@/components/ui/Modal';
import ConfirmDialog from '@/components/ui/ConfirmDialog';
import FilterPanel, { FilterField, FilterInput } from '@/components/ui/FilterPanel';
import SubjectForm from '@/components/forms/SubjectForm';

export default function SubjectsPage() {
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
  const [filters, setFilters] = useState({ name: '', description: '' });
  const [modalOpen, setModalOpen] = useState(false);
  const [editItem, setEditItem] = useState(null);
  const [deleteItem, setDeleteItem] = useState(null);

  const columns = [
    { key: 'name', label: 'Nama Mapel' },
    { key: 'description', label: 'Deskripsi', render: (v) => <span className="max-w-[250px] truncate block" title={v}>{v || '-'}</span> },
    { key: 'eligible_teachers', label: 'Guru Pengampu', render: (v) => {
      if (!v || v.length === 0) return <span className="text-white/20">-</span>;
      return (
        <div className="flex flex-wrap gap-1 max-w-[250px]">
          {v.slice(0, 3).map((t, i) => (
            <span key={i} className="inline-flex items-center px-2 py-0.5 rounded-md text-[10px] font-medium bg-violet-500/15 text-violet-400">
              {t.teacher_name || t.name || 'Guru'}
            </span>
          ))}
          {v.length > 3 && <span className="text-[10px] text-white/30">+{v.length - 3} lagi</span>}
        </div>
      );
    }},
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
      const params = { page, size: pageSize, sortBy, sortDirection: sortDir };
      if (searchName.trim()) params.name = searchName;
      if (filters.name) params.name = filters.name;
      if (filters.description) params.description = filters.description;
      const res = await subjectService.filter(params);
      setData(res.data.content || []);
      setMetadata(res.data.metadata || null);
    } catch (err) {
      toast.error(err.details?.[0] || 'Gagal memuat data mata pelajaran');
    } finally {
      setLoading(false);
    }
  }, [page, pageSize, sortBy, sortDir, searchName, filters, toast]);

  useEffect(() => { fetchTeachers(); }, [fetchTeachers]);
  useEffect(() => { fetchData(); }, [fetchData]);

  const handleCreate = async (formData) => {
    setSaving(true);
    try {
      await subjectService.create(formData);
      toast.success('Mata pelajaran berhasil ditambahkan');
      setModalOpen(false); fetchData();
    } catch (err) { toast.error(err.details?.join(', ') || 'Gagal menambahkan mata pelajaran'); }
    finally { setSaving(false); }
  };

  const handleUpdate = async (formData) => {
    setSaving(true);
    try {
      await subjectService.update(editItem.secure_id, formData);
      toast.success('Mata pelajaran berhasil diperbarui');
      setModalOpen(false); setEditItem(null); fetchData();
    } catch (err) { toast.error(err.details?.join(', ') || 'Gagal memperbarui mata pelajaran'); }
    finally { setSaving(false); }
  };

  const handleDelete = async () => {
    setDeleting(true);
    try {
      await subjectService.delete(deleteItem.secure_id);
      toast.success('Mata pelajaran berhasil dihapus');
      setDeleteItem(null); fetchData();
    } catch (err) { toast.error(err.details?.join(', ') || 'Gagal menghapus mata pelajaran'); }
    finally { setDeleting(false); }
  };

  return (
    <div className="space-y-4 animate-fade-in">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
        <div>
          <h1 className="text-xl font-bold text-white">Mata Pelajaran</h1>
          <p className="text-sm text-white/40">Kelola data mata pelajaran</p>
        </div>
        <button onClick={() => { setEditItem(null); setModalOpen(true); }}
          className="inline-flex items-center gap-2 px-4 py-2.5 rounded-xl text-sm font-medium bg-gradient-to-r from-indigo-500 to-violet-500 text-white hover:shadow-lg hover:shadow-indigo-500/25 hover:scale-[1.02] active:scale-[0.98] transition-all">
          <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}><path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" /></svg>
          Tambah Mapel
        </button>
      </div>

      <FilterPanel searchValue={searchName} onSearchChange={(v) => { setSearchName(v); setPage(0); }} searchPlaceholder="Cari mata pelajaran..." onApply={() => setPage(0)} onReset={() => { setFilters({ name: '', description: '' }); setSearchName(''); setPage(0); }}>
        <FilterField label="Deskripsi"><FilterInput type="text" value={filters.description} onChange={(e) => setFilters({ ...filters, description: e.target.value })} placeholder="Filter deskripsi" /></FilterField>
      </FilterPanel>

      <DataTable columns={columns} data={data} loading={loading} onEdit={(row) => { setEditItem(row); setModalOpen(true); }} onDelete={(row) => setDeleteItem(row)} />
      <Pagination metadata={metadata} onPageChange={setPage} onPageSizeChange={(size) => { setPageSize(size); setPage(0); }} />

      <Modal isOpen={modalOpen} onClose={() => { setModalOpen(false); setEditItem(null); }} title={editItem ? 'Edit Mata Pelajaran' : 'Tambah Mata Pelajaran'} size="lg">
        <SubjectForm initialData={editItem} teachers={teachers} onSubmit={editItem ? handleUpdate : handleCreate} loading={saving} />
      </Modal>

      <ConfirmDialog isOpen={!!deleteItem} onClose={() => setDeleteItem(null)} onConfirm={handleDelete} loading={deleting} message={`Apakah Anda yakin ingin menghapus mata pelajaran "${deleteItem?.name}"?`} />
    </div>
  );
}
