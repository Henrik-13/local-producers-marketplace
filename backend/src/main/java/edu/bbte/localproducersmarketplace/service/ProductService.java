package edu.bbte.localproducersmarketplace.service;

import edu.bbte.localproducersmarketplace.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
public interface ProductService {
    Product findById(Long id);

    Product create(Product entity);

    Product update(Product entity);

    void delete(Long id);

    Page<Product> findFiltered(String name, Long categoryId, Double minPrice, Double maxPrice, Pageable pageable);
}
