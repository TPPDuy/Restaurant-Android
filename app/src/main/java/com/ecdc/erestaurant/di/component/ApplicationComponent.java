package com.ecdc.erestaurant.di.component;


import com.ecdc.erestaurant.di.module.ContextModule;
import com.ecdc.erestaurant.di.module.NetworkModule;
import com.ecdc.erestaurant.screens.activity.BillActivity;
import com.ecdc.erestaurant.screens.activity.DetailOrderActivity;
import com.ecdc.erestaurant.screens.activity.LoginActivity;
import com.ecdc.erestaurant.screens.activity.MenuActivity;
import com.ecdc.erestaurant.screens.fragment.MapFragment;
import com.ecdc.erestaurant.screens.fragment.ServingFragment;
import com.ecdc.erestaurant.screens.fragment.StatisticFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ContextModule.class, NetworkModule.class})
public interface ApplicationComponent {
    void inject(ServingFragment servingFragment);
    void inject(MapFragment mapFragment);
    void inject(StatisticFragment statisticFragment);
    void inject(LoginActivity loginActivity);
    void inject(MenuActivity menuActivity);
    void inject(DetailOrderActivity detailOrderActivity);
    void inject(BillActivity billActivity);

}
