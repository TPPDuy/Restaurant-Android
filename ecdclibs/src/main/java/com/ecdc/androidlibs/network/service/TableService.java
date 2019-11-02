package com.ecdc.androidlibs.network.service;

import com.ecdc.androidlibs.database.entity.OrderWrapper;
import com.ecdc.androidlibs.database.entity.Table;
import com.ecdc.androidlibs.database.entity.TableWrapper;
import com.ecdc.androidlibs.network.NetworkResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TableService {
    @GET("table_order/index")
    Observable<NetworkResponse<OrderWrapper>> getServingTable(@Query("search_status") int status,
                                                              @Query("pg_page") int page,
                                                              @Query("pg_size") int size);

    @GET("table/index")
    Observable<NetworkResponse<TableWrapper>> getAllTable(@Query("pg_page") int page,
                                                          @Query("pg_size") int size);

    @GET("table/index")
    Observable<NetworkResponse<TableWrapper>> getTableByDepartmentId(@Query("search_department") String departmentId,
                                                                     @Query("pg_page") int page,
                                                                     @Query("pg_size") int size);

    @GET("table/index")
    Observable<NetworkResponse<TableWrapper>> searchTableByName(@Query("search_name") String key,
                                                                @Query("pg_page") int page,
                                                                @Query("pg_size") int size);

    @POST("table/status")
    Observable<NetworkResponse<Table>> changeTableStatus(@Query("_id") String tableId,
                                                         @Query("status") int status);
}
