package edu.bbte.localproducersmarketplace.service;

import edu.bbte.localproducersmarketplace.model.Category;

import java.util.List;

public interface CategoryService {
    Category create(Category category);
    Category findById(Long id);
    List<Category> findAll();
    void deleteById(Long id);
}
