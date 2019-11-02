package com.ecdc.erestaurant.screens.fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecdc.androidlibs.database.entity.Order;
import com.ecdc.androidlibs.database.entity.OrderWrapper;
import com.ecdc.androidlibs.database.entity.TableStatistics;
import com.ecdc.androidlibs.database.entity.TimeLine;
import com.ecdc.androidlibs.network.NetworkProvider;
import com.ecdc.androidlibs.utils.DatetimeUtils;
import com.ecdc.erestaurant.R;
import com.ecdc.erestaurant.config.ERestaurant;
import com.ecdc.erestaurant.screens.activity.BillActivity;
import com.ecdc.erestaurant.screens.activity.MainActivity;
import com.ecdc.erestaurant.screens.adapter.StatisticAdapter;
import com.ecdc.erestaurant.screens.core.BaseFragment;
import com.ecdc.erestaurant.screens.dialog.CalenderDialog;
import com.ecdc.erestaurant.usecases.StatisticUseCase;
import com.ecdc.erestaurant.usecases.StatisticUseCaseImpl;
import com.ecdc.erestaurant.utils.RecyclerViewBuilder;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import static java.util.Calendar.DATE;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.imstudio.core.ui.pager.decoration.SpaceItemDecoration;
import me.imstudio.core.ui.pager.listener.EndlessRecyclerViewScrollListener;
import me.imstudio.core.ui.pager.listener.RecyclerItemClickListener;

public class StatisticFragment extends BaseFragment<StatisticUseCase.Presenter> implements StatisticUseCase.View {

    private CalenderDialog dialog;
    @BindView(R.id.text_number_table)
    TextView numberTable;
    @BindView(R.id.text_number_foods)
    TextView numberFoods;
    @BindView(R.id.text_revenue)
    TextView revenue;
    @BindView(R.id.recycler_view)
    RecyclerView listOrders;
    @BindView(R.id.text_date)
    TextView textDate;
    @BindView(R.id.view_calendar) View viewCalendar;

    @BindView(R.id.view_table_info) View viewTable;
    @BindView(R.id.view_title) View viewTitle;
    @BindView(R.id.layout_empty) View layoutEmpty;

    @Inject
    NetworkProvider provider;

    private StatisticAdapter adapter;

    private TimeLine selectedDate;
    private static int status;

    @Override
    public int getLayout()
    {
        return R.layout.layout_statistic_fragment;
    }
    @Override
    public void onSyncViews(View v){
        super.onSyncViews(v);

        ERestaurant.self(requireContext()).getApplicationComponent().inject(this);
        setPresenter(new StatisticUseCaseImpl(requireContext(), this, provider));
        dialog = new CalenderDialog();
        adapter = new StatisticAdapter(requireContext());

        RecyclerViewBuilder.with().into(listOrders)
                .initializeWithDefaultLinearLayout(LinearLayoutManager.VERTICAL)
                .addItemDecoration(new SpaceItemDecoration(2))
                .setAdapter(adapter);

    }

    @OnClick({R.id.ic_cancel, R.id.view_calendar})
    public void onClick(View view){

        int id = view.getId();

        if(id == R.id.ic_cancel || id == R.id.view_calendar) {
            dialog.show(getFragmentManager(), CalenderDialog.TAG);
        }
    }

    @Override
    public void onSyncEvents() {

        super.onSyncEvents();

        dialog.addOnDoneListener(timeLine -> {
            if(timeLine != null) {
                selectedDate = timeLine;
                //Chose single day
                if (selectedDate.getEndDate() == 0) {
                    //Today
                    if (selectedDate.getStartDate() / 1000 == atStartOfDay() / 1000) {
                        selectedDate = new TimeLine(atStartOfDay(), atEndOfDay());
                        Log.e("END_DATE", Long.toString(atEndOfDay()));
                        String strToday = "Hôm nay" + "\n" + (DatetimeUtils.formatType2(selectedDate.getStartDate()));
                        textDate.setText(strToday);
                    } else {
                        long timeStamp = selectedDate.getStartDate();
                        // Get end of day for Date single chosed
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(timeStamp);
                        int mYear = calendar.get(Calendar.YEAR);
                        int mMonth = calendar.get(Calendar.MONTH);
                        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                        calendar.set(mYear, mMonth, mDay, 23, 59, 59);

                        selectedDate.setEndDate(calendar.getTimeInMillis());

                        String strToday = "Ngày" + "\n" + (DatetimeUtils.formatType2(selectedDate.getStartDate()));
                        textDate.setText(strToday);
                    }
                } else {
                    //Chosing all start & end date
                    textDate.setText(DatetimeUtils.formatType9(selectedDate.getStartDate(), selectedDate.getEndDate()));
                }

                adapter.clearAll();

                presenter.getAllOrderStatistics(1, selectedDate, 1, 10);
                presenter.getStatisticsTable(selectedDate);
            }

        });

        listOrders.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                (view, position, b) -> {
                        Order order = adapter.get(position);
                        if(order != null)
                            BillActivity.start(requireContext(),order,1);
                }));


        listOrders.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        listOrders.addOnScrollListener(new EndlessRecyclerViewScrollListener(
                (LinearLayoutManager) listOrders.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.getAllOrderStatistics( 1, selectedDate, page + 1
                ,  totalItemsCount);
            }
        });

    }

    @Override
    public void onResume()
    {
        super.onResume();
        adapter.clearAll();
        status = MainActivity.instance.getStatusResume();

        if(status == 0 || status == 1){
            selectedDate = new TimeLine(atStartOfDay(), atEndOfDay());
            String strToday = "Hôm nay" + "\n" + (DatetimeUtils.formatType2(selectedDate.getStartDate()));
            textDate.setText(strToday);
        }else{}

        adapter.clearAll();

        presenter.getStatisticsTable(selectedDate);
        presenter.getAllOrderStatistics(1, selectedDate, 1, 10);

    }

   /* Handler booking timeline*/

    public long atStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(DATE),
                0, 0, 0);
        return calendar.getTimeInMillis();
    }

    public long atEndOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(DATE),
                23, 59, 59);
        return calendar.getTimeInMillis();
    }
    @Override
    public void onHandleTableSuccessful(TableStatistics tableStatistics) {
        if(tableStatistics != null) {
            numberFoods.setText(Integer.toString(tableStatistics.getNumberOfFoods()));
            numberTable.setText(Integer.toString(tableStatistics.getNumberOfOrders()));
            revenue.setText(NumberFormat.getInstance(Locale.getDefault()).format(tableStatistics.getTotalRevenue()) + " " +"đ");
            Log.e("Test Data Statistics: ", Integer.toString(tableStatistics.getNumberOfFoods()));
        }
    }

    @Override
    public void onHandleOrderSuccessful(OrderWrapper data) {

        //adapter.clearAll();
        if(data != null)
            adapter.addAll(data.getOrders());
        if (adapter.getItemCount() == 0)
            layoutEmpty.setVisibility(View.VISIBLE);
        else
            layoutEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String errorMessage) {
        showError(errorMessage);

    }

    @Override
    public void onTimeout() {

    }
}

