package com.ecdc.androidlibs.database.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import me.imstudio.core.ui.pager.Selectable;

public class CategoryWrapper extends Selectable
{
    @SerializedName("docs")
    private List<Category> categories;

    public List<Category> getCategories() {
        return categories;
    }

    public int getNumProduct()
    {
        int result = 0;
        for(Category c : categories)
            result += c.getNumProducts();
        return result;
    }
}
