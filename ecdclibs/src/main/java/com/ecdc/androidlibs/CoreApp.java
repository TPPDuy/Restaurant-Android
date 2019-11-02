package com.ecdc.androidlibs;

import android.app.Application;

import com.ecdc.androidlibs.database.CacheProvider;
import com.imstudio.core.imageloader.GlideLoader;
import com.imstudio.core.imageloader.ImageLoader;

import me.imstudio.core.utils.CompressUtils;
import me.imstudio.core.utils.LocaleUtils;
import me.imstudio.core.utils.LogUtils;

public class CoreApp extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();
        CompressUtils.ZIP.unpackFromAsset(this, "fonts.zip");
        CacheProvider.of(this);
        LocaleUtils.install(this, LocaleUtils.DEFAULT_LANGUAGE_VI);
        LogUtils.setDebug(BuildConfig.DEBUG);
        ImageLoader.plug().setImageLoader(new GlideLoader());
    }
}
