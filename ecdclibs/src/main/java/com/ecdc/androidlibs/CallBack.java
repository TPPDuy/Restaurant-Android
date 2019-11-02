package com.ecdc.androidlibs;

public interface CallBack {

    void run();

    interface With<T> {
        void run(T t);
    }

    interface WithPair<T, V> {
        void run(T t, V v);
    }

    interface Request<T, V> {

        void onSuccessful(T data);

        void onFailure(V error);

    }

    interface WithPairBonus<S, T, V> {
        void run(S s, T t, V v);
    }

}

