package com.ecdc.androidlibs.database.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TableWrapper
{
    @SerializedName("docs")
    private List<Table> tables;

    public List<Table> getTables() {
        return tables;
    }
}
