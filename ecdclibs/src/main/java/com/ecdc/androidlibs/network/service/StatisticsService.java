package com.ecdc.androidlibs.network.service;

import com.ecdc.androidlibs.database.entity.OrderWrapper;
import com.ecdc.androidlibs.database.entity.TableStatistics;
import com.ecdc.androidlibs.network.NetworkResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StatisticsService {

    @GET("table_statistics/index")
    Observable<NetworkResponse<TableStatistics>> getStatisticsTable(@Query("start_date_s") long startDate,
                                                                 @Query("end_date_s") long endDate);

    @GET("table_order/index")
    Observable<NetworkResponse<OrderWrapper>> getAllOrderStatistics(@Query("search_status") int searchStatus,
                                                                    @Query("start_date_s") long startDate,
                                                                    @Query("end_date_s") long endDate,
                                                                    @Query("pg_page") int page,
                                                                    @Query("pg_size") int pageSize);

    @GET("table_order/index")
    Observable<NetworkResponse<OrderWrapper>> getAllStatistics(@Query("search_status") int searchStatus,
                                                                    @Query("pg_page") int pageNum,
                                                                    @Query("pg_size") int pageSize);
}
