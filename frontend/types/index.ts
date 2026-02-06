// Auth Types
export interface LoginDto {
  email: string;
  password: string;
}

export interface RegisterDto {
  email: string;
  name: string;
  password: string;
  role: string;
}

export interface User {
  id: number;
  email: string;
  name: string;
  role: 'CUSTOMER' | 'PRODUCER' | 'ADMIN';
}

// Product Types
export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  quantity: number;
  category: Category;
  producer?: User;
  images: Image[];
}

export interface ProductInDto {
  name: string;
  description: string;
  price: number;
  quantity: number;
  categoryId: number;
}

// Category Types
export interface Category {
  id: number;
  name: string;
}

export interface CategoryInDto {
  name: string;
}

// Cart Types
export interface CartItem {
  productId: number;
  quantity: number;
  unitPrice: number;
}

export interface CartItemResponse {
  productId: number;
  quantity: number;
  unitPrice: number;
}

export interface Cart {
  id: number;
  userId: number;
  items: CartItemResponse[];
}

export interface CartCreateDto {
  items: CartItem[];
}

// Order Types
export enum OrderStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  SHIPPED = 'SHIPPED',
  DELIVERED = 'DELIVERED',
  CANCELED = 'CANCELED'
}

export interface OrderItem {
  productId: number;
  quantity: number;
  unitPrice: number;
}

export interface OrderItemResponse {
  productId: number;
  quantity: number;
  unitPrice: number;
}

export interface Order {
  id: number;
  customerId: number;
  totalPrice: number;
  status: OrderStatus;
  items: OrderItemResponse[];
}

export interface OrderCreateDto {
  items: OrderItem[];
}

export interface StatusUpdateDto {
  status: OrderStatus;
}

// Image Types
export interface Image {
  id: number;
  name: string;
}

export interface ImageOutDto {
  id: number;
  name: string;
}

// Page Response Types
export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

