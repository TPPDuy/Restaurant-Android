package com.ecdc.erestaurant.screens.adapter;

import android.content.Context;
import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.ecdc.androidlibs.CallBack;
import com.ecdc.androidlibs.database.entity.Product;
import com.ecdc.erestaurant.R;
import com.imstudio.core.imageloader.ImageLoader;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imstudio.core.ui.pager.IMSRecyclerView;

public class ProductAdapter extends IMSRecyclerView<Product>{

    private Context context;
    private CallBack.WithPairBonus<String, Product, View> callBack;

    public ProductAdapter(Context context, CallBack.WithPairBonus<String, Product, View> callBack)
    {
        this.context = context;
        this.callBack = callBack;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_product, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IMSRecyclerView.ViewHolder holder, int position) {
        Product item = setSelectable(holder, position);
        ((ProductHolder)holder).bind(item);
    }

    public class ProductHolder extends ViewHolder<Product>
    {
        @BindView(R.id.img_product)
        AppCompatImageView productImg;
        @BindView(R.id.product_name)
        TextView productName;
        @BindView(R.id.products_price)
        TextView productPrice;
        @BindView(R.id.product_unit)
        TextView productUnit;
        @BindView(R.id.layout_holder) View transitionsContainer;

        public ProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Product item)
        {
            ImageLoader.plug().load(productImg, item.getImgUrl());
            productName.setText(item.getName());
            productPrice.setText(NumberFormat.getInstance(Locale.getDefault()).format(item.getPrice()));
            if (item.getUnit().equalsIgnoreCase("disc"))
                productUnit.setText("phần");
        }

        @OnClick({R.id.layout_holder, R.id.btn_add_option})
        public void onViewClicked(View view)
        {
            if (view.getId() == R.id.layout_holder) {

                callBack.run("Add to cart", getItem(), itemView);


            }
            else
                callBack.run("Add note", getItem(), itemView);
        }

    }
}
