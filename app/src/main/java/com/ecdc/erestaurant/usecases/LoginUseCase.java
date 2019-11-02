package com.ecdc.erestaurant.usecases;

import com.ecdc.androidlibs.presentation.IBasePresenter;
import com.ecdc.androidlibs.presentation.IBaseView;

public interface LoginUseCase {

    interface View extends IBaseView<Presenter> {
        void onLogInSuccessfully();

    }

    interface Presenter extends IBasePresenter {

        void logIn(String userName, String password);
    }
}