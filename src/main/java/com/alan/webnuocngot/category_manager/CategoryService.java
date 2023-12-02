package com.alan.webnuocngot.category_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired private CategoryRepository repository;

    public List<Category> findAllCategory() {
        return (List<Category>) repository.findAll();
    }

    public Category findByidCategory(String id) {
        Optional<Category> categoryOptional = repository.findById(id);
        return categoryOptional.get();
    }

    public void saveCategory(Category category) {
        repository.save(category);
    }

    public void deleteCategoryByID(String id) {
        repository.deleteById(id);
    }

    public String findNameCatByIdCat(String id) {
        return repository.findNameCatByIdCat(id);
    }
}
