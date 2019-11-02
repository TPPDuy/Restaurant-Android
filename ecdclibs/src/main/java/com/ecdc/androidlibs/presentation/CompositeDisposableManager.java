package com.ecdc.androidlibs.presentation;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public final class CompositeDisposableManager {

    private static CompositeDisposable compositeDisposable;

    private CompositeDisposableManager() {

    }

    public static void add(Disposable disposable) {
        getCompositeDisposable().add(disposable);
    }

    protected static CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null || compositeDisposable.isDisposed())
            compositeDisposable = new CompositeDisposable();
        return compositeDisposable;
    }
}