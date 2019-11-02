package com.ecdc.erestaurant.screens.core;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.ecdc.androidlibs.Const;
import com.ecdc.androidlibs.presentation.CoreCallBack;
import com.ecdc.androidlibs.presentation.ErrorDialog;
import com.ecdc.androidlibs.presentation.IBasePresenter;
import com.ecdc.androidlibs.presentation.ProgressDialog;
import com.ecdc.erestaurant.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.imstudio.core.ui.activity.IMSActivity;
import me.imstudio.core.utils.LocaleUtils;

public abstract class BaseActivity<T extends IBasePresenter> extends IMSActivity {

    private ErrorDialog errorDialog = null;
    private ProgressDialog progressDialog = null;
    protected T presenter;
    private Unbinder unbinder;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleUtils.self().setLocale(newBase));
    }

    @Override
    public void onSyncViews() {
        unbinder = ButterKnife.bind(this);
    }

    public void setPresenter(T presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onSyncEvents() {

    }

    @Override
    public void onSyncData() {

    }

    @Override
    public void onBackPressed() {
        dismissAll();
        if (className == null)
            super.onBackPressed();
        else {
            Intent intent = new Intent(this, className);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        /*overridePendingTransitionExit();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
        if (presenter != null)
            presenter.dispose();
        dismissAll();
    }

    public void showProgress(boolean isShow) {
        try {
            if (isShow) {
                if (progressDialog == null)
                    progressDialog = new ProgressDialog(this);
                if (!progressDialog.isShowing())
                    progressDialog.show();
            } else {
                wait(() -> {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                }, Const.MIN_DELAY);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        //overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     *//*
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    *//**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     *//*
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }*/

    protected void showError(String message, CoreCallBack clickListener, CoreCallBack dismissListener) {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
            if (errorDialog == null)
                errorDialog = new ErrorDialog(this);
            if (!errorDialog.isShowing())
                errorDialog.dismiss();
            errorDialog.show(message, clickListener);
            errorDialog.setDismissListener(dismissListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showErrorForceAction(String message, CoreCallBack onDismissListener) {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
            if (errorDialog == null)
                errorDialog = new ErrorDialog(this);
            if (!errorDialog.isShowing())
                errorDialog.dismiss();
            errorDialog.show(message, onDismissListener);
            errorDialog.setDismissListener(onDismissListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showError(String message, CoreCallBack clickListener) {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
            if (errorDialog == null)
                errorDialog = new ErrorDialog(this);
            if (!errorDialog.isShowing())
                errorDialog.dismiss();
            errorDialog.show(message, clickListener);
            errorDialog.setDismissListener(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showError(String message) {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
            if (errorDialog == null)
                errorDialog = new ErrorDialog(this);
            if (!errorDialog.isShowing())
                errorDialog.show(message, null);
            errorDialog.setDismissListener(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void dismissAll() {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
            if (errorDialog != null && errorDialog.isShowing())
                errorDialog.dismiss();
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
        new Handler().postDelayed(() -> {
            if (callBack != null)
                callBack.run();
        }, time);
    }

}
