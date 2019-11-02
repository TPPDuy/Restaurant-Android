package com.ecdc.erestaurant.usecases;

import android.content.Context;

import com.ecdc.androidlibs.database.entity.DepartmentWrapper;
import com.ecdc.androidlibs.database.entity.Table;
import com.ecdc.androidlibs.database.entity.TableWrapper;
import com.ecdc.androidlibs.network.NetworkProvider;
import com.ecdc.androidlibs.network.NetworkResponse;
import com.ecdc.androidlibs.network.ObserverCallback;
import com.ecdc.androidlibs.presentation.BaseUseCase;

import io.reactivex.disposables.Disposable;

public class MapUseCaseImpl extends BaseUseCase<MapUseCase.View> implements MapUseCase.Presenter
{
    private NetworkProvider provider;
    public MapUseCaseImpl(Context context, MapUseCase.View view, NetworkProvider provider)
    {
        super(view, context);
        this.provider = provider;
    }
    @Override
    public void getDepartment()
    {
        if (provider!=null)
        {
            provider.getDepartment()
                    .subscribe(new ObserverCallback<NetworkResponse<DepartmentWrapper>>(view) {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onSuccessful(NetworkResponse<DepartmentWrapper> response) {
                            if (response!=null)
                            {
                                if (response.getData()!=null)
                                    view.onLoadDepartmentSuccessfully(response.getData());
                                else
                                    view.onFailure(response.errorMessage());
                            }
                        }
                    });
        }
    }

    @Override
    public void getAllTable(int page, int size)
    {
        if (provider!=null)
        {
            provider.getAllTable(page, size)
                    .subscribe(new ObserverCallback<NetworkResponse<TableWrapper>>(view) {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onSuccessful(NetworkResponse<TableWrapper> response) {
                            if (response!=null)
                            {
                                if (response.getData()!=null)
                                    view.onLoadTableSuccessfully(response.getData().getTables());
                                else
                                    view.onFailure(response.errorMessage());
                            }
                        }
                    });
        }
    }

    @Override
    public void getTableByDepartment(String departmentId, int page, int size)
    {
        if (provider!=null)
        {
            provider.getTableByDepartment(departmentId, page, size)
                    .subscribe(new ObserverCallback<NetworkResponse<TableWrapper>>(view) {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onSuccessful(NetworkResponse<TableWrapper> response) {
                            if (response!=null)
                            {
                                if (response.getData()!=null)
                                    view.onLoadTableSuccessfully(response.getData().getTables());
                                else
                                    view.onFailure(response.errorMessage());
                            }
                        }
                    });
        }
    }

    @Override
    public void searchTable(String key, int page, int size)
    {
        if (provider!=null)
        {
            provider.searchTableByName(key, page, size)
                    .subscribe(new ObserverCallback<NetworkResponse<TableWrapper>>(view) {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onSuccessful(NetworkResponse<TableWrapper> response) {
                            if (response!=null)
                            {
                                if (response.getData()!=null)
                                    view.onLoadTableSuccessfully(response.getData().getTables());
                                else
                                    view.onFailure(response.errorMessage());
                            }
                        }
                    });
        }
    }

    @Override
    public void changeTableStatus(String id, int status)
    {
        if (provider!=null)
        {
            provider.changeTableStatus(id, status)
                    .subscribe(new ObserverCallback<NetworkResponse<Table>>(view) {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onSuccessful(NetworkResponse<Table> response) {
                            if (response!=null)
                            {
                                if (response.getData()!=null)
                                    view.onHandleTableStatusSuccessfully(response.getData());
                                else
                                    view.onFailure(response.errorMessage());
                            }
                        }
                    });
        }
    }
}
