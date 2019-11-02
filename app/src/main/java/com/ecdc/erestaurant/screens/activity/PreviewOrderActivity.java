package com.ecdc.erestaurant.screens.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecdc.androidlibs.database.entity.Order;
import com.ecdc.androidlibs.database.entity.OrderSessions;
import com.ecdc.erestaurant.R;
import com.ecdc.erestaurant.screens.adapter.PreviewOrderAdapter;
import com.ecdc.erestaurant.screens.core.BaseActivity;
import com.ecdc.erestaurant.utils.RecyclerViewBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ecdc.erestaurant.screens.activity.MenuActivity.BUNDLE;

public class PreviewOrderActivity extends BaseActivity {
    public static final String ORDER = "order";
    public static void start(Context context, Order order)
    {
        Intent intent = new Intent(context, PreviewOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ORDER, order);
        intent.putExtra(BUNDLE, bundle);
        context.startActivity(intent);
    }

    @BindView(R.id.table_name)
    TextView tableName;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.total_session)
    TextView totalSession;

    private Order order;
    private PreviewOrderAdapter adapter;
    @Override
    public int getLayout()
    {
        return R.layout.layout_preview_bill_activity;
    }

    @Override
    public void onSyncViews()
    {
        super.onSyncViews();
        adapter = new PreviewOrderAdapter();

        RecyclerViewBuilder.with().into(recyclerView)
                .initializeWithDefaultLinearLayout(LinearLayoutManager.VERTICAL)
                .setAdapter(adapter);
    }

    @Override
    public void onSyncEvents()
    {
        super.onSyncEvents();
    }

    @Override
    public void onSyncData()
    {
        super.onSyncData();
        order = (Order)getIntent().getBundleExtra(BUNDLE).get(ORDER);

        if (order!=null)
        {
            tableName.setText(String.format("Bàn %s", order.getTable() == null ? "" : order.getTable().getName()));
            List<OrderSessions> orderSessions = order.getSessions();
            Collections.reverse(orderSessions);
            adapter.addAll(orderSessions);
            totalSession.setText(String.format(Locale.getDefault(), "%s", order.getSessions().size()));
        }


    }

    @OnClick(R.id.btn_close)
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @OnClick(R.id.fab_add)
    public void addSession()
    {
        MenuActivity.start(this, order.getTable(), order);
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @OnClick(R.id.btn_payment)
    public void onPayment()
    {
        BillActivity.start(this, order, 0);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }
}
