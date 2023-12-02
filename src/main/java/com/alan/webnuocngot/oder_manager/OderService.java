package com.alan.webnuocngot.oder_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OderService {
    @Autowired OderRepository repo;

    public String findO(Integer id) {
        return repo.billO(id);
    }

    public String findC(Integer id) {
        return repo.billC(id);
    }

    public List<String> findOlP(Integer id) {
        return repo.billOlP(id);
    }

    public List<Oder> findAllOrder() {
        return repo.findAll();
    }

    public void saveOrder(Oder oder) {
        repo.save(oder);
    }

    public int countOder() {
        return (int) repo.count();
    }
}
