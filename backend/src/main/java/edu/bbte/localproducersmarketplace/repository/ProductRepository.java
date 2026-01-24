package edu.bbte.localproducersmarketplace.repository;

import edu.bbte.localproducersmarketplace.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @EntityGraph(attributePaths = "images")
    Optional<Product> findWithImagesById(Long id);
}
