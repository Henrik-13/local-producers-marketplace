package edu.bbte.localproducersmarketplace.service;

import edu.bbte.localproducersmarketplace.dto.in.OrderCreateDTO;
import edu.bbte.localproducersmarketplace.dto.out.OrderResponseDTO;
import edu.bbte.localproducersmarketplace.model.OrderStatus;
import edu.bbte.localproducersmarketplace.model.User;

import java.util.List;


public interface OrderService {

    OrderResponseDTO createOrder(User customer, OrderCreateDTO dto);

    List<OrderResponseDTO> getOrdersByCustomer(Long customerId);

    OrderResponseDTO updateStatus(Long orderId, OrderStatus status);

    void deleteOrder(Long orderId);

    List<OrderResponseDTO> getAllOrders();

    List<OrderResponseDTO> getOrdersByStatus(OrderStatus status);

    OrderResponseDTO updateOrder(Long orderId, OrderCreateDTO dto);

    OrderResponseDTO updateOrderConcat(Long orderId, OrderCreateDTO dto);

    List<OrderResponseDTO> getOrdersByProducer(Long producerId);
}
