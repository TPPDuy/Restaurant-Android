package com.ecdc.erestaurant.config;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.ecdc.androidlibs.CoreApp;
import com.ecdc.erestaurant.di.component.ApplicationComponent;
import com.ecdc.erestaurant.di.component.DaggerApplicationComponent;
import com.ecdc.erestaurant.di.module.ContextModule;
import com.ecdc.erestaurant.di.module.NetworkModule;

public class ERestaurant extends CoreApp {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initApplicationComponents();
    }

    public static ERestaurant self(Context context) {
        return (ERestaurant) context.getApplicationContext();
    }

    private void initApplicationComponents() {
        applicationComponent = DaggerApplicationComponent.builder()
                .contextModule(new ContextModule(getApplicationContext()))
                .networkModule(new NetworkModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public Bitmap loadBitmapFromView(View view, int width, int height) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);

        Log.e("width", "=" + width);
        Log.e("height","="+height);
        return returnedBitmap;
    }

}
