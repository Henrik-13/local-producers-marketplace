# Local Producers Marketplace - Frontend

A Next.js frontend application for the Local Producers Marketplace.

## Getting Started

### Prerequisites

- Node.js 18+ and npm/yarn/pnpm
- Backend API running on `http://localhost:8080` (or configure `NEXT_PUBLIC_API_URL`)

### Installation

1. Install dependencies:
```bash
npm install
# or
yarn install
# or
pnpm install
```

2. Create a `.env.local` file in the frontend directory:
```
NEXT_PUBLIC_API_URL=http://localhost:8080
```

3. Run the development server:
```bash
npm run dev
# or
yarn dev
# or
pnpm dev
```

4. Open [http://localhost:3000](http://localhost:3000) in your browser.

## Features

- **Authentication**: Login and registration
- **Products**: Browse, search, and filter products
- **Shopping Cart**: Add, update, and remove items
- **Orders**: View order history and status
- **Categories**: Browse products by category

## Project Structure

```
frontend/
├── app/              # Next.js app directory
│   ├── page.tsx      # Home page
│   ├── login/        # Login page
│   ├── register/     # Registration page
│   ├── products/     # Product pages
│   ├── cart/         # Shopping cart page
│   └── orders/       # Orders page
├── components/       # React components
├── contexts/         # React contexts (Auth, etc.)
├── lib/              # Utilities and API client
├── types/            # TypeScript type definitions
└── public/           # Static assets
```

## API Integration

The frontend communicates with the backend API through the `apiClient` in `lib/api.ts`. Make sure your backend is running and accessible at the configured URL.

## Authentication

Authentication is handled through JWT tokens stored in cookies. The `AuthContext` provides authentication state and methods throughout the application.

## Building for Production

```bash
npm run build
npm start
```

## Technologies Used

- Next.js 14
- React 18
- TypeScript
- Tailwind CSS
- Axios
- js-cookie

