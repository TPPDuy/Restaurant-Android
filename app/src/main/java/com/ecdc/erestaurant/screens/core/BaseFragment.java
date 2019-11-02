package com.ecdc.erestaurant.screens.core;

import android.os.Handler;
import android.view.View;

import com.ecdc.androidlibs.Const;
import com.ecdc.androidlibs.presentation.CoreCallBack;
import com.ecdc.androidlibs.presentation.IBasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.imstudio.core.ui.fragment.IMSFragment;

public abstract class BaseFragment<T extends IBasePresenter> extends IMSFragment {

    protected T presenter;
    protected Unbinder unbinder;

    @Override
    public void onSyncViews(View view) {
        unbinder = ButterKnife.bind(this, view);
        setWaitingTime(Const.WAITING_TIME);
    }

    public void setPresenter(T presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null)
            presenter.dispose();
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null)
            unbinder.unbind();
        super.onDestroyView();
    }

    public BaseActivity getOwnActivity() {
        if (getActivity() == null)
            throw new NullPointerException();
        return (BaseActivity) getActivity();
    }

    @Override
    public void onSyncEvents() {

    }

    @Override
    public void onSyncData() {

    }

    protected void showProgress(boolean isShow) {
        try {
            getOwnActivity().showProgress(isShow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showError(String message) {
        try {
            getOwnActivity().showError(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showErrorForceAction(String message, CoreCallBack callBack) {
        try {
            getOwnActivity().showErrorForceAction(message, callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void wait(final CoreCallBack callBack) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callBack != null)
                    callBack.run();
            }
        }, Const.MIN_DELAY);
    }

    protected void wait(final CoreCallBack callBack, int time) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callBack != null)
                    callBack.run();
            }
        }, time);
    }
}