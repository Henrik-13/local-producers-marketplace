package edu.bbte.localproducersmarketplace.controller;

import edu.bbte.localproducersmarketplace.dto.in.OrderCreateDTO;
import edu.bbte.localproducersmarketplace.dto.in.StatusUpdateDTO;
import edu.bbte.localproducersmarketplace.dto.out.OrderResponseDTO;
import edu.bbte.localproducersmarketplace.exception.OrderNotFoundException;
import edu.bbte.localproducersmarketplace.exception.UserNotFoundException;
import edu.bbte.localproducersmarketplace.model.Order;
import edu.bbte.localproducersmarketplace.model.OrderStatus;
import edu.bbte.localproducersmarketplace.model.User;
import edu.bbte.localproducersmarketplace.repository.OrderRepository;
import edu.bbte.localproducersmarketplace.repository.UserRepository;
import edu.bbte.localproducersmarketplace.security.CustomUserDetailsService;
import edu.bbte.localproducersmarketplace.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/{userId}")
    public OrderResponseDTO create(@PathVariable Long userId,
                                   @RequestBody @Valid OrderCreateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        return orderService.createOrder(user, dto);
    }

    @PostMapping
    public OrderResponseDTO createOrder(@RequestBody @Valid OrderCreateDTO dto) {
        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User customer = userDetailsService.loadUserByEmail(email);
        return orderService.createOrder(customer, dto);
    }

    @GetMapping("/customer/{customerId}")
    public List<OrderResponseDTO> getOrders(@PathVariable Long customerId) {
        return orderService.getOrdersByCustomer(customerId);
    }

    @GetMapping("/customer/me")
    public List<OrderResponseDTO> getMyCustomerOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User customer = userDetailsService.loadUserByEmail(email);
        
        // Explicitly verify we're getting orders for the authenticated user
        List<OrderResponseDTO> orders = orderService.getOrdersByCustomer(customer.getId());
        
        // Double-check: filter to ensure all orders belong to this customer
        // This is a safety check in case of any data inconsistency
        return orders.stream()
                .filter(order -> order.getCustomerId().equals(customer.getId()))
                .toList();
    }

    @GetMapping("/producer/{producerId}")
    public List<OrderResponseDTO> getOrdersByProducer(@PathVariable Long producerId) {
        return orderService.getOrdersByProducer(producerId);
    }

    @GetMapping("/producer/me")
    public List<OrderResponseDTO> getMyProducerOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User producer = userDetailsService.loadUserByEmail(email);
        return orderService.getOrdersByProducer(producer.getId());
    }

    @PatchMapping("/{orderId}/status")
    public OrderResponseDTO updateStatus(@PathVariable Long orderId,
                                         @RequestBody @Valid StatusUpdateDTO statusUpdateDTO) {
        // Verify that the order contains products from the current producer
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userDetailsService.loadUserByEmail(email);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));
        
        // Check if order contains products from this producer
        boolean hasProducerProducts = order.getItems().stream()
                .anyMatch(item -> item.getProduct().getProducer().getId() == currentUser.getId());
        
        if (!hasProducerProducts) {
            throw new OrderNotFoundException("Order does not contain products from this producer");
        }
        
        return orderService.updateStatus(orderId, statusUpdateDTO.getStatus());
    }

    @PatchMapping("/{orderId}/cancel")
    public OrderResponseDTO cancelOrder(@PathVariable Long orderId) {
        // Allow customers to cancel their own orders
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userDetailsService.loadUserByEmail(email);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));
        
        // Verify that the current user is the customer who placed this order
        if (order.getCustomer().getId() != currentUser.getId()) {
            throw new OrderNotFoundException("You can only cancel your own orders");
        }
        
        // Only allow cancellation of PENDING or CONFIRMED orders
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Only PENDING or CONFIRMED orders can be cancelled");
        }
        
        return orderService.updateStatus(orderId, OrderStatus.CANCELED);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }

    @GetMapping
    public List<OrderResponseDTO> getAllOrders(@RequestParam(required = false) OrderStatus status) {

        if (status == null) {
            return orderService.getAllOrders();
        } else {
            return orderService.getOrdersByStatus(status);
        }
    }

    @PutMapping("/{orderId}")
    public OrderResponseDTO updateOrder(@PathVariable Long orderId,
                                        @RequestBody @Valid OrderCreateDTO dto) {
        return orderService.updateOrder(orderId, dto);
    }

    @PutMapping("/{orderId}/concat")
    public OrderResponseDTO updateOrderConcat(@PathVariable Long orderId,
                                        @RequestBody @Valid OrderCreateDTO dto) {
        return orderService.updateOrderConcat(orderId, dto);
    }

}
