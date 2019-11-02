package com.ecdc.erestaurant.usecases;

import com.ecdc.androidlibs.database.entity.Order;
import com.ecdc.androidlibs.presentation.IBasePresenter;
import com.ecdc.androidlibs.presentation.IBaseView;

import java.util.List;

public interface ServingTableUseCase
{
    interface Presenter extends IBasePresenter {
        void getServingTable(int page, int size);
    }

    interface View extends IBaseView<Presenter> {
        void onLoadServingTableSuccessfully(List<Order> data);
    }


}
