# Frontend Setup Guide

## Quick Start

1. **Install Dependencies**
   ```bash
   cd frontend
   npm install
   ```

2. **Configure Environment**
   Create a `.env.local` file:
   ```
   NEXT_PUBLIC_API_URL=http://localhost:8080
   ```

3. **Start Development Server**
   ```bash
   npm run dev
   ```

4. **Open Browser**
   Navigate to http://localhost:3000

## Project Structure

```
frontend/
├── app/                    # Next.js 14 App Router
│   ├── layout.tsx         # Root layout with AuthProvider
│   ├── page.tsx           # Home page
│   ├── login/             # Login page
│   ├── register/         # Registration page
│   ├── products/          # Product pages
│   │   ├── page.tsx      # Product listing with filters
│   │   └── [id]/         # Product detail page
│   ├── cart/              # Shopping cart page
│   ├── orders/            # Orders page
│   └── categories/        # Categories page
├── components/            # Reusable React components
│   ├── Navbar.tsx        # Navigation bar
│   ├── Footer.tsx        # Footer component
│   └── ProductCard.tsx   # Product card component
├── contexts/             # React contexts
│   └── AuthContext.tsx    # Authentication context
├── lib/                  # Utility functions
│   ├── api.ts            # API client (Axios)
│   └── auth.ts           # Auth utilities
├── types/                # TypeScript type definitions
│   └── index.ts          # All type definitions
└── public/               # Static assets
```

## Features Implemented

### ✅ Authentication
- Login page (`/login`)
- Registration page (`/register`)
- JWT token management via cookies
- Auth context for global state

### ✅ Products
- Product listing with pagination (`/products`)
- Product filtering (name, category, price range)
- Product detail page (`/products/[id]`)
- Product cards with images

### ✅ Shopping Cart
- Add products to cart
- Update quantities
- Remove items
- Clear cart
- View cart total

### ✅ Orders
- View order history (`/orders`)
- Filter orders by status
- Order details with items

### ✅ Categories
- Browse all categories (`/categories`)
- Filter products by category

### ✅ UI Components
- Responsive navigation bar
- Footer
- Product cards
- Loading states
- Error handling

## API Integration

The frontend communicates with the backend API through the `apiClient` in `lib/api.ts`. All API calls include:
- Automatic JWT token injection from cookies
- Error handling and 401 redirects
- Type-safe request/response handling

## Authentication Flow

1. User registers/logs in
2. JWT token is stored in cookies
3. Token is automatically included in API requests
4. On 401 errors, user is redirected to login

## Important Notes

### User ID Hardcoding
Currently, some pages use a hardcoded `userId = 1`. You'll need to:
1. Update `AuthContext` to store user information after login
2. Add an endpoint to get current user info from token
3. Replace hardcoded userId with actual user ID from context

### Image Handling
- Product images are loaded from MinIO (configured in backend)
- Missing images show a placeholder
- Image URLs should be absolute URLs from your backend

## Environment Variables

- `NEXT_PUBLIC_API_URL`: Backend API base URL (default: http://localhost:8080)

## Building for Production

```bash
npm run build
npm start
```

## Technologies

- **Next.js 14** - React framework with App Router
- **TypeScript** - Type safety
- **Tailwind CSS** - Styling
- **Axios** - HTTP client
- **js-cookie** - Cookie management

## Next Steps

1. Implement user profile management
2. Add product creation/editing for producers
3. Add order status updates
4. Implement image upload functionality
5. Add search functionality
6. Improve error handling and user feedback
7. Add loading skeletons
8. Implement proper user ID management from auth context

