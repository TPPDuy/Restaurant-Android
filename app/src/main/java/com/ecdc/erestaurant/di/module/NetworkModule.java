package com.ecdc.erestaurant.di.module;

import com.ecdc.androidlibs.network.NetworkProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {

    public NetworkModule() {

    }

    @Provides
    @Singleton
    NetworkProvider providerNetworkModule() {
        return NetworkProvider.self();
    }
}
