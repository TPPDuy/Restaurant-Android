package com.ecdc.androidlibs.database.entity;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.imstudio.core.ui.pager.Selectable;

public class Order extends Selectable implements Serializable
{
    @SerializedName("_id")
    private String id;
//    @SerializedName("bills")
//    private List<ProductBook> bills;
//
//    public Order(String id, List<ProductBook> bills) {
//        this.id = id;
//        this.bills = bills;
//    }

    @SerializedName("bills")
    private List<OrderDetail> bills;

    public List<OrderDetail> getBills() {
        return bills;
    }

    public Order(String id, List<OrderDetail> bills) {
        this.id = id;
        this.bills = bills;
    }

    @SerializedName("table")
    private Table table;
    @SerializedName("created_at")
    private long createdTime;
    @SerializedName("billed_at")
    private long billedTime;
    @SerializedName("status")
    private int status;
    @SerializedName("sessions")
    private List<OrderSessions> sessions;
    @SerializedName("department")
    private Department department;
    @SerializedName("total")
    private long total;
    @SerializedName("discount_percent")
    private int discountPercent;
    @SerializedName("discount_code")
    private String discountCode;
    @SerializedName("id")
    private String code;

    public String getId() {
        return id;
    }

    public Table getTable() {
        return table;
    }

    public int getStatus() {
        return status;
    }

    public long getTotal() {
        return total;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public String getCode() {
        return code;
    }

    public List<OrderSessions> getSessions() {
        return sessions;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public Department getDepartment() {
        return department;
    }
    public long getBilledTime() {
        return billedTime;
    }

    public int getNumDishes()
    {
        int num = 0;
        for (OrderSessions s  : sessions)
            num += s.getNumDishes();
        return num;
    }

    public List<OrderDetail> getProductBooking(){

        List<OrderDetail> listProductBooking = new ArrayList<>();
        for(OrderSessions s: sessions){
           List<OrderDetail> listProductDetail = s.getListProductBooking();
           for(int i = 0; i < listProductDetail.size(); i++){
               listProductBooking.add(listProductDetail.get(i));
           }
        }
        if(listProductBooking != null){
            for(int i = 1 ; i < listProductBooking.size(); i ++){
              for(int j = 0; j < i; j ++){
                  if(listProductBooking.get(i).getProduct().getName().equals(listProductBooking.get(j).getProduct().getName())){
                      int quantityIncrease = listProductBooking.get(i).getQuantity() + listProductBooking.get(j).getQuantity();
                          listProductBooking.get(i).setQuantity(quantityIncrease);
                          listProductBooking.remove(listProductBooking.get(j));
                          i--;
                  }
              }
            }
        }
        return listProductBooking;
    }


    public List<OrderDetail> getFoodsBooking(){
        if(bills.size() == 0){
            bills = getProductBooking();
        }else {

        }
        List<OrderDetail> listFoods = new ArrayList<>();
        for(OrderDetail item:  bills){
            if(item.getProduct().getType().equals("food")){
                listFoods.add(item);
            }
        }
        return listFoods;
    }


    public List<OrderDetail> getDrinksBooking(){
        if(bills.size() == 0){
            bills = getProductBooking();
        }else {

        }
        List<OrderDetail> listDrinks = new ArrayList<>();
        for(OrderDetail item:  bills){
            if(item.getProduct().getType().equals("drink")){
                listDrinks.add(item);
            }
        }
        return listDrinks;
    }


//    public List<ProductBook> getProductBooking(){
//        List<ProductBook> listProductBooking = new ArrayList<>();
//        for(OrderSessions s: sessions){
//           List<ProductBook> listProductDetail = s.getListProductBooking();
//           for(int i = 0; i < listProductDetail.size(); i++){
//               listProductBooking.add(listProductDetail.get(i));
//           }
//        }
//        if(listProductBooking != null){
//            for(int i = 1 ; i < listProductBooking.size(); i ++){
//              for(int j = 0; j < i; j ++){
//                  if(listProductBooking.get(i).getName().equals(listProductBooking.get(j).getName())){
//                      int quantityIncrease = listProductBooking.get(i).getQuantity() + listProductBooking.get(j).getQuantity();
//                          listProductBooking.get(i).setQuantity(quantityIncrease);
//                          listProductBooking.remove(listProductBooking.get(j));
//                          i--;
//                  }
//              }
//            }
//        }
//        return listProductBooking;
//    }

//    public List<ProductBook> getFoodsBooking(){
//        List<ProductBook> productBookList = getProductBooking();
//        List<ProductBook> listFoods = new ArrayList<>();
//        for(ProductBook product: productBookList){
//            if(product.getType().equals("food")){
//                listFoods.add(product);
//            }
//        }
//        return listFoods;
//    }
//
//    public List<ProductBook> getDrinksBooking(){
//        List<ProductBook> productBookList = getProductBooking();
//        List<ProductBook> listDrinks = new ArrayList<>();
//        for(ProductBook product: productBookList){
//            if(product.getType().equals("drink")){
//                listDrinks.add(product);
//            }
//        }
//        return listDrinks;
//    }

}
