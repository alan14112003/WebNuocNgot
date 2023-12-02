package com.alan.webnuocngot.oder_manager;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OderRepository extends CrudRepository<Oder, Integer> {
    @Query(value = "select o.id, o.oderDate from Oder o where o.id = :id ")
    String billO(@Param("id") Integer id);

    @Query(value = "select c.id, c.name, c.phone, c.address " +
            "from Customer c join Oder o on c.id = o.customer " +
            "where o.id = :id ")
    String billC(@Param("id") Integer id);

    @Query(value = "select p.idP, p.nameP, p.price, p.sale, ol.oderQuantity " +
            "from OderLine ol join Product p on ol.productId = p.idP " +
            "where ol.oderId = :id")
    List<String> billOlP(@Param("id") Integer id);

    @Query("select o from Oder o order by o.oderDate desc")
    List<Oder> findAll();

}
