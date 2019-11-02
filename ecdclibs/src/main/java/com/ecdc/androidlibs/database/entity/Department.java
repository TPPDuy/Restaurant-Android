package com.ecdc.androidlibs.database.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import me.imstudio.core.ui.pager.Selectable;

public class Department extends Selectable implements Serializable
{
    @SerializedName("_id")
    private String id;
    @SerializedName("tables")
    private int numTable;
    @SerializedName("address")
    private String address;
    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public int getNumTable() {
        return numTable;
    }

    public void setNumTable(int numTable) {
        this.numTable = numTable;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public Department()
    {
        this.id = null;
        this.name = null;
        this.address = null;
        this.numTable = 0;
    }
}
