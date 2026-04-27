# 🚀 Quick Start Guide - School Management System

## ⚡ Start in 3 Steps

### Step 1️⃣: Start Backend (Spring Boot)

```bash
cd "E:\SPRING BOOTS\Project sekolah"
mvnw spring-boot:run
```

✅ Backend akan berjalan di: **http://localhost:8080**

---

### Step 2️⃣: Open Frontend

**Option A: Using Live Server (Recommended)**
1. Install "Live Server" extension di VS Code
2. Right-click pada `frontend/login.html`
3. Click "Open with Live Server"

**Option B: Direct Browser**
1. Navigate to `frontend` folder
2. Double-click `login.html`

✅ Frontend akan terbuka di browser

---

### Step 3️⃣: Login & Explore

**Login Credentials:**
```
Username: sandy
Password: sandy
```

✅ Setelah login, Anda akan masuk ke Dashboard

---

## 🎯 Features Available

### ✅ Dashboard
- View total statistics (Classes, Students, Teachers, Subjects)
- Real-time data from backend

### ✅ Classes Management
- Create new class
- View all classes
- Edit class details
- Delete class (soft delete)

### ✅ Students Management
- Add new student
- View student list
- Update student info
- Remove student

### ✅ Teachers Management
- Register new teacher
- View teacher list
- Edit teacher profile
- Remove teacher

### ✅ Subjects Management
- Create subject
- View all subjects
- Edit subject
- Delete subject

### ✅ Schedules Management
- Create class schedule
- View weekly schedules
- Update schedule
- Remove schedule

### ✅ Scores Management
- Input student scores
- View all scores
- Edit scores
- Delete scores

---

## 🔐 API Testing (Optional)

### Using Postman/cURL:

**1. Login to get JWT token:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"sandy","password":"sandy"}'
```

**2. Use token in subsequent requests:**
```bash
curl -X GET http://localhost:8080/api/classes \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

---

## 📱 Mobile/Tablet Access

Frontend is **fully responsive**! Open in:
- ✅ Desktop browser
- ✅ Tablet
- ✅ Mobile phone

---

## 🔧 Troubleshooting

### ❌ Problem: CORS Error
**Solution:** Backend sudah include CORS config, refresh browser

### ❌ Problem: 401 Unauthorized
**Solution:** Login kembali untuk mendapat token baru

### ❌ Problem: Connection Refused
**Solution:** Pastikan backend Spring Boot sudah running

### ❌ Problem: Data tidak muncul
**Solution:** 
1. Check browser console (F12) untuk error
2. Verify backend running di port 8080
3. Check database connection

---

## 📝 Default Data (for Testing)

### Users:
- Username: `sandy`, Password: `sandy`, Role: ADMIN

### Classes:
- X IPA 1, X IPA 2 (jika sudah ada di database)

### Students:
- (Data akan kosong saat pertama kali, silakan tambah manual)

---

## 🎨 UI/UX Features

- ✨ Modern gradient design
- 🌟 Smooth animations
- 📱 Mobile-friendly
- 🔄 Loading states
- ✅ Success notifications
- ❌ Error handling
- 🎭 Modal forms
- 📊 Statistics cards

---

## 🎯 Next Actions

After successful login, you can:

1. **View Dashboard** - See overall statistics
2. **Manage Classes** - Add/Edit classes
3. **Register Students** - Add student data
4. **Add Teachers** - Register teacher info
5. **Create Subjects** - Define subjects
6. **Set Schedules** - Create class schedules
7. **Input Scores** - Enter student grades

---

## 📚 Learn More

- **Backend Guide**: See `dokumen/README.md`
- **Frontend Guide**: See `frontend/README.md`
- **Full Documentation**: See `PROJECT_COMPLETE.md`
- **N+1 Problem**: See `dokumen/N_PLUS_1_PROBLEM_GUIDE.md`

---

## 💡 Tips

1. **Use Chrome DevTools** (F12) to see network requests
2. **Check Console** for any JavaScript errors
3. **Token expires** after some time, just login again
4. **Soft Delete** means data not permanently deleted
5. **Pagination** works automatically for large datasets

---

## ✅ Checklist

Before you start, make sure:
- ✅ Java 17+ installed
- ✅ PostgreSQL running (or configured in application.yml)
- ✅ Maven installed (or use mvnw)
- ✅ Browser with JavaScript enabled
- ✅ Port 8080 available for backend

---

## 🆘 Need Help?

Check these files:
- `PROJECT_COMPLETE.md` - Complete project documentation
- `frontend/SETUP_COMPLETE.md` - Frontend setup guide
- `dokumen/` folder - Technical documentation

---

## 🎉 You're Ready!

**Enjoy your School Management System!** 🏫✨

---

**Quick Links:**
- Backend API: http://localhost:8080/api
- Swagger UI: http://localhost:8080/swagger-ui.html (if enabled)
- H2 Console: http://localhost:8080/h2-console (for testing only)
