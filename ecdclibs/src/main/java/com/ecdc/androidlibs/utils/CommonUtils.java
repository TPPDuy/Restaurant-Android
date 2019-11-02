package com.ecdc.androidlibs.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.ecdc.androidlibs.CallBack;
import com.ecdc.androidlibs.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import me.imstudio.core.utils.Utils;


public final class CommonUtils {

    public static void openStore(Activity activity) {
        final String appPackageName = activity.getPackageName();
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (Exception e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private static HashMap<String, Integer> drawableMapping;

    static {
        drawableMapping = new HashMap<>();
    }

    public static Drawable getDrawableFromFileName(Context context, String name) {
        return Utils.getDrawable(context, getResFromFileName(context, name));
    }

    public static int getResFromFileName(Context context, String name) {
        try {
            if (drawableMapping.containsKey(name))
                return drawableMapping.get(name);
            int res = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
            if (res != -1 && res != 0) {
                drawableMapping.put(name, res);
                return res;
            }
            return R.drawable.ic_image_holder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void share(Activity activity, String title) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=" + activity.getPackageName());
        activity.startActivity(Intent.createChooser(shareIntent, title));
    }

    public static String formatWithComma(long price) {
        String strPrice = "0";
        if (price < 1000)
            strPrice = String.valueOf(price);
        try {
            NumberFormat formatter = new DecimalFormat("###,###");
            strPrice = formatter.format(price);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strPrice;
    }

    public static void setColor(TextView view, String fullText, String subText, int color) {
        if (TextUtils.isEmpty(subText)) {
            view.setText(fullText);
        } else {
            try {
                if (!TextUtils.isEmpty(fullText)) {
                    view.setText(fullText, TextView.BufferType.SPANNABLE);
                    Spannable str = (Spannable) view.getText();
                    int index = fullText.toLowerCase().indexOf(subText.toLowerCase());
                    if (index >= 0)
                        str.setSpan(new ForegroundColorSpan(color), index, index + subText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    else
                        view.setText(fullText);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static long toTimestamp(int year, int month, int day) {
        try {
            String strDate = String.format("%s-%s-%s", String.valueOf(day), String.valueOf(month), String.valueOf(year));
            return (new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(strDate)).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void delay(final CallBack callBack, int mills) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callBack.run();
            }
        }, mills);
    }

    public static String getVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            return info.versionName;
        }
        return null;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getActiveNetworkInfo() != null);
    }
}
