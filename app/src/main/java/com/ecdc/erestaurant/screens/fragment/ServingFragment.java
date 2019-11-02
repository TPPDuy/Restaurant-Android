package com.ecdc.erestaurant.screens.fragment;

import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecdc.androidlibs.database.entity.Order;
import com.ecdc.androidlibs.network.NetworkProvider;
import com.ecdc.androidlibs.utils.DatetimeUtils;
import com.ecdc.erestaurant.R;
import com.ecdc.erestaurant.config.ERestaurant;
import com.ecdc.erestaurant.screens.activity.BillActivity;
import com.ecdc.erestaurant.screens.activity.MainActivity;
import com.ecdc.erestaurant.screens.activity.MenuActivity;
import com.ecdc.erestaurant.screens.activity.PreviewOrderActivity;
import com.ecdc.erestaurant.screens.adapter.ServingTableAdapter;
import com.ecdc.erestaurant.screens.core.BaseFragment;
import com.ecdc.erestaurant.usecases.ServingTableUseCase;
import com.ecdc.erestaurant.usecases.ServingTableUseCaseImpl;
import com.ecdc.erestaurant.utils.RecyclerViewBuilder;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.imstudio.core.ui.pager.decoration.SpaceItemDecoration;
import me.imstudio.core.ui.pager.listener.EndlessRecyclerViewScrollListener;

public class ServingFragment extends BaseFragment<ServingTableUseCase.Presenter> implements ServingTableUseCase.View {

    @BindView(R.id.layout_empty)
    LinearLayout layoutEmpty;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.text_today)
    TextView textToday;
    @BindView(R.id.ic_loader)
    MKLoader icLoader;
    @BindView(R.id.ic_add)
    AppCompatImageView icAddSmall;
    @Inject
    NetworkProvider provider;

    private ServingTableAdapter adapter;
    @Override
    public int getLayout()
    {
        return R.layout.layout_serving_fragment;
    }

    @Override
    public void onSyncViews(View view) {
        super.onSyncViews(view);
        ERestaurant.self(requireContext()).getApplicationComponent().inject(this);
        setPresenter(new ServingTableUseCaseImpl(requireContext(), this, provider));

        adapter = new ServingTableAdapter(requireContext(), (s, order) -> {
            if (s.equalsIgnoreCase("Payment"))
            {
                BillActivity.start(getContext(),order,0);
                getOwnActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
            else if (s.equalsIgnoreCase("Session details"))
            {
                PreviewOrderActivity.start(requireContext(), order);
                getOwnActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
            else if(s.equalsIgnoreCase("Add session"))
            {
                MenuActivity.start(requireContext(), order.getTable(), order);
                getOwnActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        RecyclerViewBuilder.with().into(recyclerView)
                .initializeWithDefaultGridLayout(2)
                .addItemDecoration(new SpaceItemDecoration(15))
                .setAdapter(adapter);

        textToday.setText(String.format("Ngày %s", DatetimeUtils.formatType2(Calendar.getInstance().getTimeInMillis())));
    }

    @Override
    public void onSyncEvents()
    {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(
                (GridLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.getServingTable(page + 1, totalItemsCount);
            }
        });
    }
    @Override
    public void onSyncData()
    {
        super.onSyncData();
        //presenter.getServingTable(1, 10);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        adapter.clearAll();
        presenter.getServingTable(1, 10);
    }

    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        if (isVisibleToUser) {
            if (adapter!=null && presenter!=null)
            {
                adapter.clearAll();
                presenter.getServingTable(1, 10);
            }
        }
    }*/

    @OnClick({R.id.btn_add_table_small, R.id.btn_add_table_large})
    public void onAddTable(View view)
    {
        if (view.getId() == R.id.btn_add_table_small)
        {
            icAddSmall.setVisibility(View.GONE);
            icLoader.setVisibility(View.VISIBLE);
            (new Handler()).postDelayed(() -> {
                ((MainActivity)getOwnActivity()).scrollToPage(1);
                icAddSmall.setVisibility(View.VISIBLE);
                icLoader.setVisibility(View.GONE);
            }, 500);
        }
        else
            ((MainActivity)getOwnActivity()).scrollToPage(1);
    }

    @Override
    public void onLoadServingTableSuccessfully(List<Order> data)
    {
        adapter.addAll(data, true);
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
        showError("Time out!");
    }
}
