'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';
import { apiClient } from '@/lib/api';
import { Order, OrderStatus } from '@/types';

export default function OrdersPage() {
  const router = useRouter();
  const { isAuth, isProducer, user } = useAuth();
  const [placedOrders, setPlacedOrders] = useState<Order[]>([]);
  const [todoOrders, setTodoOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [statusFilter, setStatusFilter] = useState<string>('');
  const [cancelingOrderId, setCancelingOrderId] = useState<number | null>(null);

  useEffect(() => {
    if (!isAuth) {
      router.push('/login');
      return;
    }
    loadOrders();
  }, [isAuth, statusFilter]);

  const loadOrders = async () => {
    setLoading(true);
    try {
      // Load orders placed by the current user (as a customer)
      const customerOrders = await apiClient.getCustomerOrders();
      
      // Double-check: filter to ensure orders belong to current user
      // This is a safety check in case backend returns incorrect data
      const verifiedCustomerOrders = user 
        ? customerOrders.filter((order: Order) => order.customerId === user.id)
        : customerOrders;
      
      let filteredCustomerOrders = verifiedCustomerOrders;
      if (statusFilter) {
        filteredCustomerOrders = verifiedCustomerOrders.filter((order: Order) => order.status === statusFilter);
      }
      setPlacedOrders(filteredCustomerOrders);

      // Load orders containing products from the current user (as a producer)
      if (isProducer) {
        const producerOrders = await apiClient.getProducerOrders();
        let filteredProducerOrders = producerOrders;
        if (statusFilter) {
          filteredProducerOrders = producerOrders.filter((order: Order) => order.status === statusFilter);
        }
        setTodoOrders(filteredProducerOrders);
      }
    } catch (error) {
      console.error('Error loading orders:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleStatusUpdate = async (orderId: number, newStatus: OrderStatus) => {
    try {
      await apiClient.updateOrderStatus(orderId, newStatus);
      loadOrders();
    } catch (error: any) {
      alert(error.response?.data?.message || 'Failed to update order status');
    }
  };

  const handleCancelOrder = async (orderId: number) => {
    if (!confirm('Are you sure you want to cancel this order? This action cannot be undone.')) {
      return;
    }

    setCancelingOrderId(orderId);
    try {
      await apiClient.cancelOrder(orderId);
      await loadOrders();
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || error.message || 'Failed to cancel order';
      alert(errorMessage);
    } finally {
      setCancelingOrderId(null);
    }
  };

  const canCancelOrder = (status: OrderStatus): boolean => {
    // Orders can only be cancelled if they are PENDING or CONFIRMED
    return status === OrderStatus.PENDING || status === OrderStatus.CONFIRMED;
  };

  const getStatusColor = (status: OrderStatus) => {
    switch (status) {
      case OrderStatus.PENDING:
        return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200';
      case OrderStatus.CONFIRMED:
        return 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200';
      case OrderStatus.SHIPPED:
        return 'bg-indigo-100 text-indigo-800 dark:bg-indigo-900 dark:text-indigo-200';
      case OrderStatus.DELIVERED:
        return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200';
      case OrderStatus.CANCELED:
        return 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200';
      default:
        return 'bg-gray-100 text-gray-800 dark:bg-gray-800 dark:text-gray-200';
    }
  };

  const getNextStatus = (currentStatus: OrderStatus): OrderStatus | null => {
    switch (currentStatus) {
      case OrderStatus.PENDING:
        return OrderStatus.CONFIRMED;
      case OrderStatus.CONFIRMED:
        return OrderStatus.SHIPPED;
      case OrderStatus.SHIPPED:
        return OrderStatus.DELIVERED;
      default:
        return null;
    }
  };

  const renderOrderCard = (order: Order, showStatusUpdate: boolean = false) => {
    const nextStatus = showStatusUpdate ? getNextStatus(order.status) : null;
    const canCancel = !showStatusUpdate && canCancelOrder(order.status);
    
    return (
      <div
        key={order.id}
        className="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6 hover:shadow-lg transition"
      >
        <div className="flex justify-between items-start mb-4">
          <div>
            <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
              Order #{order.id}
            </h3>
            <p className="text-sm text-gray-500 dark:text-gray-400">
              {showStatusUpdate ? `Customer ID: ${order.customerId}` : `Placed on ${new Date().toLocaleDateString()}`}
            </p>
          </div>
          <div className="flex items-center space-x-4">
            <span
              className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(order.status)}`}
            >
              {order.status}
            </span>
            {showStatusUpdate && nextStatus && (
              <button
                onClick={() => handleStatusUpdate(order.id, nextStatus)}
                className="px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition text-sm"
              >
                Mark as {nextStatus}
              </button>
            )}
            {canCancel && (
              <button
                onClick={() => handleCancelOrder(order.id)}
                disabled={cancelingOrderId === order.id}
                className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition text-sm disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {cancelingOrderId === order.id ? 'Canceling...' : 'Cancel Order'}
              </button>
            )}
          </div>
        </div>
        <div className="border-t dark:border-gray-700 pt-4">
          <h4 className="font-medium mb-2 text-gray-900 dark:text-white">Items:</h4>
          <ul className="space-y-2">
            {order.items.map((item, index) => (
              <li key={index} className="flex justify-between text-sm">
                <span className="text-gray-700 dark:text-gray-300">
                  Product #{item.productId} × {item.quantity}
                </span>
                <span className="text-gray-900 dark:text-white font-medium">
                  ${(item.quantity * item.unitPrice).toFixed(2)}
                </span>
              </li>
            ))}
          </ul>
          <div className="mt-4 flex justify-end border-t dark:border-gray-700 pt-2">
            <p className="text-xl font-bold text-gray-900 dark:text-white">
              Total: ${order.totalPrice.toFixed(2)}
            </p>
          </div>
        </div>
      </div>
    );
  };

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8">
        <p className="text-gray-700 dark:text-gray-300">Loading orders...</p>
      </div>
    );
  }

  const hasAnyOrders = placedOrders.length > 0 || todoOrders.length > 0;

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-900 dark:text-white">My Orders</h1>
        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
          className="px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-md bg-white dark:bg-gray-700 text-gray-900 dark:text-white focus:outline-none focus:ring-primary-500 focus:border-primary-500"
        >
          <option value="">All Statuses</option>
          {Object.values(OrderStatus).map((status) => (
            <option key={status} value={status}>
              {status}
            </option>
          ))}
        </select>
      </div>

      {!hasAnyOrders ? (
        <div className="text-center py-12">
          <p className="text-gray-500 dark:text-gray-400 mb-4">You have no orders yet</p>
          <a
            href="/products"
            className="inline-block px-6 py-3 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition"
          >
            Browse Products
          </a>
        </div>
      ) : (
        <div className="space-y-8">
          {/* Placed Orders Section */}
          <div>
            <h2 className="text-2xl font-semibold mb-4 text-gray-900 dark:text-white">
              Placed Orders
            </h2>
            {placedOrders.length > 0 ? (
              <div className="space-y-4">
                {placedOrders.map((order) => renderOrderCard(order, false))}
              </div>
            ) : (
              <p className="text-gray-500 dark:text-gray-400 py-4">
                No orders placed yet.
              </p>
            )}
          </div>

          {/* To-Do Orders Section (only for producers) */}
          {isProducer && (
            <div>
              <h2 className="text-2xl font-semibold mb-4 text-gray-900 dark:text-white">
                To-Do Orders
              </h2>
              <p className="text-sm text-gray-600 dark:text-gray-400 mb-4">
                Orders containing your products that need your attention
              </p>
              {todoOrders.length > 0 ? (
                <div className="space-y-4">
                  {todoOrders.map((order) => renderOrderCard(order, true))}
                </div>
              ) : (
                <p className="text-gray-500 dark:text-gray-400 py-4">
                  No orders with your products at the moment.
                </p>
              )}
            </div>
          )}
        </div>
      )}
    </div>
  );
}
