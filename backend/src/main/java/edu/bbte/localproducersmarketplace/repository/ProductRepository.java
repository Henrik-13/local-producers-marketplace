package edu.bbte.localproducersmarketplace.repository;

import edu.bbte.localproducersmarketplace.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @EntityGraph(attributePaths = {"images", "category", "producer"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Product> findWithImagesById(Long id);
    
    @EntityGraph(attributePaths = {"category", "producer"}, type = EntityGraph.EntityGraphType.FETCH)
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
}
