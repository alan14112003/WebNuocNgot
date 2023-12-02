package com.alan.webnuocngot.oder_manager;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OrderLineRepository extends CrudRepository<OderLine, Integer> {
//	@Query(value = "")
}
