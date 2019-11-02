package com.ecdc.androidlibs.network;

import java.io.Serializable;

public class NetworkResponse<T> extends CoreResponse implements Serializable {

    private T data;
    private String msg;
    private int status;

    @Override
    public boolean isSuccessful() {
        return status == 1;
    }

    public int getStatus()
    {
        return status;
    }
    public T getData() {
        return data;
    }

    @Override
    public String errorMessage() {
        return msg;
    }
}
