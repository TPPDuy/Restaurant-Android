package com.ecdc.erestaurant.screens.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecdc.androidlibs.database.entity.Order;
import com.ecdc.androidlibs.database.entity.OrderDetail;
import com.ecdc.androidlibs.network.NetworkProvider;
import com.ecdc.androidlibs.utils.DatetimeUtils;
import com.ecdc.erestaurant.R;
import com.ecdc.erestaurant.config.ERestaurant;
import com.ecdc.erestaurant.screens.adapter.FoodAdapter;
import com.ecdc.erestaurant.screens.core.BaseActivity;
import com.ecdc.erestaurant.usecases.OrderUseCase;
import com.ecdc.erestaurant.usecases.OrderUseCaseImpl;
import com.ecdc.erestaurant.utils.RecyclerViewBuilder;
import com.tuyenmonkey.mkloader.MKLoader;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class BillActivity extends BaseActivity<OrderUseCase.Presenter> implements OrderUseCase.View {

    public static final String BILL = "bill";
    private FoodAdapter adapterFoods;
    private FoodAdapter adapterDrinks;
    public List<OrderDetail> arrFoods = new ArrayList<>();
    public List<OrderDetail> arrDrinks = new ArrayList<>();
    private Order order;
    private long totalPriceFoodsNum = 0;
    private long totalPriceDrinksNum = 0;
    private long totalPriceNum = 0;
    private long dateNum =0;

    private long timeStartNum = 0;
    private long timeEndNum = 0;
    private String address="";
    private int status = 0;

    private String idOrder;

    @Inject
    NetworkProvider provider;

    public static void start(Context context, Order order, int type)
    {
        Intent intent = new Intent(context, BillActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BILL, order);
        intent.putExtra(BILL, bundle);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }
    @BindView(R.id.text_title)
    TextView title;
    @BindView(R.id.view_foods) View viewFoods;
    @BindView(R.id.view_drinks) View viewDrinks;

    @BindView(R.id.layout_confirm)
    View layoutConfirm;
    @BindView(R.id.btn_edit)
    AppCompatImageView btnEdit;
    @BindView(R.id.text_number_table)
    TextView numberTable;
    @BindView(R.id.text_time_start) TextView timeStart;
    @BindView(R.id.text_time_end) TextView timeEnd;
    @BindView(R.id.text_date) TextView date;
    @BindView(R.id.text_total_price_foods) TextView totalPriceFoods;
    @BindView(R.id.text_total_price_drinks) TextView totalPriceDrinks;
    @BindView(R.id.recycler_view_foods)
    RecyclerView listFoods;
    @BindView(R.id.recycler_view_drinks)
    RecyclerView listDrinks;
    @BindView(R.id.text_total_price_all) TextView totalPriceAll;
    @BindView(R.id.view_total_price) View viewTotalPrice;
    @BindView(R.id.text_total_price_payment) TextView totalPricePayment;

    @BindView(R.id.btn_confirm_edit) TextView btnConfirm;
    @BindView(R.id.btn_done_edit) TextView btnDone;

    @BindView(R.id.ic_loader)
    MKLoader icLoader;

    @Override
    public int getLayout() {
        return R.layout.layout_bill_activity;
    }

    @Override
    public void onSyncViews(){
        super.onSyncViews();
        ERestaurant.self(this).getApplicationComponent().inject(this);
        setPresenter(new OrderUseCaseImpl(this,this, provider));

        adapterFoods = new FoodAdapter(this, (s, orderDetails) -> {
            if (s.equalsIgnoreCase("Completed edit bill")){
               // Toast.makeText(this,"Test call back", Toast.LENGTH_LONG).show();
                layoutConfirm.setVisibility(View.VISIBLE);
                btnDone.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                arrFoods.clear();
                arrFoods.addAll(orderDetails);
            }else {

            }
        });
        adapterDrinks = new FoodAdapter(this,(s, orderDetails) -> {
            if (s.equalsIgnoreCase("Completed edit bill")){
                layoutConfirm.setVisibility(View.VISIBLE);
                btnDone.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                arrDrinks.clear();
                arrDrinks.addAll(orderDetails);
            }else {

            }
        });

        RecyclerViewBuilder.with().into(listFoods)
                .initializeWithDefaultLinearLayout(LinearLayoutManager.VERTICAL)
                .setAdapter(adapterFoods);

        RecyclerViewBuilder.with().into(listDrinks)
                .initializeWithDefaultLinearLayout(LinearLayoutManager.VERTICAL)
                .setAdapter(adapterDrinks);

        int type = getIntent().getIntExtra("type", 0);

        if(type == 1){//View the bill
            viewTotalPrice.setVisibility(View.VISIBLE);
            layoutConfirm.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
            title.setText("Chi tiết hoá đơn");

        }else {//Edit & action payment with bill
            viewTotalPrice.setVisibility(View.GONE);
            layoutConfirm.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);

            btnConfirm.setVisibility(View.VISIBLE);
            btnDone.setVisibility(View.GONE);
            title.setText("Hoá đơn tạm tính");
        }
        try{
            order = ((Order)getIntent().getBundleExtra(BILL).get(BILL));
            if(order != null){
                //Set info from order

                arrDrinks = order.getDrinksBooking();
                arrFoods = order.getFoodsBooking();

                totalPriceNum = order.getTotal();
                dateNum = order.getCreatedTime();
                timeStartNum = order.getCreatedTime();
                if (type == 1)
                    timeEndNum = order.getBilledTime();
                else
                    timeEndNum = System.currentTimeMillis()/1000;
                address = order.getTable().getName();

                idOrder = order.getId();
            }
        }catch(NullPointerException ex)
        {
            Log.e("", "Null Pointer Ex in Sync Views");
            return;
        }finally {
            setInfoBill();
        }
    }
    private long getPrice(List<OrderDetail> listItem){
        long priceTotal = 0;
        if(listItem.size()> 0){
           for(OrderDetail item: listItem){
               priceTotal += item.getTotalPrice();
           }
           return priceTotal;
        }else
            return 0;
    }
    private void setInfoBill() {
        numberTable.setText("Bàn" + " " + address);

        date.setText(DatetimeUtils.formatType2(dateNum * 1000));
        timeStart.setText(DatetimeUtils.formatType4(timeStartNum * 1000));
        timeEnd.setText(DatetimeUtils.formatType4(timeEndNum * 1000));

        totalPriceAll.setText(NumberFormat.getInstance(Locale.getDefault()).format(totalPriceNum) + " đ");//get to order
        setDataAdapter(arrFoods, arrDrinks);

    }

    private void setDataAdapter(List<OrderDetail> arrFoods, List<OrderDetail> arrDrinks){
        if(arrFoods != null){
            if(arrFoods.size() == 0){
                viewFoods.setVisibility(View.GONE);
            }else {
                viewFoods.setVisibility(View.VISIBLE);
                adapterFoods.clearAll();
                adapterFoods.replaceAll(arrFoods);
            }
        }

        if(arrDrinks != null){
            if(arrDrinks.size() == 0){
                viewDrinks.setVisibility(View.GONE);
            }else {
                viewDrinks.setVisibility(View.VISIBLE);
                adapterDrinks.clearAll();
                adapterDrinks.replaceAll(arrDrinks);
            }
        }

        totalPriceFoodsNum = getPrice(arrFoods);
        totalPriceDrinksNum = getPrice(arrDrinks);

        totalPriceFoods.setText(NumberFormat.getInstance(Locale.getDefault()).format(totalPriceFoodsNum) + " đ");
        totalPriceDrinks.setText(NumberFormat.getInstance(Locale.getDefault()).format(totalPriceDrinksNum) + " đ");
        totalPricePayment.setText(NumberFormat.getInstance(Locale.getDefault()).format(totalPriceFoodsNum + totalPriceDrinksNum) + " đ");
    }
    @OnClick(R.id.btn_edit)
    public void onEdit(){
        status = 1;
        adapterFoods.setStatus(status);
        adapterDrinks.setStatus(status);
        if(status == 1){
            btnEdit.setVisibility(View.GONE);
           // layoutConfirm.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.GONE);
            btnDone.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onSyncData(){
        super.onSyncData();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
    @OnClick(R.id. btn_exit)
    public void Exit(){
        this.onBackPressed();
        MainActivity.instance.setStatusResume(2);
    }

    @OnClick({R.id.btn_done_edit,R.id.btn_confirm_edit})
    public void onHandleEdit(View v){
        int id = v.getId();
        switch (id){
            case R.id.btn_done_edit:
                status = 0;
                adapterFoods.setStatus(status);
                adapterDrinks.setStatus(status);
                btnConfirm.setVisibility(View.VISIBLE);
                btnDone.setVisibility(View.GONE);
                setDataAdapter(arrFoods, arrDrinks);

                // Auto hide the keyboard after press "Done" key
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
            case R.id.btn_confirm_edit:
                //Call Post order bill
                presenter.orderBilling(idOrder,arrFoods, arrDrinks);
                presenter.changeOrderStatus(idOrder, 1);
                icLoader.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onHandleOrderSuccessful(Object order) {
        finish();
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        MainActivity.instance.scrollToPage(2);
    }


    @Override
    public void onFailure(String errorMessage) {
        showError(errorMessage, () -> {
            finish();
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            MainActivity.instance.scrollToPage(2);
        });
    }

    @Override
    public void onTimeout() {

    }
}
