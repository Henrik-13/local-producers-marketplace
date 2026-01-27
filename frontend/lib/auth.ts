import Cookies from 'js-cookie';
import { apiClient } from './api';

export const setAuthToken = (token: string) => {
  Cookies.set('token', token, { expires: 7 }); // 7 days
};

export const getAuthToken = (): string | undefined => {
  return Cookies.get('token');
};

export const removeAuthToken = () => {
  Cookies.remove('token');
};

export const isAuthenticated = (): boolean => {
  return !!getAuthToken();
};

export const login = async (email: string, password: string): Promise<string> => {
  const token = await apiClient.login(email, password);
  setAuthToken(token);
  return token;
};

export const logout = () => {
  removeAuthToken();
  if (typeof window !== 'undefined') {
    window.location.href = '/login';
  }
};

