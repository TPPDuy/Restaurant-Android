package com.ecdc.androidlibs.presentation;

import android.content.Context;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.imstudio.core.utils.LogUtils;

public abstract class BaseUseCase<T extends IBaseView> implements IBasePresenter {

    protected final T view;
    private Context context;
    protected final CompositeDisposable compositeDisposable;
    private String TAG = BaseUseCase.class.getSimpleName();

    public BaseUseCase(T view, Context context) {
        this.view = view;
        this.context = context;
        this.compositeDisposable = CompositeDisposableManager.getCompositeDisposable();
        setTAG();
    }

    protected void addToDisposable(Disposable disposable) {
        this.compositeDisposable.add(disposable);
    }

    public void setTAG() {
        this.TAG = getClass().getSimpleName();
    }

    public String getTAG() {
        return TAG;
    }

    protected Context requireContext() {
        if (context == null) {
            throw new IllegalStateException("UseCase " + this + " not attached to a context.");
        } else {
            return context;
        }
    }

    protected void log(String message) {
        LogUtils.log(TAG, message);
    }

    @Override
    public void dispose() {
        if (compositeDisposable != null)
            this.compositeDisposable.dispose();
    }
}
