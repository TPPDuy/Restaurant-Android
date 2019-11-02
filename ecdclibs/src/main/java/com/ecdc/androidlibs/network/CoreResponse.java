package com.ecdc.androidlibs.network;

public abstract class CoreResponse {
    public abstract boolean isSuccessful();

    public abstract String errorMessage();
}