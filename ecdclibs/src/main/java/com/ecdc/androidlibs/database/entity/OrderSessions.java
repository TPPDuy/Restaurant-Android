package com.ecdc.androidlibs.database.entity;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.imstudio.core.ui.pager.Selectable;

public class OrderSessions extends Selectable implements Serializable
{
    @SerializedName("_id")
    private String id;
    @SerializedName("sub_total")
    private long subTotal;
    @SerializedName("created_at")
    private long createdTime;
    @SerializedName("creator")
    private User creator;
    @SerializedName("detail")
    private List<OrderDetail> details;

    public String getId() {
        return id;
    }
    public List<OrderDetail> getDetails() {
        return details;
    }


    public long getSubTotal() {
        return subTotal;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public User getCreator() {
        return creator;
    }

    public int getNumDishes()
    {
        return details.size();
    }

    public List<OrderDetail> getListProductBooking(){
        List<OrderDetail> listProductBooking = new ArrayList<>();
        for(OrderDetail d: this.details){
//            String name = d.getProduct().getName();
//            String type = d.getProduct().getType();
//            long price = d.getProduct().getPrice();
//            int quantity = d.getQuantity();
//            item = new ProductBook(name, type,quantity,price);
//            if(item != null)
//                listProductBooking.add(item);
            listProductBooking.add(d);
        }
        return listProductBooking;

    }
}
