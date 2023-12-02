package com.alan.webnuocngot.admin_manager;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminRepository extends CrudRepository<Admin, Integer> {
    int countByEmail(String email);

    @Query(value = "SELECT a FROM Admin a where a.name like %:name%  and a.idA <> :id")
    List<Admin> findAllByNameAdmin(@Param("name") String name, @Param("id") Integer id);
    @Query(value = "select a.name from Admin a where a.idA <> :id")
    List<String> findAllNameAdmin(@Param("id") Integer id);

    Admin findByEmail(String email);
    Admin findByIdA(Integer id);
    @Query(value = "select a from Admin a where a.idA <> :id ")
    List<Admin> findAllByNotId(@Param("id") Integer id);
}
