package com.ecdc.erestaurant.screens.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecdc.androidlibs.database.entity.Category;
import com.ecdc.androidlibs.database.entity.CategoryWrapper;
import com.ecdc.androidlibs.database.entity.Order;
import com.ecdc.androidlibs.database.entity.Product;
import com.ecdc.androidlibs.database.entity.ProductWrapper;
import com.ecdc.androidlibs.database.entity.Table;
import com.ecdc.androidlibs.network.NetworkProvider;
import com.ecdc.erestaurant.R;
import com.ecdc.erestaurant.config.ERestaurant;
import com.ecdc.erestaurant.screens.adapter.ProductAdapter;
import com.ecdc.erestaurant.screens.adapter.TabCategoryAdapter;
import com.ecdc.erestaurant.screens.core.BaseActivity;
import com.ecdc.erestaurant.screens.dialog.ProductOptionDialog;
import com.ecdc.erestaurant.usecases.ProductUseCase;
import com.ecdc.erestaurant.usecases.ProductUseCaseImpl;
import com.ecdc.erestaurant.utils.RecyclerViewBuilder;
import com.tuyenmonkey.mkloader.MKLoader;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.imstudio.core.ui.pager.decoration.SpaceItemDecoration;
import me.imstudio.core.ui.pager.listener.EndlessRecyclerViewScrollListener;

import static com.ecdc.erestaurant.screens.activity.DetailOrderActivity.ORDER_PRODUCT;
import static com.ecdc.erestaurant.screens.activity.PreviewOrderActivity.ORDER;

public class MenuActivity extends BaseActivity<ProductUseCase.Presenter> implements ProductUseCase.View {
    public static final String BUNDLE = "bundle";
    public static final String TABLE = "table";
    public static MenuActivity instance;

