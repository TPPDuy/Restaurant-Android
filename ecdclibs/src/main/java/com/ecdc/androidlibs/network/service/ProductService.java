package com.ecdc.androidlibs.network.service;

import com.ecdc.androidlibs.database.entity.CategoryWrapper;
import com.ecdc.androidlibs.database.entity.ProductWrapper;
import com.ecdc.androidlibs.network.NetworkResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductService {

    @GET("category/index")
    Observable<NetworkResponse<CategoryWrapper>> getCategory();

    @GET("product/index")
    Observable<NetworkResponse<ProductWrapper>> getAllProduct(@Query("pg_page") int page,
                                                              @Query("pg_size") int size);

    @GET("product/index")
    Observable<NetworkResponse<ProductWrapper>> getProductByCategory(@Query("search_category") String id,
                                                                     @Query("pg_page") int page,
                                                                     @Query("pg_size") int size);

    @GET("product/index")
    Observable<NetworkResponse<ProductWrapper>> searchProductByName(@Query("search_name") String name,
                                                                    @Query("pg_page") int page,
                                                                    @Query("pg_size") int size);

}
