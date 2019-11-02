package com.ecdc.androidlibs.database.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import me.imstudio.core.ui.pager.Selectable;

public class OrderedProduct extends Selectable implements Serializable
{
    @SerializedName("product")
    private String productId;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("note")
    private String note;

    public OrderedProduct(Product product)
    {
        this.productId = product.getId();
        this.quantity =  product.getQuantity();
        this.note = product.getNote();
    }
    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getNote() {
        return note;
    }
}
