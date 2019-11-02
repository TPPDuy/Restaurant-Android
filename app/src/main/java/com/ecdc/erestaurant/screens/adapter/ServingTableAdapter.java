package com.ecdc.erestaurant.screens.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ecdc.androidlibs.CallBack;
import com.ecdc.androidlibs.database.entity.Order;
import com.ecdc.androidlibs.utils.DatetimeUtils;
import com.ecdc.erestaurant.R;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imstudio.core.ui.pager.IMSRecyclerView;

public class ServingTableAdapter extends IMSRecyclerView<Order>
{
    private Context context;
    private CallBack.WithPair<String, Order> callBack;

    public ServingTableAdapter(Context context, CallBack.WithPair<String, Order> callBack)
    {
        this.context = context;
        this.callBack = callBack;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_serving_table, parent, false);
        return new ServingTableHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IMSRecyclerView.ViewHolder holder, int position) {
        Order item = setSelectable(holder, position);
        ((ServingTableHolder)holder).bind(item);
    }

    public class ServingTableHolder extends ViewHolder<Order>
    {
        @BindView(R.id.text_table_name)
        TextView tableName;
        @BindView(R.id.text_time)
        TextView time;
        @BindView(R.id.text_quantity)
        TextView quantity;
        @BindView(R.id.text_price)
        TextView price;

        public ServingTableHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(Order item)
        {
            tableName.setText(item.getTable()==null ? "" : item.getTable().getName());
            quantity.setText(String.format("%s %s", item.getNumDishes(), "món"));
            price.setText(String.format("%s đ", NumberFormat.getInstance().format(item.getTotal())));
            time.setText(DatetimeUtils.formatType4(item.getCreatedTime()*1000));
        }

        @OnClick({R.id.layout_holder, R.id.btn_add_dish, R.id.btn_pay})
        public void onViewClicked(View view)
        {
            int id = view.getId();
            switch (id)
            {
                case R.id.btn_add_dish:
                    callBack.run("Add session", getItem());
                    break;
                case R.id.btn_pay:
                    callBack.run("Payment", getItem());
                    break;
                case R.id.layout_holder:
                    callBack.run("Session details", getItem());
                    break;
            }
        }
    }
}
