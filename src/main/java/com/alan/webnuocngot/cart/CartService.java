package com.alan.webnuocngot.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired private CartRepository repo;

    public List<String> findAllProductByIdC(Integer id) {
        return repo.findAllProductByIdC(id);
    }

    public Optional<Cart> getCard(Integer id) {
        return repo.findById(id);
    }

    public void save(Cart cart) {
        repo.save(cart);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }

    public int countAllByIdC(Integer idC) {
        return repo.countAllByIdC(idC);
    }

    public int countProductByCustomer(Integer idC, Integer idP) {
        return repo.countProductByCustomer(idC, idP);
    }

    public List<String> findAllIdPByIdC(Integer idC) {
        return repo.findAllIdPByIdC(idC);
    }

    public List<Cart> findAllCartByIdC(Integer idC) {
        return repo.findAllCartByIdC(idC);
    }
}
