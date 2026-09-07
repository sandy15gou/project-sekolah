// ==========================================
// UTILITY FUNCTIONS
// ==========================================

// --- Epoch Days Conversion ---
export const toEpochDays = (dateString) => {
  if (!dateString) return null;
  return Math.floor(new Date(dateString).getTime() / (1000 * 60 * 60 * 24));
};

export const fromEpochDays = (epochDays) => {
  if (epochDays === null || epochDays === undefined) return '';
  const date = new Date(epochDays * 24 * 60 * 60 * 1000);
  return date.toISOString().split('T')[0]; // YYYY-MM-DD
};

export const formatDate = (epochDays) => {
  if (epochDays === null || epochDays === undefined) return '-';
  const date = new Date(epochDays * 24 * 60 * 60 * 1000);
  return date.toLocaleDateString('id-ID', {
    day: 'numeric',
    month: 'long',
    year: 'numeric',
  });
};

// --- Gender Formatter ---
export const formatGender = (code) => {
  if (code === 'M') return 'Laki-laki';
  if (code === 'F') return 'Perempuan';
  return '-';
};

// --- Error Code Mapping ---
export const ERROR_CODES = {
  1: 'INVALID_DATA',
  2: 'INTERNAL_ERROR',
  3: 'NETWORK_ERROR',
  4: 'OTHER_ERROR',
  5: 'DATA_NOT_FOUND',
};

export const getErrorMessage = (errorCode) => {
  return ERROR_CODES[errorCode] || 'UNKNOWN_ERROR';
};

// --- Grade Color Mapping ---
export const getGradeColor = (grade) => {
  const colors = {
    A: 'bg-emerald-500/20 text-emerald-400 border-emerald-500/30',
    B: 'bg-blue-500/20 text-blue-400 border-blue-500/30',
    C: 'bg-yellow-500/20 text-yellow-400 border-yellow-500/30',
    D: 'bg-orange-500/20 text-orange-400 border-orange-500/30',
    E: 'bg-rose-500/20 text-rose-400 border-rose-500/30',
  };
  return colors[grade] || 'bg-slate-500/20 text-slate-400 border-slate-500/30';
};

// --- Day Options ---
export const DAY_OPTIONS = [
  'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'
];

// --- Semester Options ---
export const SEMESTER_OPTIONS = ['1', '2'];

// --- Gender Options ---
export const GENDER_OPTIONS = [
  { value: 'M', label: 'Laki-laki' },
  { value: 'F', label: 'Perempuan' },
];

// --- Class Name Formatter ---
export const cn = (...classes) => {
  return classes.filter(Boolean).join(' ');
};
