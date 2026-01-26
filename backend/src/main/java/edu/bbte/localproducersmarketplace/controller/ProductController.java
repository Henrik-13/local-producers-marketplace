package edu.bbte.localproducersmarketplace.controller;

import edu.bbte.localproducersmarketplace.dto.PageResponse;
import edu.bbte.localproducersmarketplace.dto.in.ProductInDto;
import edu.bbte.localproducersmarketplace.dto.out.ProductOutDto;
import edu.bbte.localproducersmarketplace.mapper.ProductMapper;
import edu.bbte.localproducersmarketplace.model.Product;
import edu.bbte.localproducersmarketplace.service.CategoryService;
import edu.bbte.localproducersmarketplace.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;
    private final ProductMapper mapper;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService service, ProductMapper mapper, CategoryService categoryService) {
        this.service = service;
        this.mapper = mapper;
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<ProductOutDto> create(@Valid @RequestBody ProductInDto dto) {
        var entity = mapper.toEntity(dto);
        entity.setCategory(categoryService.findById(dto.getCategoryId()));
        ProductOutDto created = mapper.toDto(service.create(entity));
        return ResponseEntity
                .created(URI.create("/products/" + created.getId()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductOutDto> getOne(@PathVariable Long id) {
        System.out.println(service.findById(id));
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }


    @GetMapping
    public PageResponse<ProductOutDto> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable
    ) {
        Page<Product> page = service.findFiltered(name, categoryId, minPrice, maxPrice, pageable);

        return new PageResponse<>(
                mapper.toDtoList(page.getContent()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductOutDto> update(@PathVariable Long id, @Valid @RequestBody ProductInDto dto) {
        var product = mapper.toEntity(dto);
        product.setId(id);
        Product updated = service.update(product);
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
