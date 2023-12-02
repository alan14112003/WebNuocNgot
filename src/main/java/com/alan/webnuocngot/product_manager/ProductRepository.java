package com.alan.webnuocngot.product_manager;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    @Query("select p from Product p where p.gas = ?1")
    List<Product> findAllByGas(String gas);
    @Query("select p from Product p where p.idP = ?1")
    Product findByIdP(Integer id);

    @Query(value = "SELECT count(distinct p.pCode) FROM Product p")
    Integer countDistinctCode();

    @Query(value = "SELECT sum(p.quantity) from Product p")
    Integer countAllQuantityProduct();

    @Query(value = "SELECT sum(p.quantity) from Product p where p.pCode = :code")
    Integer countAllQuantityProductByCode(@Param("code") String code);

    @Query(value = "SELECT distinct p.pCode from Product p")
    List<String> findDistinctCode();

    @Query(value = "SELECT p from Product p where p.pCode = :code")
    List<Product> findAllByPCode(@Param("code") String code);

    @Query(value = "SELECT count (p.sale) from Product p where p.sale <> ''")
    Integer countAllBySale();

    @Query(value = "select p from Product p where p.sale <> ''")
    List<Product> findAllSaleNotNull();

    @Query(value = "select p.nameP from Product p")
    List<String> findAllName();

    @Query(value = "select p from Product p where p.nameP like %:name%")
    List<Product> findAllByName(@Param("name") String name);
}
