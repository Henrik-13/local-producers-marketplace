package edu.bbte.localproducersmarketplace.controller;

import edu.bbte.localproducersmarketplace.dto.in.CategoryInDto;
import edu.bbte.localproducersmarketplace.dto.out.CategoryOutDto;
import edu.bbte.localproducersmarketplace.mapper.CategoryMapper;
import edu.bbte.localproducersmarketplace.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper mapper;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryMapper mapper) {
        this.categoryService = categoryService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CategoryOutDto> create(@Valid @RequestBody CategoryInDto dto) {
        var entity = mapper.toEntity(dto);
        var created = categoryService.create(entity);

        return ResponseEntity
                .created(URI.create("/categories/" + created.getId()))
                .body(mapper.toDto(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryOutDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(
                mapper.toDto(categoryService.findById(id))
        );
    }

    @GetMapping
    public ResponseEntity<List<CategoryOutDto>> getAll() {
        return ResponseEntity.ok(
                mapper.toDtoList(categoryService.findAll())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}