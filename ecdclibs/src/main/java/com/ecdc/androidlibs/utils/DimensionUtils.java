package com.ecdc.androidlibs.utils;

import android.content.Context;

public final class DimensionUtils {

    public static int getScreenHeight(Context paramContext) {
        return paramContext.getResources().getDisplayMetrics().heightPixels;
    }
    public static int getScreenWidth(Context paramContext) {
        return paramContext.getResources().getDisplayMetrics().widthPixels;
    }
}
