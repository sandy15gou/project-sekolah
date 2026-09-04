#!/bin/bash
# ============================================
# Setup awal VPS Oracle Cloud (1 GB RAM)
# Jalankan sekali di VPS 1 dan VPS 2
# ============================================

set -e
echo "🚀 Memulai Setup VPS untuk Project Sekolah..."

# 1. Update System
echo "📦 Updating system packages..."
sudo apt update && sudo apt upgrade -y

# 2. Setup 2 GB Swap (Wajib untuk VPS 1 GB RAM)
echo "💾 Setting up 2 GB swap file..."
if [ ! -f /swapfile ]; then
    sudo fallocate -l 2G /swapfile || sudo dd if=/dev/zero of=/swapfile bs=1M count=2048
    sudo chmod 600 /swapfile
    sudo mkswap /swapfile
    sudo swapon /swapfile
    echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
    echo "✅ Swap 2 GB berhasil diaktifkan"
else
    echo "⏭️ Swap file sudah ada"
fi

# 3. Install Docker
echo "🐳 Installing Docker..."
if ! command -v docker &> /dev/null; then
    curl -fsSL https://get.docker.com | sudo sh
    sudo usermod -aG docker $USER
    echo "✅ Docker berhasil diinstall"
else
    echo "⏭️ Docker sudah terinstall"
fi

# 4. Install Docker Compose Plugin
echo "📦 Installing Docker Compose..."
sudo apt install docker-compose-plugin -y

# 5. Open Ports di Firewall Local (iptables)
echo "🔥 Configuring iptables firewall..."
sudo iptables -I INPUT 6 -m state --state NEW -p tcp --dport 80 -j ACCEPT
sudo iptables -I INPUT 6 -m state --state NEW -p tcp --dport 8090 -j ACCEPT
sudo iptables -I INPUT 6 -m state --state NEW -p tcp --dport 5432 -j ACCEPT
sudo apt install iptables-persistent -y
sudo netfilter-persistent save

echo ""
echo "============================================"
echo "✅ Setup VPS Selesai!"
echo "============================================"
free -h
docker --version
docker compose version
echo ""
echo "⚠️ Silakan logout (exit) dan login kembali agar izin user docker aktif!"
