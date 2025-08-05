// Authentication Module
class AuthManager {
    constructor() {
        this.token = localStorage.getItem('authToken');
        this.username = localStorage.getItem('username');
    }

    async login(username, password) {
        try {
            showLoading(true);

            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username: username,
                    password: password
                })
            });

            if (response.ok) {
                const data = await response.json();
                this.token = data.token;
                this.username = username;

                // Store in localStorage
                localStorage.setItem('authToken', this.token);
                localStorage.setItem('username', this.username);

                this.showMainApp();
                return { success: true };
            } else {
                const errorData = await response.json();
                return {
                    success: false,
                    message: errorData.message || 'Login gagal'
                };
            }
        } catch (error) {
            console.error('Login error:', error);
            return {
                success: false,
                message: 'Terjadi kesalahan saat login'
            };
        } finally {
            showLoading(false);
        }
    }

    logout() {
        this.token = null;
        this.username = null;
        localStorage.removeItem('authToken');
        localStorage.removeItem('username');
        this.showLoginPage();
    }

    isAuthenticated() {
        return !!this.token;
    }

    getAuthHeader() {
        return this.token ? { 'Authorization': `Bearer ${this.token}` } : {};
    }

    showLoginPage() {
        document.getElementById('loginPage').style.display = 'block';
        document.getElementById('mainContent').style.display = 'none';
        document.getElementById('mainNav').style.display = 'none';
    }

    showMainApp() {
        document.getElementById('loginPage').style.display = 'none';
        document.getElementById('mainContent').style.display = 'block';
        document.getElementById('mainNav').style.display = 'flex';

        // Show dashboard by default
        showPage('dashboard');
        loadDashboardData();
    }

    init() {
        if (this.isAuthenticated()) {
            this.showMainApp();
        } else {
            this.showLoginPage();
        }
    }
}

// Global auth manager instance
const authManager = new AuthManager();

// Login form handler
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');

    if (loginForm) {
        loginForm.addEventListener('submit', async function(e) {
            e.preventDefault();

            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            const errorDiv = document.getElementById('loginError');

            // Clear previous errors
            errorDiv.style.display = 'none';

            const result = await authManager.login(username, password);

            if (!result.success) {
                errorDiv.textContent = result.message;
                errorDiv.style.display = 'block';
            }
        });
    }

    // Initialize auth manager
    authManager.init();
});

// Logout function
function logout() {
    if (confirm('Apakah Anda yakin ingin logout?')) {
        authManager.logout();
    }
}

// Check token expiration periodically
setInterval(() => {
    if (authManager.isAuthenticated()) {
        // You can add token validation logic here
        // For now, we'll assume the token is valid
    }
}, 5 * 60 * 1000); // Check every 5 minutes
