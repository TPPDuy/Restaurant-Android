package com.ecdc.erestaurant.screens.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecdc.androidlibs.database.CacheProvider;
import com.ecdc.androidlibs.database.entity.Order;
import com.ecdc.androidlibs.database.entity.ProductWrapper;
import com.ecdc.androidlibs.database.temporary.OrderSessionAdd;
import com.ecdc.androidlibs.database.temporary.Session;
import com.ecdc.androidlibs.database.entity.Table;
import com.ecdc.androidlibs.network.NetworkProvider;
import com.ecdc.erestaurant.R;
import com.ecdc.erestaurant.config.ERestaurant;
import com.ecdc.erestaurant.screens.adapter.DetailOrderAdapter;
import com.ecdc.erestaurant.screens.core.BaseActivity;
import com.ecdc.erestaurant.screens.dialog.ProductOptionDialog;
import com.ecdc.erestaurant.usecases.OrderUseCase;
import com.ecdc.erestaurant.usecases.OrderUseCaseImpl;
import com.ecdc.erestaurant.utils.RecyclerViewBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.imstudio.core.ui.pager.decoration.SpaceItemDecoration;

import static com.ecdc.erestaurant.screens.activity.MenuActivity.BUNDLE;
import static com.ecdc.erestaurant.screens.activity.MenuActivity.TABLE;
import static com.ecdc.erestaurant.screens.activity.PreviewOrderActivity.ORDER;

public class DetailOrderActivity extends BaseActivity<OrderUseCase.Presenter> implements OrderUseCase.View {
    public static final String ORDER_PRODUCT = "order products";

    public static void start(Context context, Table table, ProductWrapper orderedProducts)
    {
        Intent intent = new Intent(context, DetailOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TABLE, table);
        bundle.putSerializable(ORDER_PRODUCT, orderedProducts);
        intent.putExtra(BUNDLE, bundle);

        context.startActivity(intent);
    }

    public static void start(Context context, Table table, Order order, ProductWrapper orderedProducts)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ORDER, order);
        bundle.putSerializable(TABLE, table);
        bundle.putSerializable(ORDER_PRODUCT, orderedProducts);
        Intent intent = new Intent(context, DetailOrderActivity.class);
        intent.putExtra(BUNDLE, bundle);

        context.startActivity(intent);
    }

    @BindView(R.id.text_table_name)
    TextView tableName;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.text_quantity)
    TextView textQuantity;
    @BindView(R.id.ic_loader)
    MKLoader icLoader;
    @BindView(R.id.layout_empty)
    LinearLayout layoutEmpty;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;

    @Inject
    NetworkProvider provider;

    private Table table;
    private ProductWrapper orderedProducts;
    private DetailOrderAdapter adapter;
    private Order order;
    private ProductOptionDialog dialog;
    @Override
    public int getLayout()
    {
        return R.layout.layout_detail_activity;
    }

    @Override
    public void onSyncViews()
    {
        super.onSyncViews();
        ERestaurant.self(this).getApplicationComponent().inject(this);
        setPresenter(new OrderUseCaseImpl(this, this, provider));
        dialog = new ProductOptionDialog();
        adapter = new DetailOrderAdapter(this, (s, product) -> {
            if (s.equalsIgnoreCase("Add note")) {
                dialog.setInfoToDialog(product);
                dialog.show(getSupportFragmentManager(), ProductOptionDialog.TAG);
            }
            else {
                orderedProducts.setProducts(adapter.getAdapterData());
                textQuantity.setText(String.format(Locale.getDefault(), "%d", orderedProducts.getQuantity()));
                if (adapter.getItemCount() == 0)
                    layoutEmpty.setVisibility(View.VISIBLE);
                else
                    layoutEmpty.setVisibility(View.GONE);
            }
        });

        RecyclerViewBuilder.with().into(recyclerView)
                .initializeWithDefaultLinearLayout(LinearLayoutManager.VERTICAL)
                .addItemDecoration(new SpaceItemDecoration(3))
                .setAdapter(adapter);
    }

    @Override
    public void onSyncEvents()
    {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fabAdd.getVisibility() == View.VISIBLE) {
                    fabAdd.hide();
                } else if (dy < 0 && fabAdd.getVisibility() != View.VISIBLE) {
                    fabAdd.show();
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fabAdd.show();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        dialog.addOnDoneListener(product -> {
            textQuantity.setText(String.format(Locale.getDefault(), "%d", orderedProducts.getQuantity()));
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onSyncData()
    {
        try{
            table = (Table)getIntent().getBundleExtra(BUNDLE).get(TABLE);
            orderedProducts = (ProductWrapper)getIntent().getBundleExtra(BUNDLE).get(ORDER_PRODUCT);
            order = (Order)getIntent().getBundleExtra(BUNDLE).get(ORDER);
            tableName.setText(String.format("Bàn %s", table.getName()));
            textQuantity.setText(String.format(Locale.getDefault(), "%d", orderedProducts.getQuantity()));
            adapter.clearAll();
            adapter.addAll(orderedProducts.getProducts());
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /*@Override
    public void onResume()
    {
        super.onResume();
        onSyncData();
    } */

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @OnClick(R.id.btn_close)
    public void onCancel()
    {
        showError("Bạn có chắc huỷ lần đặt món này?", this::onBackPressed);
    }

    @OnClick(R.id.fab_add)
    public void onAddProduct()
    {
        if (order==null)
            MenuActivity.start(this, table, orderedProducts);
        else
            MenuActivity.start(this, table, order);

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }

    @OnClick(R.id.btn_create_order)
    public void onCreateOrder()
    {
        if (orderedProducts.getQuantity() != 0) {
            Session session = new Session();
            session.setCreator(CacheProvider.self().getUser().getId());
            session.setCreatedTime(System.currentTimeMillis() / 1000);
            session.setDetails(orderedProducts.getProducts());
            if (order == null)
                presenter.createOrder(table.getId(), table.getDepartmentId(), session);
            else {
                presenter.addSession(new OrderSessionAdd(order.getId(), table.getId(), session));
            }
            icLoader.setVisibility(View.VISIBLE);
        }
        else
        {
            Context context = this;
            showError("Chọn ít nhất một món để tiếp tục", () -> {
                if (order==null)
                    MenuActivity.start(context, table, orderedProducts);
                else
                    MenuActivity.start(context, table, order);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                finish();
            });
        }
    }

    @Override
    public void onHandleOrderSuccessful(Object order) {
        icLoader.setVisibility(View.GONE);
        MenuActivity.instance.finish();
        MainActivity.instance.scrollToPage(0);
        finish();
    }


    @Override
    public void onFailure(String errorMessage) {
        showError(errorMessage);
    }

    @Override
    public void onTimeout() {

    }
}
