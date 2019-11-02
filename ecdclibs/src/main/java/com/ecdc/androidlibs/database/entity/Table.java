package com.ecdc.androidlibs.database.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import me.imstudio.core.ui.pager.Selectable;

public class Table extends Selectable implements Serializable
{
    @SerializedName("_id")
    private String id;
    @SerializedName("department")
    private String departmentId;
    @SerializedName("status")
    private int status;
    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setStatus(int newStatus)
    {
        this.status = newStatus;
    }
}
