package com.ecdc.erestaurant.usecases;

import android.content.Context;

import com.ecdc.androidlibs.database.CacheProvider;
import com.ecdc.androidlibs.database.entity.User;
import com.ecdc.androidlibs.network.NetworkProvider;
import com.ecdc.androidlibs.network.NetworkResponse;
import com.ecdc.androidlibs.network.ObserverCallback;
import com.ecdc.androidlibs.presentation.BaseUseCase;

import io.reactivex.disposables.Disposable;

public final class LoginUseCaseImpl extends BaseUseCase<LoginUseCase.View> implements LoginUseCase.Presenter {

    private NetworkProvider provider;

    public LoginUseCaseImpl(LoginUseCase.View view, Context context, NetworkProvider provider) {
        super(view, context);
        this.provider = provider;
    }


    @Override
    public void logIn(String userName, String password)
    {
        provider.logIn(userName, password)
                .subscribe(new ObserverCallback<NetworkResponse<User>>(view)
                {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    protected void onSuccessful(NetworkResponse<User> response) {
                        if (response != null && response.getData() != null)
                        {
                            CacheProvider.self().putToCache(CacheProvider.USER_TYPE, response.getData().toString());
                            view.onLogInSuccessfully();
                        } else {
                            view.onFailure(response.errorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        log(e.getMessage());
                        view.onFailure(e.getMessage());
                    }
                });
    }

}
