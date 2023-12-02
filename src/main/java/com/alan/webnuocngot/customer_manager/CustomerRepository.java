package com.alan.webnuocngot.customer_manager;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
//    int countAll();
   Customer findByEmail(String email);
}
