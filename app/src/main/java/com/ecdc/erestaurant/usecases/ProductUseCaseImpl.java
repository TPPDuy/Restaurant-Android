package com.ecdc.erestaurant.usecases;

import android.content.Context;

import com.ecdc.androidlibs.database.entity.CategoryWrapper;
import com.ecdc.androidlibs.database.entity.ProductWrapper;
import com.ecdc.androidlibs.network.NetworkProvider;
import com.ecdc.androidlibs.network.NetworkResponse;
import com.ecdc.androidlibs.network.ObserverCallback;
import com.ecdc.androidlibs.presentation.BaseUseCase;

import io.reactivex.disposables.Disposable;

public class ProductUseCaseImpl extends BaseUseCase<ProductUseCase.View> implements ProductUseCase.Presenter {

    private NetworkProvider provider;

    public ProductUseCaseImpl(Context context, ProductUseCase.View view, NetworkProvider provider)
    {
        super(view, context);
        this.provider = provider;
    }

    @Override
    public void getCategory() {
        if (provider!=null)
        {
            provider.getCategory()
                    .subscribe(new ObserverCallback<NetworkResponse<CategoryWrapper>>(view){
                        @Override
                        protected void onSuccessful(NetworkResponse<CategoryWrapper> response) {
                            if (response!=null)
                            {
                                if (response.getData()!=null)
                                    view.onLoadCategorySuccessfully(response.getData());
                                else
                                    view.onFailure(response.errorMessage());
                            }
                        }

                        @Override
                        public void onSubscribe(Disposable d) {

                        }
                    });
        }
    }

    @Override
    public void getAllProducts(int page, int size) {
        if (provider!=null)
        {
            provider.getAllProducts(page, size)
                    .subscribe(new ObserverCallback<NetworkResponse<ProductWrapper>>(view){
                        @Override
                        protected void onSuccessful(NetworkResponse<ProductWrapper> response) {
                            if (response!=null)
                            {
                                if (response.getData()!=null)
                                    view.onLoadProductSuccessfully(response.getData().getProducts());
                                else
                                    view.onFailure(response.errorMessage());
                            }
                        }

                        @Override
                        public void onSubscribe(Disposable d) {

                        }
                    });
        }
    }

    @Override
    public void getProductByCategoryId(String id, int page, int size) {
        if (provider!=null)
        {
            provider.getProductByCategory(id, page, size)
                    .subscribe(new ObserverCallback<NetworkResponse<ProductWrapper>>(view){
                        @Override
                        protected void onSuccessful(NetworkResponse<ProductWrapper> response) {
                            if (response!=null)
                            {
                                if (response.getData()!=null)
                                    view.onLoadProductSuccessfully(response.getData().getProducts());
                                else
                                    view.onFailure(response.errorMessage());
                            }
                        }

                        @Override
                        public void onSubscribe(Disposable d) {

                        }
                    });
        }
    }

    @Override
    public void searchProductByName(String name, int page, int size) {
        if (provider!=null)
        {
            provider.searchProductByName(name, page, size)
                    .subscribe(new ObserverCallback<NetworkResponse<ProductWrapper>>(view){
                        @Override
                        protected void onSuccessful(NetworkResponse<ProductWrapper> response) {
                            if (response!=null)
                            {
                                if (response.getData()!=null)
                                    view.onLoadProductSuccessfully(response.getData().getProducts());
                                else
                                    view.onFailure(response.errorMessage());
                            }
                        }

                        @Override
                        public void onSubscribe(Disposable d) {

                        }
                    });
        }
    }
}
