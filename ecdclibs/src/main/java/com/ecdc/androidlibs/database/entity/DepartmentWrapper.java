package com.ecdc.androidlibs.database.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DepartmentWrapper
{
    @SerializedName("docs")
    private List<Department> departments;

    public List<Department> getDepartments() {
        return departments;
    }
    public int getNumTable()
    {
        int result = 0;
        for (Department d : departments)
            result += d.getNumTable();
        return result;
    }
}
