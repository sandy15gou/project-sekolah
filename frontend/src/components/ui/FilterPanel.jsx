'use client';
import { useState } from 'react';
import { cn } from '@/lib/utils';

export default function FilterPanel({ children, onApply, onReset, searchPlaceholder, searchValue, onSearchChange }) {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <div className="space-y-3">
      {/* Search bar + toggle */}
      <div className="flex flex-col sm:flex-row gap-3">
        {onSearchChange && (
          <div className="relative flex-1">
            <svg className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-white/30" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
            <input
              type="text"
              value={searchValue || ''}
              onChange={(e) => onSearchChange(e.target.value)}
              placeholder={searchPlaceholder || 'Cari...'}
              className="w-full pl-10 pr-4 py-2.5 rounded-xl bg-white/[0.04] border border-white/[0.08] text-sm text-white placeholder-white/30 focus:outline-none focus:border-indigo-500/40 focus:bg-white/[0.06] transition-all"
            />
          </div>
        )}

        <button
          onClick={() => setIsOpen(!isOpen)}
          className={cn(
            'flex items-center gap-2 px-4 py-2.5 rounded-xl text-sm font-medium border transition-all',
            isOpen
              ? 'bg-indigo-500/10 border-indigo-500/30 text-indigo-400'
              : 'bg-white/[0.04] border-white/[0.08] text-white/50 hover:text-white/70 hover:bg-white/[0.06]'
          )}
        >
          <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
            <path strokeLinecap="round" strokeLinejoin="round" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
          </svg>
          Filter
          <svg className={cn('w-3 h-3 transition-transform', isOpen && 'rotate-180')} fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
            <path strokeLinecap="round" strokeLinejoin="round" d="M19 9l-7 7-7-7" />
          </svg>
        </button>
      </div>

      {/* Collapsible filter panel */}
      <div className={cn(
        'overflow-hidden transition-all duration-300 ease-out',
        isOpen ? 'max-h-[600px] opacity-100' : 'max-h-0 opacity-0'
      )}>
        <div className="p-4 rounded-xl bg-white/[0.02] border border-white/[0.06] space-y-4">
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
            {children}
          </div>

          {/* Actions */}
          <div className="flex items-center justify-end gap-2 pt-3 border-t border-white/[0.06]">
            <button
              onClick={onReset}
              className="px-4 py-2 rounded-lg text-xs font-medium text-white/40 hover:text-white/70 hover:bg-white/[0.04] transition-all"
            >
              Reset
            </button>
            <button
              onClick={onApply}
              className="px-4 py-2 rounded-lg text-xs font-medium bg-indigo-500/20 text-indigo-400 border border-indigo-500/30 hover:bg-indigo-500/30 transition-all"
            >
              Terapkan Filter
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

// Reusable filter input field
export function FilterField({ label, children }) {
  return (
    <div className="space-y-1.5">
      <label className="text-[11px] font-medium text-white/40 uppercase tracking-wider">{label}</label>
      {children}
    </div>
  );
}

// Common filter input styling
export function FilterInput({ ...props }) {
  return (
    <input
      {...props}
      className="w-full px-3 py-2 rounded-lg bg-white/[0.04] border border-white/[0.08] text-sm text-white placeholder-white/25 focus:outline-none focus:border-indigo-500/40 transition-all"
    />
  );
}

export function FilterSelect({ options, ...props }) {
  return (
    <select
      {...props}
      className="w-full px-3 py-2 rounded-lg bg-white/[0.04] border border-white/[0.08] text-sm text-white/70 focus:outline-none focus:border-indigo-500/40 transition-all"
    >
      {options.map((opt) => (
        <option key={opt.value} value={opt.value} className="bg-[#13131d] text-white">
          {opt.label}
        </option>
      ))}
    </select>
  );
}
