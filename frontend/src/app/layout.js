import './globals.css';
import { AuthProvider } from '@/context/AuthProvider';
import { ToastProvider } from '@/context/ToastProvider';

export const metadata = {
  title: 'EduManager - Sistem Manajemen Sekolah',
  description: 'Aplikasi manajemen sekolah lengkap untuk mengelola guru, siswa, kelas, mata pelajaran, jadwal, dan nilai.',
};

export default function RootLayout({ children }) {
  return (
    <html lang="id">
      <body className="antialiased">
        <AuthProvider>
          <ToastProvider>
            {children}
          </ToastProvider>
        </AuthProvider>
      </body>
    </html>
  );
}
