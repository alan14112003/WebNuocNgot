package com.alan.webnuocngot.product_manager;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idP;
    private String nameP;
    private double price;
    private String sale;
    private String expired;
    private String gas;
    private String image;
    private int quantity;
    private String pCode;

    public Product() {

    }

    public Integer getIdP() {
        return idP;
    }

    public void setIdP(Integer idP) {
        this.idP = idP;
    }

    public String getNameP() {
        return nameP;
    }

    public void setNameP(String nameP) {
        this.nameP = nameP;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getpCode() {
        return pCode;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
    }
    public Product(String nameP, double price, String sale, String expired, String gas, int quantity, String pCode) {
        this.nameP = nameP;
        this.price = price;
        this.sale = sale;
        this.expired = expired;
        this.gas = gas;
        this.quantity = quantity;
        this.pCode = pCode;
    }

    @Override
    public String toString() {
        return "Product{" +
                "idP='" + idP + '\'' +
                ", nameP='" + nameP + '\'' +
                ", price=" + price +
                ", sale='" + sale + '\'' +
                ", expired='" + expired + '\'' +
                ", gas='" + gas + '\'' +
                ", image='" + image + '\'' +
                ", quantity=" + quantity +
                '}';
    }
    public String getImageProductPath() {
        if (image == null || image.equals("")) return "/product-images/no-image/no_img.png";
        return "/product-images/" + idP +"/"+ image;
    }

}
