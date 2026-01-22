package edu.bbte.localproducersmarketplace.service.impl;

import edu.bbte.localproducersmarketplace.exception.ResourceNotFoundException;
import edu.bbte.localproducersmarketplace.mapper.ProductMapper;
import edu.bbte.localproducersmarketplace.model.Product;
import edu.bbte.localproducersmarketplace.repository.ProductRepository;
import edu.bbte.localproducersmarketplace.service.ProductService;
import edu.bbte.localproducersmarketplace.spec.ProductSpec;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    @Autowired
    public ProductServiceImpl(ProductRepository repo, ProductMapper mapper) {
        this.repo = repo;
    }

    @Override
    public Product create(Product product) {
        return repo.saveAndFlush(product);
    }

    @Transactional(readOnly = true)
    @Override
    public Product findById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Product> findFiltered(
            String name,
            Long categoryId,
            Double minPrice,
            Double maxPrice,
            Pageable pageable
    ) {
        Specification<Product> spec = Specification
                .where(ProductSpec.hasName(name))
                .and(ProductSpec.inCategory(categoryId))
                .and(ProductSpec.priceBetween(minPrice, maxPrice));

        return repo.findAll(spec, pageable);
    }

    @Override
    public Product update(Product product) {
        Product existing = repo.findById(product.getId()).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + product.getId()));
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setCategory(product.getCategory());
        return repo.saveAndFlush(existing);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Product not found: " + id);
        repo.deleteById(id);
    }
}

