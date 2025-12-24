@echo off
REM `deploy.bat` - temporarily disabled
REM Exit immediately to avoid running commands
exit /B 0

REM The original commands are commented out below for reference:
REM echo 🚀 Memulai deployment aplikasi sekolah...
REM echo 📦 Menghentikan container yang ada...
REM docker-compose down
REM echo 🔨 Building Docker image...
REM docker-compose build --no-cache
REM echo ▶️ Menjalankan aplikasi...
REM docker-compose up -d
REM echo ⏳ Menunggu aplikasi startup...
REM timeout /t 30
REM echo 📊 Status container:
REM docker-compose ps
REM echo 🏥 Mengecek health status...
REM curl -f http://localhost:8090/actuator/health || echo Health check belum ready
REM echo ✅ Deployment selesai!
REM echo 🌐 Aplikasi dapat diakses di: http://localhost:8090
REM echo 📊 Health check: http://localhost:8090/actuator/health
REM echo 📚 API Documentation: http://localhost:8090/swagger-ui.html
REM pause