package com.example.canvasgalerija.models;

import java.io.Serializable;

public class NoveSlikeModel implements Serializable {

    String name;
    String rating;
    int price;
    String img_url;

    public NoveSlikeModel() {
    }

    public NoveSlikeModel(String name, String rating, int price, String img_url) {
        this.name = name;
        this.rating = rating;
        this.price = price;
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
