package com.ecdc.erestaurant.usecases;

import android.content.Context;

import com.ecdc.androidlibs.database.entity.Order;
import com.ecdc.androidlibs.database.entity.OrderDetail;
import com.ecdc.androidlibs.database.entity.OrderWrapper;
import com.ecdc.androidlibs.database.temporary.OrderSessionAdd;
import com.ecdc.androidlibs.database.temporary.Session;
import com.ecdc.androidlibs.network.NetworkProvider;
import com.ecdc.androidlibs.network.NetworkResponse;
import com.ecdc.androidlibs.network.ObserverCallback;
import com.ecdc.androidlibs.presentation.BaseUseCase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class OrderUseCaseImpl extends BaseUseCase<OrderUseCase.View> implements OrderUseCase.Presenter
{
    private NetworkProvider provider;
    public OrderUseCaseImpl(Context context, OrderUseCase.View view, NetworkProvider provider)
    {
        super(view, context);
        this.provider = provider;
    }
    @Override
    public void createOrder(String tableId, String departmentId, Session session)
    {
        if (provider!=null)
        {
            provider.createOrder(tableId, departmentId, session)
                    .subscribe(new ObserverCallback<NetworkResponse<Object>>(view){
                        @Override
                        protected void onSuccessful(NetworkResponse<Object> response) {
                            if (response!=null)
                            {
                                if (response.getData()!=null)
                                    view.onHandleOrderSuccessful(response.getData());
                                else
                                    view.onFailure(response.errorMessage());
                            }
                        }
                        @Override
                        public void onSubscribe(Disposable d) {

                        }
                    });
        }
    }

    @Override
    public void addSession(OrderSessionAdd session)
    {
        if (provider!=null)
        {
            provider.addSession(session)
                    .subscribe(new ObserverCallback<NetworkResponse<Object>>(view){
                        @Override
                        protected void onSuccessful(NetworkResponse<Object> response) {
                            if (response!=null)
                            {
                                if (response.getData()!=null)
                                    view.onHandleOrderSuccessful(response.getData());
                                else
                                    view.onFailure(response.errorMessage());
                            }
                        }

                        @Override
                        public void onSubscribe(Disposable d) {

                        }
                    });
        }
    }

    @Override
    public void orderBilling(String idOrder, List<OrderDetail> arrFoods, List<OrderDetail> arrDrinks) {
        List<OrderDetail> list = new ArrayList<>();
        list.addAll(arrFoods);
        list.addAll(arrDrinks);
        Order order = new Order(idOrder,list);

        if(provider != null){

            provider.orderBilling(order)
                    .subscribe(new ObserverCallback<NetworkResponse<OrderWrapper>>(view) {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        protected void onSuccessful(NetworkResponse<OrderWrapper> response) {
                            /*if(response != null)
                                view.onHandleOrderSuccessful(response.getData());*/
                            /*else
                                view.onFailure(response.errorMessage());*/
                        }
                    });
        }
    }

    @Override
    public void changeOrderStatus(String orderId, int status)
    {
        if (provider!=null)
        {
            provider.changeOrderStatus(orderId, status)
                    .subscribe(new ObserverCallback<NetworkResponse<Object>>(view){
                        @Override
                        protected void onSuccessful(NetworkResponse<Object> response) {
                            if (response!=null)
                            {
                                if (response.getData()!=null)
                                    view.onHandleOrderSuccessful(response.getData());
                                else
                                    view.onFailure(response.errorMessage());
                            }
                        }
                        @Override
                        public void onSubscribe(Disposable d) {

                        }
                    });
        }
    }
}
