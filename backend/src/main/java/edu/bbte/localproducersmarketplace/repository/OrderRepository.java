package edu.bbte.localproducersmarketplace.repository;

import edu.bbte.localproducersmarketplace.model.Order;
import edu.bbte.localproducersmarketplace.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId")
    List<Order> findByCustomerId(Long customerId);
    
    List<Order> findByStatus(OrderStatus orderStatus);
    
    @Query("SELECT DISTINCT o FROM Order o JOIN o.items i WHERE i.product.producer.id = :producerId")
    List<Order> findByProducerId(Long producerId);
}
