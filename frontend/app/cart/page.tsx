'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';
import { apiClient } from '@/lib/api';
import Image from 'next/image';
import Link from 'next/link';

export default function CartPage() {
  const router = useRouter();
  const { isAuth, user } = useAuth();
  const [cart, setCart] = useState<any>(null);
  const [products, setProducts] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [updating, setUpdating] = useState(false);

  useEffect(() => {
    if (!isAuth) {
      router.push('/login');
      return;
    }
    if (user) {
      loadCart();
    }
  }, [isAuth, user]);

  const loadCart = async () => {
    if (!user) return;
    try {
      const cartData = await apiClient.getCart(user.id);
      setCart(cartData);

      // Load product details for each cart item
      const productPromises = cartData.items.map((item: any) =>
        apiClient.getProduct(item.productId)
      );
      const productData = await Promise.all(productPromises);
      setProducts(productData);
    } catch (error: any) {
      if (error.response?.status === 404) {
        setCart(null);
      } else {
        console.error('Error loading cart:', error);
      }
    } finally {
      setLoading(false);
    }
  };

  const handleRemoveItem = async (productId: number) => {
    if (!cart || !user) return;
    setUpdating(true);
    try {
      await apiClient.removeItemFromCart(user.id, productId);
      await loadCart();
    } catch (error) {
      console.error('Error removing item:', error);
      alert('Failed to remove item');
    } finally {
      setUpdating(false);
    }
  };

  const handleUpdateQuantity = async (productId: number, newQuantity: number) => {
    if (!cart || !user || newQuantity < 1) return;
    setUpdating(true);
    try {
      const updatedItems = cart.items.map((item: any) =>
        item.productId === productId
          ? { ...item, quantity: newQuantity }
          : item
      );
      await apiClient.updateCart(user.id, { items: updatedItems });
      await loadCart();
    } catch (error) {
      console.error('Error updating quantity:', error);
      alert('Failed to update quantity');
    } finally {
      setUpdating(false);
    }
  };

  const handleClearCart = async () => {
    if (!cart || !user || !confirm('Are you sure you want to clear your cart?')) return;
    setUpdating(true);
    try {
      await apiClient.clearCart(user.id);
      await loadCart();
    } catch (error) {
      console.error('Error clearing cart:', error);
      alert('Failed to clear cart');
    } finally {
      setUpdating(false);
    }
  };

  const handleCheckout = async () => {
    if (!cart || cart.items.length === 0 || !user) return;
    try {
      const orderData = {
        items: cart.items.map((item: any) => ({
          productId: item.productId,
          quantity: item.quantity,
          unitPrice: item.unitPrice,
        })),
      };
      await apiClient.createOrderForCurrentUser(orderData);
      await apiClient.clearCart(user.id);
      router.push('/orders');
    } catch (error: any) {
      alert(error.response?.data?.message || 'Failed to create order');
    }
  };

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8">
        <p>Loading cart...</p>
      </div>
    );
  }

  if (!cart || cart.items.length === 0) {
    return (
      <div className="container mx-auto px-4 py-8">
        <h1 className="text-3xl font-bold mb-8">Shopping Cart</h1>
        <div className="text-center py-12">
          <p className="text-gray-500 mb-4">Your cart is empty</p>
          <Link
            href="/products"
            className="inline-block px-6 py-3 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition"
          >
            Browse Products
          </Link>
        </div>
      </div>
    );
  }

  const total = cart.items.reduce(
    (sum: number, item: any) => sum + item.quantity * item.unitPrice,
    0
  );

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold">Shopping Cart</h1>
        <button
          onClick={handleClearCart}
          disabled={updating}
          className="text-red-600 hover:text-red-700 disabled:opacity-50"
        >
          Clear Cart
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2">
          <div className="bg-white rounded-lg shadow-md overflow-hidden">
            {cart.items.map((item: any, index: number) => {
              const product = products[index];
              return (
                <div
                  key={item.productId}
                  className="border-b last:border-b-0 p-6 flex flex-col md:flex-row gap-4"
                >
                  {product && (
                    <>
                      <div className="relative w-full md:w-32 h-32 bg-gray-200 rounded overflow-hidden flex-shrink-0">
                        {product.images && product.images.length > 0 ? (
                          <Image
                            src={product.images[0].url}
                            alt={product.name}
                            fill
                            className="object-cover"
                            sizes="128px"
                          />
                        ) : (
                          <div className="flex items-center justify-center h-full text-gray-400 text-xs">
                            No Image
                          </div>
                        )}
                      </div>
                      <div className="flex-1">
                        <Link
                          href={`/products/${product.id}`}
                          className="text-lg font-semibold hover:text-primary-600"
                        >
                          {product.name}
                        </Link>
                        <p className="text-gray-600 text-sm mt-1">
                          ${item.unitPrice.toFixed(2)} each
                        </p>
                        <div className="mt-4 flex items-center space-x-4">
                          <label className="text-sm">Quantity:</label>
                          <input
                            type="number"
                            min="1"
                            value={item.quantity}
                            onChange={(e) =>
                              handleUpdateQuantity(
                                item.productId,
                                parseInt(e.target.value) || 1
                              )
                            }
                            disabled={updating}
                            className="w-20 px-2 py-1 border border-gray-300 rounded disabled:opacity-50"
                          />
                          <button
                            onClick={() => handleRemoveItem(item.productId)}
                            disabled={updating}
                            className="text-red-600 hover:text-red-700 text-sm disabled:opacity-50"
                          >
                            Remove
                          </button>
                        </div>
                      </div>
                      <div className="text-right">
                        <p className="text-lg font-semibold">
                          ${(item.quantity * item.unitPrice).toFixed(2)}
                        </p>
                      </div>
                    </>
                  )}
                </div>
              );
            })}
          </div>
        </div>

        <div className="lg:col-span-1">
          <div className="bg-white rounded-lg shadow-md p-6 sticky top-4">
            <h2 className="text-xl font-semibold mb-4">Order Summary</h2>
            <div className="space-y-2 mb-4">
              <div className="flex justify-between">
                <span>Subtotal:</span>
                <span>${total.toFixed(2)}</span>
              </div>
              <div className="flex justify-between font-semibold text-lg pt-4 border-t">
                <span>Total:</span>
                <span>${total.toFixed(2)}</span>
              </div>
            </div>
            <button
              onClick={handleCheckout}
              disabled={updating || cart.items.length === 0}
              className="w-full px-6 py-3 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition disabled:opacity-50 disabled:cursor-not-allowed"
            >
              Proceed to Checkout
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

