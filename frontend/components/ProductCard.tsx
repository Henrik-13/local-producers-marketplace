'use client';

import Link from 'next/link';
import Image from 'next/image';
import { Product } from '@/types';
import { getImageUrl } from '@/lib/images';

interface ProductCardProps {
  product: Product;
}

export default function ProductCard({ product }: ProductCardProps) {
  const imageUrl =
    product.images && product.images.length > 0 && product.images[0]?.name
      ? getImageUrl(product.images[0].name)
      : null;

  return (
    <Link href={`/products/${product.id}`}>
      <div className="bg-white dark:bg-gray-800 rounded-lg shadow-md overflow-hidden hover:shadow-xl transition-shadow cursor-pointer">
        <div className="relative h-48 w-full bg-gray-200 dark:bg-gray-700 overflow-hidden">
          {imageUrl ? (
            <img
              src={imageUrl}
              alt={product.name}
              className="w-full h-full object-cover"
              onError={(e) => {
                console.error('Image load error:', imageUrl);
                const target = e.target as HTMLImageElement;
                target.style.display = 'none';
              }}
            />
          ) : (
            <div className="flex items-center justify-center h-full text-gray-400 dark:text-gray-500">
              No Image
            </div>
          )}
        </div>
        <div className="p-4">
          <h3 className="text-lg font-semibold mb-2 line-clamp-2 text-gray-900 dark:text-white">{product.name}</h3>
          <p className="text-gray-600 dark:text-gray-400 text-sm mb-2 line-clamp-2">
            {product.description}
          </p>
          <div className="flex justify-between items-center">
            <span className="text-2xl font-bold text-primary-600 dark:text-primary-400">
              ${product.price.toFixed(2)}
            </span>
            {product.category && (
              <span className="text-xs bg-gray-100 dark:bg-gray-700 px-2 py-1 rounded text-gray-700 dark:text-gray-300">
                {product.category.name}
              </span>
            )}
          </div>
          {product.quantity !== undefined && (
            <p className="text-xs text-gray-500 dark:text-gray-400 mt-2">
              Stock: {product.quantity}
            </p>
          )}
        </div>
      </div>
    </Link>
  );
}

