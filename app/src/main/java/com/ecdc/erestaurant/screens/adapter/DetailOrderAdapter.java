package com.ecdc.erestaurant.screens.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ecdc.androidlibs.CallBack;
import com.ecdc.androidlibs.database.entity.Product;
import com.ecdc.erestaurant.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imstudio.core.ui.pager.IMSRecyclerView;

public class DetailOrderAdapter extends IMSRecyclerView<Product>
{
    private Context context;
    private CallBack.WithPair<String, Product> callBack;

    public DetailOrderAdapter(Context context, CallBack.WithPair<String, Product> callBack)
    {
        this.context = context;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent, false);
        return new DetailOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IMSRecyclerView.ViewHolder holder, int position) {
        Product item = setSelectable(holder, position);
        ((DetailOrderHolder)holder).bind(item);
    }

    public List<Product> getAdapterData()
    {
        return this.mData;
    }
    public class DetailOrderHolder extends ViewHolder<Product>
    {
        @BindView(R.id.stt)
        TextView textStt;
        @BindView(R.id.product_name)
        TextView productName;
        @BindView(R.id.text_price)
        TextView price;
        @BindView(R.id.text_unit)
        TextView unit;
        @BindView(R.id.note)
        TextView note;
        @BindView(R.id.quantity)
        EditText editQuantity;

        public DetailOrderHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Product item)
        {
            textStt.setText(String.format(Locale.getDefault(),"%d", getAdapterPosition()+1));
            productName.setText(item.getName());
            price.setText(NumberFormat.getInstance().format(item.getPrice()));

            if (item.getUnit().equalsIgnoreCase("disc"))
                unit.setText("/ phần");
            else
                unit.setText(String.format("/ %s", item.getUnit()));

            if (item.getNote() == null || item.getNote().isEmpty())
                note.setVisibility(View.GONE);
            else
            {
                note.setVisibility(View.VISIBLE);
                note.setText(item.getNote());
            }

            editQuantity.setText(String.format(Locale.getDefault(), "%d", item.getQuantity()));

            editQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().isEmpty())
                        getItem().setQuantity(Integer.parseInt(s.toString()));
                }
            });
        }

        @OnClick({R.id.btn_decrease, R.id.btn_increase, R.id.btn_remove, R.id.add_note})
        public void onViewClicked(View view)
        {
            switch (view.getId())
            {
                case R.id.btn_decrease:
                    if (getItem().getQuantity() > 1) {
                        getItem().setQuantity(getItem().getQuantity() - 1);
                        editQuantity.setText(String.format(Locale.getDefault(), "%d", getItem().getQuantity()));
                        callBack.run("Update quantity", getItem());
                    }
                    break;
                case R.id.btn_increase:
                    getItem().setQuantity(getItem().getQuantity() + 1);
                    editQuantity.setText(String.format(Locale.getDefault(), "%d", getItem().getQuantity()));
                    callBack.run("Update quantity", getItem());
                    break;
                case R.id.btn_remove:
                    mData.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    callBack.run("Update quantity", getItem());
                    break;
                case R.id.add_note:
                    callBack.run("Add note", getItem());
                    break;
            }
        }
    }
}
