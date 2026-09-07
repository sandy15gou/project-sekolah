'use client';
import { useState, useEffect } from 'react';
import { useAuth } from '@/context/AuthProvider';
import { teacherService } from '@/services/teacherService';
import { studentService } from '@/services/studentService';
import { classService } from '@/services/classService';
import { subjectService } from '@/services/subjectService';
import { scheduleService } from '@/services/scheduleService';

const statCards = [
  { key: 'teachers', label: 'Total Guru', icon: TeacherIcon, gradient: 'from-blue-500 to-cyan-500', shadow: 'shadow-blue-500/20' },
  { key: 'students', label: 'Total Siswa', icon: StudentIcon, gradient: 'from-violet-500 to-purple-500', shadow: 'shadow-violet-500/20' },
  { key: 'classes', label: 'Total Kelas', icon: ClassIcon, gradient: 'from-emerald-500 to-teal-500', shadow: 'shadow-emerald-500/20' },
  { key: 'subjects', label: 'Mata Pelajaran', icon: SubjectIcon, gradient: 'from-amber-500 to-orange-500', shadow: 'shadow-amber-500/20' },
  { key: 'schedules', label: 'Total Jadwal', icon: ScheduleIcon, gradient: 'from-rose-500 to-pink-500', shadow: 'shadow-rose-500/20' },
];

export default function DashboardPage() {
  const { user } = useAuth();
  const [stats, setStats] = useState({
    teachers: null,
    students: null,
    classes: null,
    subjects: null,
    schedules: null,
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [teacherRes, studentRes, classRes, subjectRes, scheduleRes] = await Promise.allSettled([
          teacherService.getPaged({ page: 0, size: 1 }),
          studentService.getPaged({ page: 0, size: 1 }),
          classService.getPaged({ page: 0, size: 1 }),
          subjectService.filter({ page: 0, size: 1 }),
          scheduleService.getPaged({ page: 0, size: 1 }),
        ]);

        setStats({
          teachers: teacherRes.status === 'fulfilled' ? teacherRes.value.data?.metadata?.total_elements : 0,
          students: studentRes.status === 'fulfilled' ? studentRes.value.data?.metadata?.total_elements : 0,
          classes: classRes.status === 'fulfilled' ? classRes.value.data?.metadata?.total_elements : 0,
          subjects: subjectRes.status === 'fulfilled' ? subjectRes.value.data?.metadata?.total_elements : 0,
          schedules: scheduleRes.status === 'fulfilled' ? scheduleRes.value.data?.metadata?.total_elements : 0,
        });
      } catch {
        // Ignore
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, []);

  return (
    <div className="space-y-6 animate-fade-in">
      {/* Welcome */}
      <div className="relative overflow-hidden rounded-2xl border border-white/[0.06] bg-gradient-to-br from-indigo-500/[0.08] to-violet-500/[0.05] p-6 lg:p-8">
        <div className="relative z-10">
          <h1 className="text-2xl lg:text-3xl font-bold text-white mb-2">
            Selamat Datang, <span className="text-transparent bg-clip-text bg-gradient-to-r from-indigo-400 to-violet-400">{user?.username || 'User'}</span> 👋
          </h1>
          <p className="text-sm text-white/50 max-w-lg">
            Kelola data guru, siswa, kelas, mata pelajaran, jadwal, dan nilai sekolah Anda dari satu dashboard.
          </p>
        </div>
        <div className="absolute top-0 right-0 w-64 h-64 bg-indigo-500/[0.08] rounded-full blur-[80px] -translate-y-1/2 translate-x-1/3" />
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-4">
        {statCards.map((card, i) => {
          const Icon = card.icon;
          const value = stats[card.key];
          return (
            <div
              key={card.key}
              className="group relative overflow-hidden rounded-xl border border-white/[0.06] bg-white/[0.02] p-5 hover:bg-white/[0.04] transition-all duration-300 animate-slide-up"
              style={{ animationDelay: `${i * 80}ms` }}
            >
              <div className="flex items-start justify-between mb-3">
                <div className={`w-10 h-10 rounded-xl bg-gradient-to-br ${card.gradient} flex items-center justify-center shadow-lg ${card.shadow}`}>
                  <Icon />
                </div>
              </div>
              <div>
                {loading || value === null ? (
                  <div className="h-8 w-16 bg-white/[0.06] rounded-lg animate-pulse mb-1" />
                ) : (
                  <p className="text-2xl font-bold text-white mb-1">{value?.toLocaleString('id-ID') || '0'}</p>
                )}
                <p className="text-xs text-white/40">{card.label}</p>
              </div>
              <div className="absolute inset-0 bg-gradient-to-br from-white/[0.02] to-transparent opacity-0 group-hover:opacity-100 transition-opacity" />
            </div>
          );
        })}
      </div>

      {/* Quick Actions */}
      <div className="rounded-xl border border-white/[0.06] bg-white/[0.02] p-6">
        <h2 className="text-sm font-semibold text-white/70 mb-4">Aksi Cepat</h2>
        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-3">
          {[
            { label: 'Tambah Guru', href: '/teachers', color: 'from-blue-500/10 to-blue-500/5 border-blue-500/20 text-blue-400' },
            { label: 'Tambah Siswa', href: '/students', color: 'from-violet-500/10 to-violet-500/5 border-violet-500/20 text-violet-400' },
            { label: 'Tambah Kelas', href: '/classes', color: 'from-emerald-500/10 to-emerald-500/5 border-emerald-500/20 text-emerald-400' },
            { label: 'Tambah Mapel', href: '/subjects', color: 'from-amber-500/10 to-amber-500/5 border-amber-500/20 text-amber-400' },
            { label: 'Tambah Jadwal', href: '/schedules', color: 'from-rose-500/10 to-rose-500/5 border-rose-500/20 text-rose-400' },
            { label: 'Input Nilai', href: '/scores', color: 'from-cyan-500/10 to-cyan-500/5 border-cyan-500/20 text-cyan-400' },
          ].map((action) => (
            <a
              key={action.label}
              href={action.href}
              className={`flex items-center justify-center py-3 px-4 rounded-xl bg-gradient-to-br border text-xs font-medium hover:scale-[1.02] active:scale-[0.98] transition-all ${action.color}`}
            >
              + {action.label}
            </a>
          ))}
        </div>
      </div>
    </div>
  );
}

function TeacherIcon() {
  return <svg className="w-5 h-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}><path strokeLinecap="round" strokeLinejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" /></svg>;
}
function StudentIcon() {
  return <svg className="w-5 h-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}><path strokeLinecap="round" strokeLinejoin="round" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" /></svg>;
}
function ClassIcon() {
  return <svg className="w-5 h-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}><path strokeLinecap="round" strokeLinejoin="round" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" /></svg>;
}
function SubjectIcon() {
  return <svg className="w-5 h-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}><path strokeLinecap="round" strokeLinejoin="round" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" /></svg>;
}
function ScheduleIcon() {
  return <svg className="w-5 h-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}><path strokeLinecap="round" strokeLinejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>;
}
