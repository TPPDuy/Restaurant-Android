package com.ecdc.androidlibs.database.temporary;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import me.imstudio.core.ui.pager.Selectable;

import static com.ecdc.androidlibs.network.NetworkEndPoint.BASE_RESOURCE;


public class ProductTemp extends Selectable implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("created_at")
    private long createdTime;
    @SerializedName("type")
    private String type;
    @SerializedName("unit")
    private String unit;
    @SerializedName("discount_percent")
    private int discountPercent;
    @SerializedName("price")
    private long price;
    @SerializedName("image")
    private String imgUrl;
    @SerializedName("desc")
    private String describe;
    @SerializedName("name")
    private String name;

    private int quantity;
    private String note = "";

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }


    public long getCreatedTime() {
        return createdTime;
    }

    public String getType() {
        return type;
    }

    public String getUnit() {
        return unit;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public long getPrice() {
        return price;
    }

    public String getImgUrl() {
        return BASE_RESOURCE + imgUrl;
    }

    public String getDescribe() {
        return describe;
    }

    public String getName() {
        return name;
    }
}
