package com.ecdc.androidlibs.database.entity;

import com.google.gson.annotations.SerializedName;

import me.imstudio.core.ui.pager.Selectable;

import java.io.Serializable;

import static com.ecdc.androidlibs.network.NetworkEndPoint.BASE_RESOURCE;

public class Category extends Selectable implements Serializable
{
    @SerializedName("_id")
    private String id;
    @SerializedName("created_at")
    private long created_time;
    @SerializedName("products")
    private int numProducts;
    @SerializedName("image")
    private String imgUrl;
    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public long getCreated_time() {
        return created_time;
    }

    public int getNumProducts() {
        return numProducts;
    }

    public String getImgUrl() {
        return BASE_RESOURCE + imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setNumProducts(int num)
    {
        this.numProducts = num;
    }
}
