package com.ecdc.erestaurant.usecases;

import android.content.Context;

import com.ecdc.androidlibs.database.entity.OrderWrapper;
import com.ecdc.androidlibs.database.entity.TableStatistics;
import com.ecdc.androidlibs.database.entity.TimeLine;
import com.ecdc.androidlibs.network.NetworkProvider;
import com.ecdc.androidlibs.network.NetworkResponse;
import com.ecdc.androidlibs.network.ObserverCallback;
import com.ecdc.androidlibs.presentation.BaseUseCase;

import io.reactivex.disposables.Disposable;

public class StatisticUseCaseImpl extends BaseUseCase<StatisticUseCase.View> implements StatisticUseCase.Presenter {

    private NetworkProvider provider;

    public StatisticUseCaseImpl(Context context, StatisticUseCase.View view, NetworkProvider provider){
        super(view, context);
        this.provider=provider;
    }
    @Override
    public void getStatisticsTable(TimeLine timeLine) {
        if (provider != null) {
            provider.getTableStatistics(timeLine.getStartDate()/1000, timeLine.getEndDate()/1000)
                    .subscribe(new ObserverCallback<NetworkResponse<TableStatistics>>(view) {

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        protected void onSuccessful(NetworkResponse<TableStatistics> response) {
                            if(response != null){
                                if(response.getData() != null){
                                    view.onHandleTableSuccessful(response.getData());
                                }else {
                                    view.onFailure(response.errorMessage());
                                }
                            }
                        }

                    });
        }
    }

    @Override
    public void getAllOrderStatistics(int statusSearch, TimeLine timeLine, int page, int pageSize) {
        if(provider != null){
            provider.getAllOrderStatistics(1, timeLine.getStartDate()/1000, timeLine.getEndDate()/1000, page, pageSize)
           // provider.getAllOrderStatistics(1, 1556668800, 1559347199)
                    .subscribe(new ObserverCallback<NetworkResponse<OrderWrapper>>(view) {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }
                        @Override
                        protected void onSuccessful(NetworkResponse<OrderWrapper> response) {
                            if(response != null){
                                if(response.getData() != null){
                                    view.onHandleOrderSuccessful(response.getData());
                                }else {
                                    view.onFailure(response.errorMessage());
                                }
                            }
                        }
                    });
        }
    }


    @Override
    public void getAllStatistics(int statusSearch, int pageNum, int pageSize) {
        if(provider != null){
            provider.getAllStatistics(statusSearch, 1,10)
                    .subscribe(new ObserverCallback<NetworkResponse<OrderWrapper>>(view) {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }
                        @Override
                        protected void onSuccessful(NetworkResponse<OrderWrapper> response) {
                            if(response != null){
                                if(response.getData() != null){
                                    view.onHandleOrderSuccessful(response.getData());
                                }else {
                                    view.onFailure(response.errorMessage());
                                }
                            }
                        }
                    });
        }
    }
}
