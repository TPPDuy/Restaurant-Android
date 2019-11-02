package com.ecdc.erestaurant.usecases;

import com.ecdc.androidlibs.database.entity.OrderDetail;
import com.ecdc.androidlibs.database.temporary.OrderSessionAdd;
import com.ecdc.androidlibs.database.temporary.Session;
import com.ecdc.androidlibs.presentation.IBasePresenter;
import com.ecdc.androidlibs.presentation.IBaseView;

import java.util.List;

public interface OrderUseCase {

    interface Presenter extends IBasePresenter
    {
        void createOrder(String tableId, String departmentId, Session session);
        void addSession(OrderSessionAdd session);
        void orderBilling(String idOrder, List<OrderDetail> arrFoods, List<OrderDetail> arrDrinks);
        void changeOrderStatus(String orderId, int status);
    }

    interface View extends IBaseView<Presenter>
    {
        void onHandleOrderSuccessful(Object order);
        //void onUpdateQuantitySuccessful(String message);
    }
}
