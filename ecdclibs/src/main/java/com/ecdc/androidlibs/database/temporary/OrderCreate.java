package com.ecdc.androidlibs.database.temporary;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OrderCreate {
    @SerializedName("table")
    private String tableId;
    @SerializedName("department")
    private String departmentId;
    @SerializedName("sessions")
    private List<Session> session;

    public OrderCreate(String tableId, String departmentId, Session session)
    {
        this.session = new ArrayList<>();
        this.tableId = tableId;
        this.departmentId = departmentId;
        this.session.add(session);

    }
}
