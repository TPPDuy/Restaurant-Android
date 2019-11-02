package com.ecdc.erestaurant.screens.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ecdc.androidlibs.database.entity.Order;
import com.ecdc.androidlibs.utils.DatetimeUtils;
import com.ecdc.erestaurant.R;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.imstudio.core.ui.pager.IMSRecyclerView;

public class StatisticAdapter extends IMSRecyclerView<Order> {
    private Context context;

    public  StatisticAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_statistic, parent, false);
        return new StatisticHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IMSRecyclerView.ViewHolder holder, int position) {
        Order item = setSelectable(holder, position);
        ((StatisticHolder)holder).bind(item);
    }

    public class StatisticHolder extends ViewHolder<Order>{

        @BindView(R.id.text_code)
        TextView code;
        @BindView(R.id.text_address)
        TextView address;
        @BindView(R.id.text_time)
        TextView time;
        @BindView(R.id.text_total_price)
        TextView totalPrice;
        @BindView(R.id.text_number_foods)
        TextView numberFoods;
        @BindView(R.id.text_date)
        TextView date;


        public StatisticHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void bind(Order item){
            long total = item.getTotal();
            totalPrice.setText(NumberFormat.getInstance(Locale.getDefault()).format(total) + " " +"đ");
            code.setText(item.getCode());
            numberFoods.setText(Integer.toString(item.getNumDishes()));
            date.setText(DatetimeUtils.formatType1(item.getCreatedTime()*1000));
            time.setText(DatetimeUtils.formatType4(item.getBilledTime()*1000));
            String tableName = item.getTable().getName();
            String dep = item.getDepartment().getName();
            String tableInfo = "Bàn" + " " + tableName + " - " + dep;
            address.setText(tableInfo);
//            List<ProductBook> productBookList = item.getProductBooking();
//            int size = productBookList.size();
//            Log.e("CHECK PRODUCT BOOKING: ", Integer.toString(size));

        }
    }
}
