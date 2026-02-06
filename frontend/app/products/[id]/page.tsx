'use client';

import { useState, useEffect } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { apiClient } from '@/lib/api';
import { useAuth } from '@/contexts/AuthContext';
import { Product } from '@/types';
import { getImageUrl } from '@/lib/images';

export default function ProductDetailPage() {
  const params = useParams();
  const router = useRouter();
  const { isAuth, user, isProducer } = useAuth();
  const [product, setProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState(true);
  const [quantity, setQuantity] = useState(1);
  const [addingToCart, setAddingToCart] = useState(false);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [imageName, setImageName] = useState('');
  const [uploading, setUploading] = useState(false);
  const [uploadError, setUploadError] = useState('');
  const [selectedImageIndex, setSelectedImageIndex] = useState(0);

  useEffect(() => {
    loadProduct();
  }, [params.id]);

  const loadProduct = async () => {
    try {
      const data = await apiClient.getProduct(Number(params.id));
      setProduct(data);
      setSelectedImageIndex(0); // Reset to first image when product loads
      // Debug: log image data
      if (data?.images) {
        console.log('Product images from API:', data.images);
        if (data.images.length > 0 && data.images[0].name) {
          console.log('First image name:', data.images[0].name);
          console.log('Image URL:', getImageUrl(data.images[0].name));
        }
      }
    } catch (error) {
      console.error('Error loading product:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleImageUpload = async () => {
    if (!product || !selectedFile) return;

    setUploadError('');
    setUploading(true);
    try {
      await apiClient.uploadProductImage(
        product.id,
        selectedFile,
        imageName || selectedFile.name
      );
      setSelectedFile(null);
      setImageName('');
      await loadProduct();
    } catch (error: any) {
      const message =
        error?.response?.data?.message || error.message || 'Failed to upload image';
      setUploadError(message);
    } finally {
      setUploading(false);
    }
  };

  const handleAddToCart = async () => {
    if (!isAuth || !user) {
      router.push('/login');
      return;
    }

    setAddingToCart(true);
    try {
      // Add item to cart (backend will auto-create cart if it doesn't exist)
      await apiClient.addItemsToCart(user.id, {
        items: [
          {
            productId: product!.id,
            quantity: quantity,
            unitPrice: product!.price,
          },
        ],
      });
      alert('Product added to cart!');
    } catch (error: any) {
      alert(error.response?.data?.message || 'Failed to add to cart');
    } finally {
      setAddingToCart(false);
    }
  };

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8">
        <p>Loading...</p>
      </div>
    );
  }

  if (!product) {
    return (
      <div className="container mx-auto px-4 py-8">
        <p>Product not found</p>
      </div>
    );
  }

  const images = product?.images || [];
  const hasImages = images.length > 0;
  const currentImage = hasImages && images[selectedImageIndex]?.name
    ? getImageUrl(images[selectedImageIndex].name)
    : null;

  const handlePreviousImage = () => {
    if (hasImages) {
      setSelectedImageIndex((prev) => (prev > 0 ? prev - 1 : images.length - 1));
    }
  };

  const handleNextImage = () => {
    if (hasImages) {
      setSelectedImageIndex((prev) => (prev < images.length - 1 ? prev + 1 : 0));
    }
  };

  const handleThumbnailClick = (index: number) => {
    setSelectedImageIndex(index);
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        <div>
          <div className="relative h-96 w-full bg-gray-200 rounded-lg overflow-hidden group">
            {currentImage ? (
              <>
                <img
                  src={currentImage}
                  alt={product.name}
                  className="w-full h-full object-cover"
                  onError={(e) => {
                    console.error('Image load error for URL:', currentImage);
                    console.error('Product images data:', product.images);
                    const target = e.target as HTMLImageElement;
                    target.style.display = 'none';
                  }}
                  onLoad={() => {
                    console.log('Image loaded successfully:', currentImage);
                  }}
                />
                {hasImages && images.length > 1 && (
                  <>
                    <button
                      onClick={handlePreviousImage}
                      className="absolute left-2 top-1/2 -translate-y-1/2 bg-black/50 hover:bg-black/70 text-white p-2 rounded-full opacity-0 group-hover:opacity-100 transition-opacity"
                      aria-label="Previous image"
                    >
                      <svg
                        className="w-6 h-6"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                      >
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          strokeWidth={2}
                          d="M15 19l-7-7 7-7"
                        />
                      </svg>
                    </button>
                    <button
                      onClick={handleNextImage}
                      className="absolute right-2 top-1/2 -translate-y-1/2 bg-black/50 hover:bg-black/70 text-white p-2 rounded-full opacity-0 group-hover:opacity-100 transition-opacity"
                      aria-label="Next image"
                    >
                      <svg
                        className="w-6 h-6"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                      >
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          strokeWidth={2}
                          d="M9 5l7 7-7 7"
                        />
                      </svg>
                    </button>
                    <div className="absolute bottom-2 left-1/2 -translate-x-1/2 bg-black/50 text-white px-3 py-1 rounded-full text-sm opacity-0 group-hover:opacity-100 transition-opacity">
                      {selectedImageIndex + 1} / {images.length}
                    </div>
                  </>
                )}
              </>
            ) : (
              <div className="flex items-center justify-center h-full text-gray-400">
                <div className="text-center">
                  <p>No Image</p>
                  {hasImages && (
                    <p className="text-xs mt-2">
                      Images array exists but no name found. Check console.
                    </p>
                  )}
                </div>
              </div>
            )}
          </div>
          {hasImages && images.length > 1 && (
            <div className="grid grid-cols-4 gap-2 mt-4">
              {images.map((image, idx) => (
                <button
                  key={idx}
                  onClick={() => handleThumbnailClick(idx)}
                  className={`relative h-20 w-full bg-gray-200 rounded overflow-hidden border-2 transition-all ${
                    selectedImageIndex === idx
                      ? 'border-primary-600 ring-2 ring-primary-300'
                      : 'border-transparent hover:border-gray-400'
                  }`}
                >
                  <img
                    src={getImageUrl(image.name)}
                    alt={`${product.name} ${idx + 1}`}
                    className="w-full h-full object-cover"
                    onError={(e) => {
                      const target = e.target as HTMLImageElement;
                      target.style.display = 'none';
                    }}
                  />
                  {selectedImageIndex === idx && (
                    <div className="absolute inset-0 bg-primary-600/20" />
                  )}
                </button>
              ))}
            </div>
          )}
        </div>

        <div>
          <h1 className="text-3xl font-bold mb-4">{product.name}</h1>
          {product.category && (
            <span className="inline-block bg-primary-100 text-primary-800 px-3 py-1 rounded-full text-sm mb-4">
              {product.category.name}
            </span>
          )}
          <p className="text-4xl font-bold text-primary-600 mb-6">
            ${product.price.toFixed(2)}
          </p>

          <div className="mb-6">
            <h2 className="text-xl font-semibold mb-2">Description</h2>
            <p className="text-gray-700">{product.description}</p>
          </div>

          {product.quantity !== undefined && (
            <div className="mb-6">
              <p className="text-sm text-gray-600">
                Stock: {product.quantity} available
              </p>
            </div>
          )}

          <div className="border-t pt-6">
            <div className="flex items-center space-x-4 mb-6">
              <label htmlFor="quantity" className="text-sm font-medium">
                Quantity:
              </label>
              <input
                id="quantity"
                type="number"
                min="1"
                max={product.quantity || 999}
                value={quantity}
                onChange={(e) => setQuantity(parseInt(e.target.value) || 1)}
                className="w-20 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-primary-500 focus:border-primary-500"
              />
            </div>
            <button
              onClick={handleAddToCart}
              disabled={addingToCart || (product.quantity !== undefined && product.quantity === 0)}
              className="w-full px-6 py-3 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {addingToCart
                ? 'Adding...'
                : product.quantity === 0
                ? 'Out of Stock'
                : 'Add to Cart'}
            </button>
          </div>

          {isProducer &&
            user &&
            product.producer &&
            product.producer.id === user.id && (
              <div className="border-t pt-6 mt-8">
                <h2 className="text-xl font-semibold mb-4">Manage Images</h2>
                {uploadError && (
                  <div className="mb-4 rounded bg-red-100 px-4 py-2 text-sm text-red-700">
                    {uploadError}
                  </div>
                )}
                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium mb-1">
                      Select image
                    </label>
                    <input
                      type="file"
                      accept="image/*"
                      onChange={(e) => {
                        const file = e.target.files?.[0] || null;
                        setSelectedFile(file);
                        if (file && !imageName) {
                          setImageName(file.name);
                        }
                      }}
                      className="block w-full text-sm text-gray-900 file:mr-4 file:rounded-md file:border-0 file:bg-primary-600 file:px-4 file:py-2 file:text-sm file:font-semibold file:text-white hover:file:bg-primary-700"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-1">
                      Image name (optional)
                    </label>
                    <input
                      type="text"
                      value={imageName}
                      onChange={(e) => setImageName(e.target.value)}
                      placeholder="e.g. product-image-1.jpg"
                      className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:border-primary-500 focus:outline-none focus:ring-primary-500"
                    />
                  </div>
                  <button
                    type="button"
                    onClick={handleImageUpload}
                    disabled={uploading || !selectedFile}
                    className="inline-flex items-center rounded-md bg-primary-600 px-4 py-2 text-sm font-medium text-white hover:bg-primary-700 disabled:cursor-not-allowed disabled:opacity-50"
                  >
                    {uploading ? 'Uploading...' : 'Upload Image'}
                  </button>
                </div>
              </div>
            )}
        </div>
      </div>
    </div>
  );
}

