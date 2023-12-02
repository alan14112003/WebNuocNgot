package com.alan.webnuocngot.product_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired private ProductRepository repo;

    public List<Product> listAll() {
        return (List<Product>) repo.findAll();
    }
    public Product getIdProduct(Integer id) {
        return repo.findByIdP(id);
    }
    public Product save(Product product) {
        repo.save(product);
        return product;
    }
    public Product get(Integer id) throws ProductNotFoundException {
        Optional<Product> result = repo.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new ProductNotFoundException("Could not find any product with ID "+ id);
    }
    public boolean kiemTra(Integer id) {
        return repo.existsById(id);
    }
    public void delete(Integer id) throws ProductNotFoundException {
        if (repo.findById(id).equals("")) {
            throw new ProductNotFoundException("Could not find any product with ID "+ id);
        }
        repo.deleteById(id);
    }

    public int countAllProduct() {
        return (int) repo.count();
    }

    public int countDistinctPCode() {
        return repo.countDistinctCode();
    }

    public List<String> findDistinctCode() {
        return repo.findDistinctCode();
    }

    public Integer countQuantity() {
        return repo.countAllQuantityProduct();
    }

    public List<Product> findAllByCode(String code) {
        return repo.findAllByPCode(code);
    }
    public List<Product> findAllByGas(String gas) {
        return repo.findAllByGas(gas);
    }
    public Integer countQuantityByCode(String code) {
        return repo.countAllQuantityProductByCode(code);
    }

    public Integer countAllBySale() {
        return repo.countAllBySale();
    }
    public List<Product> findAllSaleNotNull() {
        return repo.findAllSaleNotNull();
    }
    public List<String> findAllName() {
        return repo.findAllName();
    }
    public List<Product> findAllByName(String name) {
        return repo.findAllByName(name);
    }
}
