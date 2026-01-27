'use client';

import React, { createContext, useContext, useState, useEffect } from 'react';
import { getAuthToken, isAuthenticated, setAuthToken, removeAuthToken } from '@/lib/auth';
import { apiClient } from '@/lib/api';

interface AuthContextType {
  user: any | null;
  loading: boolean;
  isAuth: boolean;
  isProducer: boolean;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
  refreshUser: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [user, setUser] = useState<any | null>(null);
  const [loading, setLoading] = useState(true);
  const [isAuth, setIsAuth] = useState(false);

  const refreshUser = async () => {
    try {
      if (isAuthenticated()) {
        // Fetch current user info from backend
        const userData = await apiClient.getCurrentUser();
        setUser(userData);
        setIsAuth(true);
      } else {
        setIsAuth(false);
        setUser(null);
      }
    } catch (error) {
      console.error('Error refreshing user:', error);
      setIsAuth(false);
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    refreshUser();
  }, []);

  const login = async (email: string, password: string) => {
    const token = await apiClient.login(email, password);
    setAuthToken(token);
    setIsAuth(true);
    await refreshUser();
  };

  const logout = () => {
    setIsAuth(false);
    setUser(null);
    removeAuthToken();
    if (typeof window !== 'undefined') {
      window.location.href = '/login';
    }
  };

  const isProducer = user?.role === 'PRODUCER';

  return (
    <AuthContext.Provider
      value={{
        user,
        loading,
        isAuth,
        isProducer,
        login,
        logout,
        refreshUser,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

