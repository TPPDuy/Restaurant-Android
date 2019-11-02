package com.ecdc.erestaurant.screens.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.ecdc.androidlibs.CallBack;
import com.ecdc.androidlibs.database.entity.OrderDetail;
import com.ecdc.erestaurant.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import me.imstudio.core.ui.pager.IMSRecyclerView;

public class FoodAdapter extends IMSRecyclerView<OrderDetail> {

    private Context context;
    private CallBack.WithPair<String, List<OrderDetail>> callBack;

    private List<OrderDetail> orderDetailArrayList = new ArrayList<>();
    public int status = 0;

    public FoodAdapter(Context context, CallBack.WithPair<String, List<OrderDetail>> callBack){
        this.context = context;
        this.callBack = callBack;

    }

    public void setStatus(int status){
        this.status = status;
        notifyDataSetChanged();
    }

    public List<OrderDetail> getAdapterData()
    {
        return this.mData;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foods, parent, false);
        return new FoodHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IMSRecyclerView.ViewHolder holder, int position) {
        OrderDetail item = setSelectable(holder, position);
        ((FoodHolder) holder).bind(item, position);
    }

    public class FoodHolder extends ViewHolder<OrderDetail>{

        @BindView(R.id.text_name_food)
        TextView nameFood;
        @BindView(R.id.text_stt) TextView stt;
        @BindView(R.id.text_quantity)
        EditText quantity;
        @BindView(R.id.text_price) TextView price;
        @BindView(R.id.text_total_price) TextView totalPrice;

        public FoodHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(OrderDetail item, int position){
            stt.setText(Integer.toString(position+1));

            nameFood.setText(item.getProduct().getName());
            if(status == 0){
                quantity.setEnabled(false);
                quantity.setFocusable(false);
                quantity.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            }else if(status == 1){
                quantity.setEnabled(true);
                quantity.setFocusableInTouchMode(true);
                quantity.setBackgroundResource(R.drawable.all_style_rounded_edittext);
            }
            quantity.setText(Integer.toString(item.getQuantity()));
            price.setText(NumberFormat.getInstance(Locale.getDefault()).format(item.getPrice()));
            long totalPriceNum = item.getTotalPrice();
            totalPrice.setText(NumberFormat.getInstance(Locale.getDefault()).format(totalPriceNum));


            quantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().isEmpty()) {
                        getItem().setQuantity(Integer.parseInt(s.toString()));
                        long totalPriceNum = getItem().getTotalPrice();
                        totalPrice.setText(NumberFormat.getInstance(Locale.getDefault()).format(totalPriceNum));
                        Log.e("TEST UPDATE QUANTITY: ", s.toString());
                    }else {
                      //  Toast.makeText(context,"Số lượng không được để trống", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @OnEditorAction(R.id.text_quantity)
        protected void actionDo(int actionId){
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Auto hide the keyboard after press "Done" key
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(itemView.getWindowToken(), 0);

                List<OrderDetail> orderDetailList =  getAdapterData();
                callBack.run("Completed edit bill",orderDetailList);
            }else {
                callBack.run("", orderDetailArrayList);
            }
        }
    }
}
