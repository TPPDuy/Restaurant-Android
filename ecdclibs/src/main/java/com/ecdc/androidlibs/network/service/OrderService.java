package com.ecdc.androidlibs.network.service;

import com.ecdc.androidlibs.database.entity.Order;
import com.ecdc.androidlibs.database.entity.OrderWrapper;
import com.ecdc.androidlibs.database.temporary.OrderCreate;
import com.ecdc.androidlibs.database.temporary.OrderSessionAdd;
import com.ecdc.androidlibs.network.NetworkResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderService {
    @POST("table_order/index")
    Observable<NetworkResponse<Object>> createOrder(@Body OrderCreate order);

    @POST("table_order/add_session")
    Observable<NetworkResponse<Object>> addSession(@Body OrderSessionAdd session);

    @POST("table_order/billing")
    Observable<NetworkResponse<OrderWrapper>> orderBilling(@Body Order order);

    @POST("table_order/status")
    Observable<NetworkResponse<Object>> changeOrderStatus(@Query("_id") String orderId,
                                                          @Query("status") int status);
}
