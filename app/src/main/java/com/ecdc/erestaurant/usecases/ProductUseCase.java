package com.ecdc.erestaurant.usecases;
import com.ecdc.androidlibs.database.entity.CategoryWrapper;
import com.ecdc.androidlibs.database.entity.Product;
import com.ecdc.androidlibs.presentation.IBasePresenter;
import com.ecdc.androidlibs.presentation.IBaseView;

import java.util.List;

public interface ProductUseCase
{
    interface Presenter extends IBasePresenter{
        void getCategory();
        void getAllProducts(int page, int size);
        void getProductByCategoryId(String id, int page, int size);
        void searchProductByName(String name, int page, int size);
    }

    interface View extends IBaseView<Presenter>{
        void onLoadCategorySuccessfully(CategoryWrapper data);
        void onLoadProductSuccessfully(List<Product> data);
    }
}
