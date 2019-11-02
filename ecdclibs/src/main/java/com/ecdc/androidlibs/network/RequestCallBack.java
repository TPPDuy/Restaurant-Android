package com.ecdc.androidlibs.network;

public interface RequestCallBack<T> {

    void onSuccessful(T data);

    void onFailure(String error);
}
