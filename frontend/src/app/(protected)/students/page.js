'use client';
import { useState, useEffect, useCallback } from 'react';
import { useToast } from '@/context/ToastProvider';
import { studentService } from '@/services/studentService';
import DataTable from '@/components/ui/DataTable';
import Pagination from '@/components/ui/Pagination';
import Modal from '@/components/ui/Modal';
import ConfirmDialog from '@/components/ui/ConfirmDialog';
import FilterPanel, { FilterField, FilterInput, FilterSelect } from '@/components/ui/FilterPanel';
import StudentForm from '@/components/forms/StudentForm';
import { formatDate, formatGender, GENDER_OPTIONS } from '@/lib/utils';

export default function StudentsPage() {
  const toast = useToast();
  const [data, setData] = useState([]);
  const [metadata, setMetadata] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [sortBy, setSortBy] = useState('createdAt');
  const [sortDir, setSortDir] = useState('DESC');
  const [searchName, setSearchName] = useState('');
  const [filters, setFilters] = useState({ name: '', gender: '', address: '', birthDateFrom: '', birthDateTo: '', minAge: '', maxAge: '' });
  const [modalOpen, setModalOpen] = useState(false);
  const [editItem, setEditItem] = useState(null);
  const [deleteItem, setDeleteItem] = useState(null);

  const columns = [
    { key: 'student_id', label: 'NIS' },
    { key: 'student_name', label: 'Nama' },
    { key: 'student_birth_date', label: 'Tanggal Lahir', render: (v) => formatDate(v) },
    { key: 'student_gender', label: 'JK', render: (v) => (
      <span className={`inline-flex items-center px-2 py-0.5 rounded-md text-xs font-medium ${v === 'M' ? 'bg-blue-500/15 text-blue-400' : 'bg-pink-500/15 text-pink-400'}`}>{formatGender(v)}</span>
    )},
    { key: 'class_name', label: 'Kelas', render: (v) => v || <span className="text-white/20">-</span> },
    { key: 'student_address', label: 'Alamat', render: (v) => <span className="max-w-[180px] truncate block" title={v}>{v || '-'}</span> },
  ];

  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      let res;
      if (searchName.trim()) {
        res = await studentService.search({ name: searchName, page, size: pageSize, sortBy, sortDirection: sortDir });
      } else if (Object.values(filters).some(v => v !== '')) {
        const params = { page, size: pageSize, sortBy, sortDirection: sortDir };
        if (filters.name) params.name = filters.name;
        if (filters.gender) params.gender = filters.gender;
        if (filters.address) params.address = filters.address;
        if (filters.birthDateFrom) params.birthDateFrom = filters.birthDateFrom;
        if (filters.birthDateTo) params.birthDateTo = filters.birthDateTo;
        if (filters.minAge) params.minAge = filters.minAge;
        if (filters.maxAge) params.maxAge = filters.maxAge;
        res = await studentService.filter(params);
      } else {
        res = await studentService.getPaged({ page, size: pageSize, sortBy, sortDirection: sortDir });
      }
      setData(res.data.content || []);
      setMetadata(res.data.metadata || null);
    } catch (err) {
      toast.error(err.details?.[0] || 'Gagal memuat data siswa');
    } finally {
      setLoading(false);
    }
  }, [page, pageSize, sortBy, sortDir, searchName, filters, toast]);

  useEffect(() => { fetchData(); }, [fetchData]);

  const handleCreate = async (formData) => {
    setSaving(true);
    try {
      await studentService.create([formData]);
      toast.success('Siswa berhasil ditambahkan');
      setModalOpen(false);
      fetchData();
    } catch (err) { toast.error(err.details?.join(', ') || 'Gagal menambahkan siswa'); }
    finally { setSaving(false); }
  };

  const handleUpdate = async (formData) => {
    setSaving(true);
    try {
      await studentService.update(editItem.secure_id, formData);
      toast.success('Data siswa berhasil diperbarui');
      setModalOpen(false); setEditItem(null);
      fetchData();
    } catch (err) { toast.error(err.details?.join(', ') || 'Gagal memperbarui data siswa'); }
    finally { setSaving(false); }
  };

  const handleDelete = async () => {
    setDeleting(true);
    try {
      await studentService.delete(deleteItem.secure_id);
      toast.success('Siswa berhasil dihapus');
      setDeleteItem(null);
      fetchData();
    } catch (err) { toast.error(err.details?.join(', ') || 'Gagal menghapus siswa'); }
    finally { setDeleting(false); }
  };

  return (
    <div className="space-y-4 animate-fade-in">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
        <div>
          <h1 className="text-xl font-bold text-white">Data Siswa</h1>
          <p className="text-sm text-white/40">Kelola data siswa sekolah</p>
        </div>
        <button onClick={() => { setEditItem(null); setModalOpen(true); }}
          className="inline-flex items-center gap-2 px-4 py-2.5 rounded-xl text-sm font-medium bg-gradient-to-r from-indigo-500 to-violet-500 text-white hover:shadow-lg hover:shadow-indigo-500/25 hover:scale-[1.02] active:scale-[0.98] transition-all">
          <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}><path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" /></svg>
          Tambah Siswa
        </button>
      </div>

      <FilterPanel searchValue={searchName} onSearchChange={(v) => { setSearchName(v); setPage(0); }} searchPlaceholder="Cari nama siswa..." onApply={() => setPage(0)} onReset={() => { setFilters({ name: '', gender: '', address: '', birthDateFrom: '', birthDateTo: '', minAge: '', maxAge: '' }); setSearchName(''); setPage(0); }}>
        <FilterField label="Jenis Kelamin"><FilterSelect value={filters.gender} onChange={(e) => setFilters({ ...filters, gender: e.target.value })} options={[{ value: '', label: 'Semua' }, ...GENDER_OPTIONS]} /></FilterField>
        <FilterField label="Alamat"><FilterInput type="text" value={filters.address} onChange={(e) => setFilters({ ...filters, address: e.target.value })} placeholder="Filter alamat" /></FilterField>
        <FilterField label="Tgl Lahir Dari"><FilterInput type="date" value={filters.birthDateFrom} onChange={(e) => setFilters({ ...filters, birthDateFrom: e.target.value })} /></FilterField>
        <FilterField label="Tgl Lahir Sampai"><FilterInput type="date" value={filters.birthDateTo} onChange={(e) => setFilters({ ...filters, birthDateTo: e.target.value })} /></FilterField>
      </FilterPanel>

      <DataTable columns={columns} data={data} loading={loading} onEdit={(row) => { setEditItem(row); setModalOpen(true); }} onDelete={(row) => setDeleteItem(row)} />
      <Pagination metadata={metadata} onPageChange={setPage} onPageSizeChange={(size) => { setPageSize(size); setPage(0); }} />

      <Modal isOpen={modalOpen} onClose={() => { setModalOpen(false); setEditItem(null); }} title={editItem ? 'Edit Siswa' : 'Tambah Siswa'}>
        <StudentForm initialData={editItem} onSubmit={editItem ? handleUpdate : handleCreate} loading={saving} />
      </Modal>

      <ConfirmDialog isOpen={!!deleteItem} onClose={() => setDeleteItem(null)} onConfirm={handleDelete} loading={deleting} message={`Apakah Anda yakin ingin menghapus siswa "${deleteItem?.student_name}"?`} />
    </div>
  );
}
