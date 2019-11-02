package com.ecdc.erestaurant.usecases;

import com.ecdc.androidlibs.database.entity.DepartmentWrapper;
import com.ecdc.androidlibs.database.entity.Table;
import com.ecdc.androidlibs.presentation.IBasePresenter;
import com.ecdc.androidlibs.presentation.IBaseView;

import java.util.List;

public interface MapUseCase
{
    interface Presenter extends IBasePresenter{
        void getDepartment();
        void getAllTable(int page, int size);
        void getTableByDepartment(String departmentId, int page, int size);
        void searchTable(String key, int page, int size);
        void changeTableStatus(String id, int status);
    }

    interface  View extends IBaseView<Presenter>
    {
        void onLoadDepartmentSuccessfully(DepartmentWrapper data);
        void onLoadTableSuccessfully(List<Table> data);
        void onHandleTableStatusSuccessfully(Table result);
    }

}
