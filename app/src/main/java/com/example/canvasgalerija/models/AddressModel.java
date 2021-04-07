 package com.example.canvasgalerija.models;

public class AddressModel {

    String userAddress;
    boolean isSelected;
    String id;

    public AddressModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public AddressModel(String userAddress, String id) {
        this.userAddress = userAddress;
        this.isSelected = isSelected;
        this.id = id;
    }
}
