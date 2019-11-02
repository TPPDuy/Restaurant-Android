package com.ecdc.androidlibs.database.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import me.imstudio.core.ui.pager.Selectable;

public class TableStatistics extends Selectable implements Serializable {
    @SerializedName("number_of_orders")
    private int numberOfOrders;

    @SerializedName("number_of_foods")
    private int numberOfFoods;

    @SerializedName("total_revenue")
    private long totalRevenue;

    @SerializedName("number_of_pending_orders")
    private int numberOfPendingOrders;

    public int getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(int numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public int getNumberOfFoods() {
        return numberOfFoods;
    }

    public void setNumberOfFoods(int numberOfFoods) {
        this.numberOfFoods = numberOfFoods;
    }

    public long getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(long totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getNumberOfPendingOrders() {
        return numberOfPendingOrders;
    }

    public void setNumberOfPendingOrders(int numberOfPendingOrders) {
        this.numberOfPendingOrders = numberOfPendingOrders;
    }
}
