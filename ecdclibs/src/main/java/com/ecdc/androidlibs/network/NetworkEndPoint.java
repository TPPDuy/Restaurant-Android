package com.ecdc.androidlibs.network;


import com.ecdc.androidlibs.BuildConfig;

/*
 * Define network endpoint
 * */
public final class NetworkEndPoint {

    public static final String BASE_URL = BuildConfig.BASE_URL;
    public static final String BASE_RESOURCE = BuildConfig.BASE_RESOURCE;
    public static final String HOME_URL = BuildConfig.BASE_HOME_URL;
    public static final String BUILD_VERSION = BuildConfig.VERSION_NAME;
    public static final long DEFAULT_READ_TIMEOUT = 30;
    public static final long DEFAULT_CONNECT_TIMEOUT = 30;
    public static final long DEFAULT_WRITE_TIMEOUT = 30;
}


