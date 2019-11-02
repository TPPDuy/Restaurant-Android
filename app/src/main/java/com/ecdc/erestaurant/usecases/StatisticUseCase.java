package com.ecdc.erestaurant.usecases;

import com.ecdc.androidlibs.database.entity.OrderWrapper;
import com.ecdc.androidlibs.database.entity.TableStatistics;
import com.ecdc.androidlibs.database.entity.TimeLine;
import com.ecdc.androidlibs.presentation.IBasePresenter;
import com.ecdc.androidlibs.presentation.IBaseView;

public interface StatisticUseCase {
    interface Presenter extends IBasePresenter{

        void getStatisticsTable(TimeLine timeLine);
        void getAllOrderStatistics(int statusSearch, TimeLine timeLine, int page, int pageSize);
        //Test
        void getAllStatistics(int statusSearch, int pageNum, int pageSize);
    }
    interface View extends IBaseView<Presenter>{

        void onHandleTableSuccessful(TableStatistics tableStatistics);
        void onHandleOrderSuccessful(OrderWrapper data);
    }
}
