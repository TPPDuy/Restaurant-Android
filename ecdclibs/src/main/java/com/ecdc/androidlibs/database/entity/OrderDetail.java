package com.ecdc.androidlibs.database.entity;

import com.ecdc.androidlibs.database.temporary.ProductTemp;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import me.imstudio.core.ui.pager.Selectable;

public class OrderDetail extends Selectable implements Serializable
{
    @SerializedName("price")
    private long price;
    @SerializedName("product")
    private ProductTemp product;
    @SerializedName("_id")
    private String id;
    @SerializedName("note")
    private String note;
    @SerializedName("quantity")
    private int quantity;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public ProductTemp getProduct() {
        return product;
    }

    public String getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getTotalPrice(){

        return quantity * price;
    }
}
