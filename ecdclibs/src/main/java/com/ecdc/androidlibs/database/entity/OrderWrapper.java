package com.ecdc.androidlibs.database.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderWrapper
{
    @SerializedName("docs")
    private List<Order> orders;
    public List<Order> getOrders() {
        return orders;
    }


}
