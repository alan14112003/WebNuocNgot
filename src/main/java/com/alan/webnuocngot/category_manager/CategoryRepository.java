package com.alan.webnuocngot.category_manager;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends CrudRepository<Category, String> {
    @Query(value = "select c.name from Category c where c.id = :id")
    String findNameCatByIdCat(@Param("id") String id);
}
