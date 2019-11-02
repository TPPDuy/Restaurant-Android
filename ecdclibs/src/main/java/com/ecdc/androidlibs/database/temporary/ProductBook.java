package com.ecdc.androidlibs.database.temporary;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import me.imstudio.core.ui.pager.Selectable;

public class ProductBook extends Selectable implements Serializable {
    @SerializedName("name")
    String name;
    @SerializedName("type")
    String type;
    @SerializedName("quantity")
    int quantity;
    @SerializedName("price")
    long price;
    public ProductBook() {

    }
    public ProductBook(String name,int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public ProductBook(String name, String type, int quantity, long price) {
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getTotalPrice(){

        return quantity * price;
    }

}
