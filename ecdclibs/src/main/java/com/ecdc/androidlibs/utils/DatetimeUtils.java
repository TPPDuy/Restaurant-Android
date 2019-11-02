package com.ecdc.androidlibs.utils;

import android.content.Context;
import android.text.format.DateUtils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DatetimeUtils {

    public static String formatDateTime(Context context, long millis) {
        return DateUtils.formatDateTime(context, millis, DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_ABBREV_ALL);
    }

    public static String formatType1(long millis) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault());
        Date date = new Date(millis);
        try {
            return originalFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatType3(long millis) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
        Date date = new Date(millis);
        try {
            return originalFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatType2(long millis) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date(millis);
        try {
            return originalFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatType7(long millis) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
        Date date = new Date(millis);
        try {
            return originalFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String formatType4(long millis) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date date = new Date(millis);
        try {
            return originalFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatType5(long millis) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("EEE, dd/MM/yyyy", Locale.getDefault());
        Date date = new Date(millis);
        try {
            return originalFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatType6(long millis) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy\nHH:mm", Locale.getDefault());
        Date date = new Date(millis);
        try {
            return originalFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatType8(long millis) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
        Date date = new Date(millis);
        try {
            return originalFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String formatType9(long date1, long date2)
    {
        return formatType2(date1) + '\n' + formatType2(date2);
    }
    public static String getDayOfWeek(long millis) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        Date date = new Date(millis);
        try {
            return originalFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static long getStartOfDateInSecond(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar.getTimeInMillis() / 1000;
    }

    public static long getEndOfDateInSecond(long date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        return calendar.getTimeInMillis() / 1000;
    }

    public static long getTomorrow(long date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTimeInMillis();
    }
    public static String getTime(long second)
    {
        if(second<0) return "";

        int hours = (int)second/3600;
        int minutes = (int)(second%3600)/60;
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setMinimumIntegerDigits(2);
        return numberFormat.format(hours) + ":" + numberFormat.format(minutes);
    }
}
