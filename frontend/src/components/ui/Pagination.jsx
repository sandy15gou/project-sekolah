'use client';
import { cn } from '@/lib/utils';

export default function Pagination({ metadata, onPageChange, onPageSizeChange }) {
  if (!metadata) return null;

  const { page_number, page_size, total_elements, total_pages, has_next, has_previous } = metadata;
  const from = total_elements === 0 ? 0 : page_number * page_size + 1;
  const to = Math.min((page_number + 1) * page_size, total_elements);

  const getPageNumbers = () => {
    const pages = [];
    const maxVisible = 5;
    let start = Math.max(0, page_number - Math.floor(maxVisible / 2));
    let end = Math.min(total_pages, start + maxVisible);

    if (end - start < maxVisible) {
      start = Math.max(0, end - maxVisible);
    }

    if (start > 0) {
      pages.push(0);
      if (start > 1) pages.push('...');
    }

    for (let i = start; i < end; i++) {
      pages.push(i);
    }

    if (end < total_pages) {
      if (end < total_pages - 1) pages.push('...');
      pages.push(total_pages - 1);
    }

    return pages;
  };

  return (
    <div className="flex flex-col sm:flex-row items-center justify-between gap-4 mt-4">
      {/* Info */}
      <div className="flex items-center gap-3">
        <span className="text-xs text-white/40">
          Menampilkan {from}-{to} dari {total_elements}
        </span>
        {onPageSizeChange && (
          <select
            value={page_size}
            onChange={(e) => onPageSizeChange(Number(e.target.value))}
            className="bg-white/[0.04] border border-white/[0.08] rounded-lg px-2 py-1 text-xs text-white/60 focus:outline-none focus:border-indigo-500/40"
          >
            {[10, 25, 50].map((s) => (
              <option key={s} value={s} className="bg-[#13131d] text-white">
                {s} / halaman
              </option>
            ))}
          </select>
        )}
      </div>

      {/* Page buttons */}
      <div className="flex items-center gap-1">
        <button
          onClick={() => onPageChange(page_number - 1)}
          disabled={!has_previous}
          className={cn(
            'px-3 py-1.5 rounded-lg text-xs font-medium transition-all',
            has_previous
              ? 'text-white/60 hover:text-white hover:bg-white/[0.06]'
              : 'text-white/20 cursor-not-allowed'
          )}
        >
          ← Prev
        </button>

        {getPageNumbers().map((page, i) =>
          page === '...' ? (
            <span key={`ellipsis-${i}`} className="px-2 text-white/20 text-xs">...</span>
          ) : (
            <button
              key={page}
              onClick={() => onPageChange(page)}
              className={cn(
                'w-8 h-8 rounded-lg text-xs font-medium transition-all',
                page === page_number
                  ? 'bg-indigo-500/20 text-indigo-400 border border-indigo-500/30'
                  : 'text-white/40 hover:text-white hover:bg-white/[0.06]'
              )}
            >
              {page + 1}
            </button>
          )
        )}

        <button
          onClick={() => onPageChange(page_number + 1)}
          disabled={!has_next}
          className={cn(
            'px-3 py-1.5 rounded-lg text-xs font-medium transition-all',
            has_next
              ? 'text-white/60 hover:text-white hover:bg-white/[0.06]'
              : 'text-white/20 cursor-not-allowed'
          )}
        >
          Next →
        </button>
      </div>
    </div>
  );
}
