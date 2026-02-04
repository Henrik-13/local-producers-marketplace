import axios, { AxiosInstance } from 'axios';
import Cookies from 'js-cookie';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

// Log API URL in development
if (typeof window !== 'undefined' && process.env.NODE_ENV === 'development') {
  console.log('API Base URL:', API_BASE_URL);
}

class ApiClient {
  private client: AxiosInstance;

  constructor() {
    this.client = axios.create({
      baseURL: API_BASE_URL,
      headers: {
        'Content-Type': 'application/json',
      },
      timeout: 10000, // 10 second timeout
    });

    // Add request interceptor to include auth token
    this.client.interceptors.request.use(
      (config) => {
        const token = Cookies.get('token');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    // Add response interceptor to handle errors
    this.client.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 401) {
          // Unauthorized - clear token and redirect to login
          Cookies.remove('token');
          if (typeof window !== 'undefined') {
            window.location.href = '/login';
          }
        }
        return Promise.reject(error);
      }
    );
  }

  // Auth API
  async login(email: string, password: string): Promise<string> {
    const response = await this.client.post('/auth/login', { email, password });
    return response.data;
  }

  async register(data: {
    email: string;
    name: string;
    password: string;
    role: string;
  }): Promise<string> {
    const response = await this.client.post('/auth/register', {
      ...data,
      role: data.role.toUpperCase(),
    });
    return response.data;
  }

  async getCurrentUser(): Promise<any> {
    const response = await this.client.get('/auth/me');
    return response.data;
  }

  // Product API
  async getProducts(params?: {
    name?: string;
    categoryId?: number;
    minPrice?: number;
    maxPrice?: number;
    page?: number;
    size?: number;
  }): Promise<{
    data?: any[];
    content?: any[];
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
  }> {
    const response = await this.client.get('/products', { params });
    return response.data;
  }

  async getProduct(id: number): Promise<any> {
    const response = await this.client.get(`/products/${id}`);
    return response.data;
  }

  async createProduct(data: {
    name: string;
    description: string;
    price: number;
    quantity: number;
    categoryId: number;
  }): Promise<any> {
    const response = await this.client.post('/products', data);
    return response.data;
  }

  async updateProduct(
    id: number,
    data: {
      name: string;
      description: string;
      price: number;
      quantity: number;
      categoryId: number;
    }
  ): Promise<any> {
    const response = await this.client.put(`/products/${id}`, data);
    return response.data;
  }

  async deleteProduct(id: number): Promise<void> {
    await this.client.delete(`/products/${id}`);
  }

  // Category API
  async getCategories(): Promise<any[]> {
    const response = await this.client.get('/categories');
    return response.data;
  }

  async getCategory(id: number): Promise<any> {
    const response = await this.client.get(`/categories/${id}`);
    return response.data;
  }

  async createCategory(data: { name: string }): Promise<any> {
    const response = await this.client.post('/categories', data);
    return response.data;
  }

  async deleteCategory(id: number): Promise<void> {
    await this.client.delete(`/categories/${id}`);
  }

  // Cart API
  async createCart(userId: number, data: { items: any[] }): Promise<any> {
    const response = await this.client.post(`/api/carts/${userId}`, data);
    return response.data;
  }

  async getCart(userId: number): Promise<any> {
    const response = await this.client.get(`/api/carts/user/${userId}`);
    return response.data;
  }

  async addItemsToCart(userId: number, data: { items: any[] }): Promise<any> {
    const response = await this.client.post(`/api/carts/${userId}/items`, data);
    return response.data;
  }

  async updateCart(userId: number, data: { items: any[] }): Promise<any> {
    const response = await this.client.put(`/api/carts/${userId}`, data);
    return response.data;
  }

  async removeItemFromCart(userId: number, productId: number): Promise<any> {
    const response = await this.client.delete(
      `/api/carts/${userId}/items/${productId}`
    );
    return response.data;
  }

  async clearCart(userId: number): Promise<void> {
    await this.client.delete(`/api/carts/${userId}/clear`);
  }

  async deleteCart(userId: number): Promise<void> {
    await this.client.delete(`/api/carts/${userId}`);
  }

  // Order API
  async createOrder(userId: number, data: { items: any[] }): Promise<any> {
    const response = await this.client.post(`/api/orders/${userId}`, data);
    return response.data;
  }

  async createOrderForCurrentUser(data: { items: any[] }): Promise<any> {
    const response = await this.client.post('/api/orders', data);
    return response.data;
  }

  async getOrders(customerId?: number, status?: string): Promise<any[]> {
    const params: any = {};
    if (status) params.status = status;
    const response = await this.client.get(
      customerId ? `/api/orders/customer/${customerId}` : '/api/orders',
      { params }
    );
    return response.data;
  }

  async updateOrderStatus(
    orderId: number,
    status: string
  ): Promise<any> {
    const response = await this.client.patch(`/api/orders/${orderId}/status`, {
      status,
    });
    return response.data;
  }

  async cancelOrder(orderId: number): Promise<any> {
    const response = await this.client.patch(`/api/orders/${orderId}/cancel`);
    return response.data;
  }

  async getProducerOrders(producerId?: number): Promise<any[]> {
    if (producerId) {
      const response = await this.client.get(`/api/orders/producer/${producerId}`);
      return response.data;
    } else {
      const response = await this.client.get('/api/orders/producer/me');
      return response.data;
    }
  }

  async getCustomerOrders(customerId?: number): Promise<any[]> {
    if (customerId) {
      const response = await this.client.get(`/api/orders/customer/${customerId}`);
      return response.data;
    } else {
      const response = await this.client.get('/api/orders/customer/me');
      return response.data;
    }
  }

  async updateOrder(orderId: number, data: { items: any[] }): Promise<any> {
    const response = await this.client.put(`/api/orders/${orderId}`, data);
    return response.data;
  }

  async deleteOrder(orderId: number): Promise<void> {
    await this.client.delete(`/api/orders/${orderId}`);
  }

  // User API
  async getUsers(): Promise<any[]> {
    const response = await this.client.get('/users');
    return response.data;
  }

  async getUser(id: number): Promise<any> {
    const response = await this.client.get(`/users/${id}`);
    return response.data;
  }

  async deleteUser(id: number): Promise<void> {
    await this.client.delete(`/users/${id}`);
  }

  // Image API
  async uploadImage(file: File): Promise<any> {
    const formData = new FormData();
    formData.append('file', file);
    const response = await this.client.post('/images', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  }

  async getImage(filename: string): Promise<Blob> {
    const response = await this.client.get(`/images/${filename}`, {
      responseType: 'blob',
    });
    return response.data;
  }

  async deleteImage(id: number): Promise<void> {
    await this.client.delete(`/images/${id}`);
  }
}

export const apiClient = new ApiClient();

