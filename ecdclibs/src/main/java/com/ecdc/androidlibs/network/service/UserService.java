package com.ecdc.androidlibs.network.service;

import com.ecdc.androidlibs.database.entity.User;
import com.ecdc.androidlibs.network.NetworkResponse;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @POST("user/login")
    Observable<NetworkResponse<User>> logIn(@Query("username") String userName,
                                            @Query("password") String password);
}
