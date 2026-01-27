package edu.bbte.localproducersmarketplace.service.impl;

import edu.bbte.localproducersmarketplace.dto.in.OrderCreateDTO;
import edu.bbte.localproducersmarketplace.dto.in.OrderItemCreateDTO;
import edu.bbte.localproducersmarketplace.dto.out.OrderResponseDTO;
import edu.bbte.localproducersmarketplace.exception.OrderFinalizedException;
import edu.bbte.localproducersmarketplace.exception.OrderNotFoundException;
import edu.bbte.localproducersmarketplace.exception.ProductNotFoundException;
import edu.bbte.localproducersmarketplace.mapper.OrderMapper;
import edu.bbte.localproducersmarketplace.model.*;
import edu.bbte.localproducersmarketplace.repository.OrderRepository;
import edu.bbte.localproducersmarketplace.repository.ProductRepository;
import edu.bbte.localproducersmarketplace.repository.UserRepository;
import edu.bbte.localproducersmarketplace.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(User customer, OrderCreateDTO dto) {

        // 1. Order létrehozása kézzel
        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);

        double totalPrice = 0;
        List<OrderItem> items = new ArrayList<>();

        // 2. OrderItem-ok feldolgozása
        for (OrderItemCreateDTO itemDTO : dto.getItems()) {

            // Product lekérdezése
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found: " + itemDTO.getProductId()));

            // OrderItem létrehozása kézzel
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(itemDTO.getUnitPrice());

            totalPrice += item.getQuantity() * item.getUnitPrice();
            items.add(item);
        }

        // 3. Összekapcsolás az Order-rel
        order.setTotalPrice(totalPrice);
        order.getItems().addAll(items);

        // 4. Mentés
        Order saved = orderRepository.save(order);

        // 5. DTO visszaadása mapper-rel (csak Order -> ResponseDTO)
        return orderMapper.toResponse(saved);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByCustomer(Long customerId) {
        User user = userRepository.findById(customerId).orElseThrow(() -> new OrderNotFoundException("User not found: " + customerId));

        return orderMapper.toResponseList(orderRepository.findByCustomerId(customerId));
    }

    @Override
    @Transactional
    public OrderResponseDTO updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));
        order.setStatus(status);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        orderRepository.delete(order);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderMapper.toResponseList(orderRepository.findAll());
    }

    @Override
    public List<OrderResponseDTO> getOrdersByStatus(OrderStatus status) {
        return orderMapper.toResponseList(orderRepository.findByStatus(status));
    }

    @Override
    @Transactional
    public OrderResponseDTO updateOrder(Long orderId, OrderCreateDTO dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        order.getItems().clear();
        double totalPrice = 0;
        List<OrderItem> items = new ArrayList<>();

        for (OrderItemCreateDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found: " + itemDTO.getProductId()));
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(itemDTO.getUnitPrice());
            totalPrice += item.getQuantity() * item.getUnitPrice();
            items.add(item);
        }

        order.setTotalPrice(totalPrice);
        order.getItems().addAll(items);

        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponseDTO updateOrderConcat(Long orderId, OrderCreateDTO dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new OrderFinalizedException(
                    "Order with ID " + orderId + " cannot be modified because its status is " + order.getStatus());
        }

        double totalPrice = order.getTotalPrice();
        for (OrderItemCreateDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found: " + itemDTO.getProductId()));

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(itemDTO.getUnitPrice());

            totalPrice += item.getQuantity() * item.getUnitPrice();
            order.getItems().add(item);
        }

        order.setTotalPrice(totalPrice);

        Order saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByProducer(Long producerId) {
        return orderMapper.toResponseList(orderRepository.findByProducerId(producerId));
    }

}
