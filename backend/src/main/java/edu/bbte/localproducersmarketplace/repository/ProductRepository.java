package edu.bbte.localproducersmarketplace.repository;

import edu.bbte.localproducersmarketplace.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