    public static void start(Context context, Table table)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TABLE, table);
        Intent intent = new Intent(context, MenuActivity.class);
        intent.putExtra(BUNDLE, bundle);
        context.startActivity(intent);
    }

    public static void start(Context context, Table table, ProductWrapper orderedItem)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TABLE, table);
        bundle.putSerializable(ORDER_PRODUCT, orderedItem);
        Intent intent = new Intent(context, MenuActivity.class);
        intent.putExtra(BUNDLE, bundle);
        context.startActivity(intent);
    }

    public static void start(Context context, Table table, Order order)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ORDER, order);
        bundle.putSerializable(TABLE, table);
        Intent intent = new Intent(context, MenuActivity.class);
        intent.putExtra(BUNDLE, bundle);
        context.startActivity(intent);
    }
    @BindView(R.id.text_table_name)
    TextView tableName;
    @BindView(R.id.tab_category)
    RecyclerView tabCategory;
    @BindView(R.id.recycler_view)
    RecyclerView listProducts;
    @BindView(R.id.search_product)
    EditText searchBar;
    @BindView(R.id.text_quantity)
    TextView quantity;
    @BindView(R.id.layout_empty)
    LinearLayout layoutEmpty;
    @BindView(R.id.ic_loader)
    MKLoader icLoader;
    @BindView(R.id.btn_option)
    View btnOption;
    @BindView(R.id.selected_option)
    TextView selectedOption;

    @BindView(R.id.img_cpy)
    ImageView mDummyImgView;
    @BindView(R.id.view_detail) View viewDetail;

    int actionbarheight;
    public View currentItemView;

    private Table currentTable;
    private TabCategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private ProductOptionDialog dialog;
    private PopupMenu popupMenu;
    private String selectedCategory;
    private String searchKey;
    private boolean isSearching;
    private boolean isShowingPopup;

    private ProductWrapper orderedItem;
    private Order order;

    @Inject
    NetworkProvider provider;

    @Override
    public int getLayout()
    {
        return R.layout.layout_menu_activity;
    }

    @Override
    public void onSyncViews()
    {
        super.onSyncViews();
        ERestaurant.self(this).getApplicationComponent().inject(this);
        setPresenter(new ProductUseCaseImpl(this, this, provider));
        dialog = new ProductOptionDialog();
        popupMenu = new PopupMenu(this, btnOption, R.style.BasePopupMenu);
        popupMenu.inflate(R.menu.popup_menu);

        TypedValue tv = new TypedValue();
        if (this.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionbarheight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

    }

    private void animateView(View foodCardView, Bitmap b) {
        mDummyImgView.setImageBitmap(b);
        mDummyImgView.setVisibility(View.VISIBLE);
        int u[] = new int[2];
        quantity.getLocationInWindow(u);

        mDummyImgView.setLeft(foodCardView.getLeft());
        mDummyImgView.setTop(foodCardView.getTop());
        AnimatorSet animSetXY = new AnimatorSet();
        ObjectAnimator y = ObjectAnimator.ofFloat(mDummyImgView, "translationY", mDummyImgView.getTop(), u[1] - (2 * actionbarheight));
        ObjectAnimator x = ObjectAnimator.ofFloat(mDummyImgView, "translationX", mDummyImgView.getLeft(), u[0] - (2 * actionbarheight));
        ObjectAnimator sy = ObjectAnimator.ofFloat(mDummyImgView, "scaleY", 0.9f, 0.1f);
        ObjectAnimator sx = ObjectAnimator.ofFloat(mDummyImgView, "scaleX", 0.9f, 0.1f);
        animSetXY.playTogether(x, y, sx, sy);
        animSetXY.setDuration(1000);
        animSetXY.start();
    }

    @Override
    public void onSyncEvents() {

        categoryAdapter = new TabCategoryAdapter(this, category -> {
            productAdapter.clearAll();
            selectedCategory = category.getId();
            isSearching = false;
            selectedOption.setVisibility(View.GONE);
            if (category.getId() == null)
                presenter.getAllProducts(1, 10);
            else
                presenter.getProductByCategoryId(selectedCategory, 1, 10);
        });
        productAdapter = new ProductAdapter(this, (s, product, cardv) -> {
            if (s.equalsIgnoreCase("Add to cart")) {
               // Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                //orderedItem.getProductById(product.getId()).setQuantity(product.getQuantity() + 1);
                //Set Animation

                currentItemView = cardv;

                if (cardv != null) {
                    Bitmap b = ERestaurant.self(this).loadBitmapFromView(cardv, cardv.getWidth(), cardv.getHeight());
                    animateView(cardv, b);
                }

                Product addedProduct = orderedItem.getProductById(product.getId());
                if (addedProduct == null)
                {
                    product.setQuantity(1);
                    orderedItem.addProduct(product);
                }
                else
                    addedProduct.setQuantity(addedProduct.getQuantity() + 1);
            }
            if (s.equalsIgnoreCase("Add note")) {
                Product addedProduct = orderedItem.getProductById(product.getId());
                if (addedProduct == null)
                    dialog.setInfoToDialog(product);
                else
                    dialog.setInfoToDialog(addedProduct);

                dialog.show(getSupportFragmentManager(), ProductOptionDialog.TAG);
                //Toast.makeText(this, "Add note", Toast.LENGTH_SHORT).show();
            }
            quantity.setText(NumberFormat.getInstance().format(orderedItem.getQuantity()));
        });

        RecyclerViewBuilder.with().into(tabCategory)
                .initializeWithDefaultLinearLayout(LinearLayoutManager.HORIZONTAL)
                .setAdapter(categoryAdapter);
        RecyclerViewBuilder.with().into(listProducts)
                .initializeWithDefaultGridLayout(3)
                .addItemDecoration(new SpaceItemDecoration(12))
                .setAdapter(productAdapter);

        listProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        listProducts.addOnScrollListener(new EndlessRecyclerViewScrollListener(
                (GridLayoutManager) listProducts.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (isSearching)
                    presenter.searchProductByName(searchKey, page+1, totalItemsCount);
                else if (selectedCategory == null)
                    presenter.getAllProducts(page + 1, totalItemsCount);
                else
                    presenter.getProductByCategoryId(selectedCategory, page + 1, totalItemsCount);
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                productAdapter.clearAll();
                isSearching = true;
                searchKey = searchBar.getText().toString();
                presenter.searchProductByName(searchKey, 1, 10);
            }
        });

        dialog.addOnDoneListener((product) -> {
            if (product.getQuantity()>0)
            {
                //orderedItem.addProduct (product);
                //totalQuantity += product.getQuantity();
                Product addedProduct = orderedItem.getProductById(product.getId());

                if (addedProduct == null)
                    orderedItem.addProduct(product);
                /*else
                    addedProduct.setQuantity(product.getQuantity());
*/
                quantity.setText(NumberFormat.getInstance().format(orderedItem.getQuantity()));

                /*if (currentItemView!= null) {
                    Bitmap b = ERestaurant.self(this).loadBitmapFromView(currentItemView, currentItemView.getWidth(), currentItemView.getHeight());
                    animateView(currentItemView, b);
                }*/
            }

        });

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            switch (id)
            {
                case R.id.tom:
                    searchKey = "tom";
                    break;
                case R.id.cua:
                    searchKey = "cua";
                    break;
                case R.id.ga:
                    searchKey = "ga";
                    break;
                case R.id.so:
                    searchKey = "so";
                    break;
                case R.id.khan:
                    searchKey = "khan";
                    break;
            }
            isSearching = true;
            isShowingPopup = false;
            selectedOption.setText(item.getTitle());
            selectedOption.setVisibility(View.VISIBLE);
            productAdapter.clearAll();
            presenter.searchProductByName(searchKey, 1, 10);
            categoryAdapter.disselectTab();
            return true;
        });
    }

    @Override
    public void onSyncData()
    {
        instance = this;
        try {
            currentTable = (Table) getIntent().getBundleExtra(BUNDLE).get(TABLE);
            orderedItem = (ProductWrapper)getIntent().getBundleExtra(BUNDLE).get(ORDER_PRODUCT);

            order = (Order)getIntent().getBundleExtra(BUNDLE).get(ORDER);

            if (orderedItem==null)
                orderedItem = new ProductWrapper();

            tableName.setText(String.format("Bàn %s", currentTable.getName()));
            quantity.setText(NumberFormat.getInstance().format(orderedItem.getQuantity()));
            presenter.getAllProducts(1, 10);
            presenter.getCategory();
            showProgress(true);
        }catch (Exception ex)
        {
            ex.printStackTrace();

        }
    }

    @OnClick({R.id.btn_back, R.id.btn_view_details})
    public void onViewClicked(View view)
    {
        if (view.getId() == R.id.btn_back)
            this.onBackPressed();
        else
        {
            Context context = this;
            if (orderedItem.getQuantity() == 0)
                showError("Vui lòng chọn ít nhất một phần để tiếp tục");
            else {
                icLoader.setVisibility(View.VISIBLE);
                (new Handler()).postDelayed(() -> {
                    if (order == null)
                        DetailOrderActivity.start(context, currentTable, orderedItem);
                    else
                        DetailOrderActivity.start(context, currentTable, order, orderedItem);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();
                }, 500);

            }
        }

    }

    @OnClick(R.id.btn_option)
    public void onClickOption()
    {
        if(isShowingPopup)
            popupMenu.dismiss();
        else
            popupMenu.show();
        isShowingPopup = !isShowingPopup;
    }
    @Override
    public void onLoadCategorySuccessfully(CategoryWrapper data) {
        Category allCategory = new Category();
        allCategory.setNumProducts(data.getNumProduct());

        List<Category> categories = new ArrayList<>();
        categories.add(allCategory);
        categories.addAll(data.getCategories());

        categoryAdapter.addAll(categories);
    }

    @Override
    public void onLoadProductSuccessfully(List<Product> data) {
        productAdapter.addAll(data, true);
        if (productAdapter.getItemCount() == 0)
            layoutEmpty.setVisibility(View.VISIBLE);
        else
            layoutEmpty.setVisibility(View.GONE);
        showProgress(false);
    }

    @Override
    public void onFailure(String errorMessage) {
        showError(errorMessage);
    }

    @Override
    public void onTimeout() {

    }
}
