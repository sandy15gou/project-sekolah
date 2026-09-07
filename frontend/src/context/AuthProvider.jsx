'use client';
import { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { getToken, setToken, clearToken, getUser, isAuthenticated as checkAuth } from '@/lib/auth';
import api from '@/lib/axios';
import { useRouter } from 'next/navigation';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  const loadUser = useCallback(() => {
    if (checkAuth()) {
      setUser(getUser());
    } else {
      setUser(null);
      clearToken();
    }
    setLoading(false);
  }, []);

  useEffect(() => {
    loadUser();
  }, [loadUser]);

  const login = async (username, password) => {
    const response = await api.post('/v1/login', { username, password });
    const { token } = response.data;
    if (!token) {
      throw new Error('Token tidak ditemukan dalam response');
    }
    setToken(token);
    const userData = getUser();
    setUser(userData);
    return userData;
  };

  const logout = useCallback(() => {
    clearToken();
    setUser(null);
    router.push('/login');
  }, [router]);

  const value = {
    user,
    loading,
    login,
    logout,
    isAuthenticated: !!user,
    isAdmin: user?.isAdmin || false,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider');
  return context;
};
