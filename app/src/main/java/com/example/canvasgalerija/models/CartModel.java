package com.example.canvasgalerija.models;

public class CartModel {

    String id;
    String productImg;
    String productPrice;
    String productName;
    int totalPrice;
    String totalQuantity;

    public String getId() {return id;}

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public CartModel() {
    }

    public String getImg_url() {
        return productImg;
    }

    public void setImg_url(String img_url) {
        this.productImg = img_url;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String  productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public CartModel(String id, String productImg,String productPrice, String productName, int totalPrice, String totalQuantity) {
        this.id = id;
        this.productImg = productImg;
        this.productPrice = productPrice;
        this.productName = productName;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
    }
}
