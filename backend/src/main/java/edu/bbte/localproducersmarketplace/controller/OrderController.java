package edu.bbte.localproducersmarketplace.controller;

import edu.bbte.localproducersmarketplace.dto.in.OrderCreateDTO;
import edu.bbte.localproducersmarketplace.dto.in.StatusUpdateDTO;
import edu.bbte.localproducersmarketplace.dto.out.OrderResponseDTO;
import edu.bbte.localproducersmarketplace.exception.UserNotFoundException;
import edu.bbte.localproducersmarketplace.model.OrderStatus;
import edu.bbte.localproducersmarketplace.model.User;
import edu.bbte.localproducersmarketplace.repository.UserRepository;
import edu.bbte.localproducersmarketplace.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    @PostMapping("/{userId}")
    public OrderResponseDTO create(@PathVariable Long userId,
                                   @RequestBody @Valid OrderCreateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        return orderService.createOrder(user, dto);
    }

    @GetMapping("/customer/{customerId}")
    public List<OrderResponseDTO> getOrders(@PathVariable Long customerId) {
        return orderService.getOrdersByCustomer(customerId);
    }

    @PatchMapping("/{orderId}/status")
    public OrderResponseDTO updateStatus(@PathVariable Long orderId,
                                         @RequestBody @Valid StatusUpdateDTO statusUpdateDTO) {
        return orderService.updateStatus(orderId, statusUpdateDTO.getStatus());
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
