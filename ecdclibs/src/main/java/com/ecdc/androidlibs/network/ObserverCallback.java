package com.ecdc.androidlibs.network;

import com.ecdc.androidlibs.presentation.IBaseView;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public abstract class ObserverCallback<T extends CoreResponse> implements Observer<T> {

    private WeakReference<IBaseView> weakReference;

    public ObserverCallback(IBaseView baseView) {
        this.weakReference = new WeakReference<>(baseView);
    }

    protected abstract void onSuccessful(T t);

    @Override
    public void onNext(T t) {
        if (t.isSuccessful())
            onSuccessful(t);
        else {
            IBaseView view = weakReference.get();
            view.onFailure(t.errorMessage());
        }
    }

    @Override
    public void onError(Throwable throwable) {
        IBaseView view = weakReference.get();
        if (throwable instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
            view.onFailure(getErrorMessage(responseBody));
        } else if (throwable instanceof SocketTimeoutException) {
            view.onTimeout();
        } else {
            view.onFailure(throwable.getMessage());
        }
    }

    @Override
    public void onComplete() {

    }

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}