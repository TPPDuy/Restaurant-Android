package com.ecdc.erestaurant.usecases;

import android.content.Context;

import com.ecdc.androidlibs.database.entity.OrderWrapper;
import com.ecdc.androidlibs.network.NetworkProvider;
import com.ecdc.androidlibs.network.NetworkResponse;
import com.ecdc.androidlibs.network.ObserverCallback;
import com.ecdc.androidlibs.presentation.BaseUseCase;

import io.reactivex.disposables.Disposable;

public class ServingTableUseCaseImpl extends BaseUseCase<ServingTableUseCase.View> implements ServingTableUseCase.Presenter {

    private NetworkProvider provider;

    public ServingTableUseCaseImpl(Context context, ServingTableUseCase.View view, NetworkProvider provider)
    {
        super(view, context);
        this.provider = provider;
    }

    @Override
    public void getServingTable(int page, int size)
    {
        if (provider!=null)
        {
            provider.getServingTable(page, size).subscribe(new ObserverCallback<NetworkResponse<OrderWrapper>>(view){
                @Override
                public void onSubscribe(Disposable d)
                { }

                @Override
                protected void onSuccessful(NetworkResponse<OrderWrapper> response)
                {
                    if(response!=null)
                    {
                        if (response.getData()!=null)
                            view.onLoadServingTableSuccessfully(response.getData().getOrders());
                        else
                            view.onFailure(response.errorMessage());
                    }
                    else
                        view.onTimeout();
                }
            });
        }
    }
}
