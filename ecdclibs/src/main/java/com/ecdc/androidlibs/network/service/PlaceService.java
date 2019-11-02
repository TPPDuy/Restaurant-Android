package com.ecdc.androidlibs.network.service;

import com.ecdc.androidlibs.database.entity.DepartmentWrapper;
import com.ecdc.androidlibs.network.NetworkResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface PlaceService
{
    @GET("department/index")
    Observable<NetworkResponse<DepartmentWrapper>> getDepartment();
}
