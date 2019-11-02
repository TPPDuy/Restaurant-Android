package com.ecdc.androidlibs.database.temporary;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderSessionAdd implements Serializable {
    @SerializedName("_id")
    private String orderId;
    @SerializedName("table")
    private String tableId;
    @SerializedName("session")
    private Session session;

    public OrderSessionAdd(String orderId, String tableId, Session session)
    {
        this.orderId = orderId;
        this.tableId = tableId;
        this.session = session;
    }
}
