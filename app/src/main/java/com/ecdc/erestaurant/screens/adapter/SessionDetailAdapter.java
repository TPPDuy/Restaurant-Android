package com.ecdc.erestaurant.screens.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.ecdc.androidlibs.database.entity.OrderDetail;
import com.ecdc.erestaurant.R;
import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.imstudio.core.ui.pager.IMSRecyclerView;

public class SessionDetailAdapter extends IMSRecyclerView<OrderDetail> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview_bill_inside_session, parent, false);
        return new SessionDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IMSRecyclerView.ViewHolder holder, int position) {
        OrderDetail item = setSelectable(holder, position);
        ((SessionDetailHolder)holder).bind(item);
    }

    public class SessionDetailHolder extends ViewHolder<OrderDetail>
    {
        @BindView(R.id.text_stt)
        TextView stt;
        @BindView(R.id.product_name)
        TextView productName;
        @BindView(R.id.product_price)
        TextView productPrice;
        @BindView(R.id.text_unit)
        TextView unit;
        @BindView(R.id.quantity)
        TextView quantity;
        @BindView(R.id.text_note)
        TextView note;

        public SessionDetailHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(OrderDetail item)
        {
            stt.setText(String.format(Locale.getDefault(), "%d", getAdapterPosition()+1));
            productName.setText(item.getProduct().getName());
            productPrice.setText(NumberFormat.getInstance().format(item.getProduct().getPrice()));

            String strUnit = item.getProduct().getUnit();
            if (strUnit.equalsIgnoreCase("disc"))
                unit.setText("/phần");
            else
                unit.setText(String.format("/%s", strUnit));

            if (item.getNote().isEmpty())
                note.setVisibility(View.GONE);
            else
                note.setText(item.getNote());

            quantity.setText(String.format(Locale.getDefault(), "%d", item.getQuantity()));

        }
    }
}
