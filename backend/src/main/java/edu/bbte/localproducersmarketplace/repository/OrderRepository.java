package edu.bbte.localproducersmarketplace.repository;

import edu.bbte.localproducersmarketplace.model.Order;
import edu.bbte.localproducersmarketplace.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByStatus(OrderStatus orderStatus);
}
