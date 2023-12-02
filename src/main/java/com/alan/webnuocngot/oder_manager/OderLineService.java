package com.alan.webnuocngot.oder_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OderLineService {
    @Autowired private OrderLineRepository repository;

    public void save(OderLine orderLine) {
        repository.save(orderLine);
    }
}
