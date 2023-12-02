package com.alan.webnuocngot.cart;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends CrudRepository<Cart, Integer> {
    @Query(value = "select p.idP, p.pCode, p.nameP," +
            " p.price, p.expired, p.sale, p.gas, p.image, c.total, c.id from Cart c join Product p on c.idP = p.idP " +
            "where c.idC = :id")
    List<String> findAllProductByIdC(@Param("id") Integer id);

    @Query(value = "select count(c.id) from Cart c where c.idC = :idC")
    int countAllByIdC(@Param("idC") Integer idC);

    @Query(value = "select count(c.id) from Cart c where c.idC = :idC and c.idP = :idP")
    int countProductByCustomer(@Param("idC") Integer idC, @Param("idP") Integer idP);

    @Query(value = "select c.idP, c.total from Cart c where c.idC = :idC")
    List<String> findAllIdPByIdC(@Param("idC") Integer idC);

    @Query(value = "select c from Cart c where c.idC = :idC")
    List<Cart> findAllCartByIdC(@Param("idC") Integer idC);
}
