# Frontend Sistem Manajemen Sekolah

Frontend sederhana dan responsif untuk sistem manajemen sekolah yang dibangun dengan HTML, CSS, dan JavaScript vanilla.

## Struktur Folder

```
frontend/
├── index.html          # Halaman utama
├── css/
│   ├── style.css       # Styling utama
│   ├── login.css       # Styling login
│   └── modal.css       # Styling modal
├── js/
│   ├── auth.js         # Authentication management
│   ├── api.js          # API communication
│   ├── app.js          # Main application logic
│   └── modals.js       # Modal management
├── assets/             # Assets (gambar, icon, dll)
└── pages/              # Halaman tambahan (jika diperlukan)
```

## Fitur

### 1. Authentication
- Login dengan username dan password
- JWT token management
- Auto logout saat session expired
- Persistent login state

### 2. Dashboard
- Overview statistik sekolah
- Total kelas, murid, guru, dan mata pelajaran
- Navigasi cepat ke berbagai modul

### 3. Manajemen Kelas
- CRUD operasi untuk kelas
- Detail kelas dengan daftar murid dan jadwal
- Assign wali kelas
- Kapasitas kelas

### 4. Manajemen Murid
- CRUD operasi untuk murid
- Informasi lengkap murid
- Assign ke kelas

### 5. Manajemen Guru
- CRUD operasi untuk guru
- Informasi lengkap guru
- Assign sebagai wali kelas

### 6. Manajemen Mata Pelajaran
- CRUD operasi untuk mata pelajaran
- Assign guru yang bisa mengajar
- Deskripsi mata pelajaran

### 7. Manajemen Jadwal
- CRUD operasi untuk jadwal
- Atur hari, waktu, kelas, mata pelajaran, dan guru
- Semester management

## Teknologi

- **HTML5**: Struktur halaman
- **CSS3**: Styling dengan flexbox/grid, animations, responsive design
- **JavaScript ES6+**: Logic aplikasi, fetch API, async/await
- **No Framework**: Pure vanilla JavaScript untuk performa optimal

## Konfigurasi

### API Configuration
Edit file `js/api.js` untuk mengubah base URL:

```javascript
const API_BASE_URL = 'http://localhost:8080/api';
```

### Authentication
Backend harus menyediakan endpoint:
- `POST /api/auth/login` - Login endpoint

### API Endpoints
Backend harus menyediakan endpoints untuk:
- `/api/classes` - Class management
- `/api/students` - Student management  
- `/api/teachers` - Teacher management
- `/api/subjects` - Subject management
- `/api/schedules` - Schedule management

## Cara Menggunakan

1. **Setup Backend**: Pastikan Spring Boot backend sudah running di localhost:8080

2. **Buka Frontend**: 
   - Buka `index.html` di browser, atau
   - Gunakan live server untuk development

3. **Login**: 
   - Masukkan username dan password
   - Setelah login berhasil, akan redirect ke dashboard

4. **Navigasi**:
   - Gunakan menu navigasi di header untuk berpindah halaman
   - Klik tombol "Tambah" untuk create data baru
   - Klik "Detail" untuk melihat informasi lengkap
   - Klik "Edit" untuk mengubah data
   - Klik "Hapus" untuk menghapus data

## Responsive Design

Frontend ini didesain responsif untuk berbagai ukuran layar:
- **Desktop** (1200px+): Layout penuh dengan sidebar
- **Tablet** (768px-1199px): Layout medium dengan menu collapse
- **Mobile** (320px-767px): Layout mobile-first dengan navigation drawer

## Browser Support

- Chrome 60+
- Firefox 55+
- Safari 11+
- Edge 79+

## Development

### Live Server
Untuk development, gunakan live server:

```bash
# Jika menggunakan VS Code
# Install extension "Live Server"
# Klik kanan pada index.html -> "Open with Live Server"

# Atau gunakan Python
cd frontend
python -m http.server 3000

# Atau gunakan Node.js http-server
npx http-server . -p 3000
```

### Debugging
- Buka Developer Tools (F12)
- Check Console untuk error JavaScript
- Check Network tab untuk API calls
- Check Application tab untuk localStorage

## Customization

### Theme Colors
Edit CSS variables di `css/style.css`:

```css
:root {
    --primary-color: #667eea;
    --secondary-color: #764ba2;
    --success-color: #27ae60;
    --danger-color: #e74c3c;
}
```

### Layout
- Modifikasi grid layout di `.stats-grid`
- Ubah navigation di `.nav`
- Customize modal sizing di `.modal-content`

## Troubleshooting

### Common Issues

1. **CORS Error**
   - Pastikan backend mengizinkan CORS dari frontend domain
   - Add CORS configuration di Spring Boot

2. **API 404 Error**
   - Check API base URL di `js/api.js`
   - Pastikan backend endpoints tersedia

3. **Login Failed**
   - Check username/password
   - Check backend authentication endpoint
   - Check browser network tab untuk response

4. **Data Tidak Muncul**
   - Check browser console untuk JavaScript errors
   - Check API response di network tab
   - Pastikan JWT token valid

### Debug Mode
Uncomment debug logs di `js/api.js` untuk verbose logging:

```javascript
console.log('API Request:', endpoint, config);
console.log('API Response:', response);
```

## Security Notes

- JWT token disimpan di localStorage (consider httpOnly cookies untuk production)
- Semua API calls menggunakan HTTPS di production
- Input validation di frontend dan backend
- Escape HTML content untuk prevent XSS

## Performance

- Lazy loading untuk modal content
- Debounced search inputs
- Pagination untuk large datasets (implementasi future)
- Image optimization untuk assets

## Future Enhancements

1. **Search & Filter**: Add search functionality untuk semua tabel
2. **Pagination**: Implement pagination untuk large datasets  
3. **Export**: Export data ke Excel/PDF
4. **Print**: Print reports dan jadwal
5. **Bulk Operations**: Bulk import/export murid
6. **Real-time**: WebSocket untuk real-time updates
7. **PWA**: Progressive Web App capabilities
8. **Dark Mode**: Theme switcher
9. **Multi-language**: Internationalization support

## Contributing

1. Fork repository
2. Create feature branch
3. Make changes
4. Test thoroughly
5. Submit pull request

## License

MIT License - See LICENSE file for details
