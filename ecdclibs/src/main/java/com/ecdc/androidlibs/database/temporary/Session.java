package com.ecdc.androidlibs.database.temporary;

import com.ecdc.androidlibs.database.entity.OrderedProduct;
import com.ecdc.androidlibs.database.entity.Product;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Session implements Serializable {

    @SerializedName("creator")
    private String creator;
    @SerializedName("created_at")
    private long createdTime;
    @SerializedName("detail")
    private List<OrderedProduct> details;

    public Session()
    {
        details = new ArrayList<>();
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public void setDetails(List<Product> orderProducts) {
        for (Product i : orderProducts)
            details.add(new OrderedProduct(i));
    }
}
