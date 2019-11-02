package com.ecdc.erestaurant.utils;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.viewpager.widget.ViewPager;

import com.ecdc.androidlibs.CallBack;
import com.ecdc.erestaurant.R;
import com.ecdc.erestaurant.screens.activity.MainActivity;
import com.ecdc.erestaurant.screens.core.BaseFragment;
import com.ecdc.erestaurant.screens.fragment.AccountFragment;
import com.ecdc.erestaurant.screens.fragment.MapFragment;
import com.ecdc.erestaurant.screens.fragment.NotificationFragment;
import com.ecdc.erestaurant.screens.fragment.ServingFragment;
import com.ecdc.erestaurant.screens.fragment.StatisticFragment;

import java.util.LinkedHashMap;

import me.imstudio.core.ui.pager.NoneSwipeAbleViewPager;
import me.imstudio.core.ui.pager.ViewPagerAdapter;
import me.imstudio.core.ui.pager.transformer.FadePageTransformer;
import me.imstudio.core.ui.widget.TabLayout;

public enum TabBuilder
{
    INSTANCE;

    private CallBack.With<Integer> onPageChangeCallBack;

    private LinkedHashMap<Integer, Pair<Pair<Integer, String>, BaseFragment>> mViewHolders;


    public void buildMainTab(@NonNull MainActivity activity, @NonNull NoneSwipeAbleViewPager viewPager, @NonNull TabLayout tabLayout) {
        try {
            mViewHolders = new LinkedHashMap<>();
            mViewHolders.put(0, new Pair<>(
                    new Pair<>(R.drawable.all_style_tab_serving, activity.getString(R.string.name_tab_1)),
                    new ServingFragment()));
            mViewHolders.put(1, new Pair<>(
                    new Pair<>(R.drawable.all_style_tab_map, activity.getString(R.string.name_tab_2)),
                    new MapFragment()));
            mViewHolders.put(2, new Pair<>(
                    new Pair<>(R.drawable.all_style_tab_statistic, activity.getString(R.string.name_tab_3)),
                    new StatisticFragment()));
            mViewHolders.put(3, new Pair<>(
                    new Pair<>(R.drawable.all_style_tab_notification, activity.getString(R.string.name_tab_4)),
                    new NotificationFragment()));
            mViewHolders.put(4, new Pair<>(
                    new Pair<>(R.drawable.all_style_tab_account, activity.getString(R.string.name_tab_5)),
                    new AccountFragment()));

            ViewPagerAdapter adapter = new ViewPagerAdapter(activity.getSupportFragmentManager());
            for (Integer key : mViewHolders.keySet())
                adapter.addFrag(key, mViewHolders.get(key).second);
            viewPager.setAdapter(adapter);
            //viewPager.setPageTransformer(false, new FadePageTransformer());
            viewPager.setOffscreenPageLimit(adapter.getCount());
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setSmoothScrollingEnabled(true);
            viewPager.addOnPageChangeListener(onPageChangeListener);
            for (Integer key : mViewHolders.keySet()) {
                final TabLayout.Tab tab = tabLayout.getTabAt(key);
                if (tab != null)
                    inflaterTab(activity, mViewHolders.get(key).first.second,
                            mViewHolders.get(key).first.first, view -> tab.setCustomView(view));
                else
                    inflaterTab(activity, mViewHolders.get(key).first.second,
                            mViewHolders.get(key).first.first, view -> tabLayout.addTab(tabLayout.newTab().setCustomView(view)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setOnPageChangeListener(CallBack.With<Integer> onPageChangeListener) {
        onPageChangeCallBack = onPageChangeListener;
    }

    public String getNameByPosition(int selectedPosition) {
        try {
            if (mViewHolders.containsKey(selectedPosition) && mViewHolders.get(selectedPosition) != null)
                return mViewHolders.get(selectedPosition).first.second;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (onPageChangeCallBack != null)
                onPageChangeCallBack.run(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    // [START] TAB BUILDER
    public void inflaterTab(Context context, final String title, final int drawable, final CallBack.With<View> callBack) {
        AsyncLayoutInflater inflater = new AsyncLayoutInflater(context);
        inflater.inflate(R.layout.layout_item_tab, null, (view, resId, parent) -> {
            ImageView imageView = view.findViewById(R.id.image_view);
            imageView.setImageResource(drawable);
            view.<TextView>findViewById(R.id.text_view).setTextColor(context.getResources().getColor(R.color.colorTextColorSecondary));
            view.<TextView>findViewById(R.id.text_view).setText(title);
            view.setLayoutParams(new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            view.setPadding(0, 0, 0, 0);
            callBack.run(view);
        });
    }

    public void addOnTabSelectedListener(TabLayout tabLayout, final CallBack.With<Integer> callBack) {
        tabLayout.addOnTabSelectedListener(new com.google.android.material.tabs.TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final com.google.android.material.tabs.TabLayout.Tab tab) {
                callBack.run(tab.getPosition());
            }
            @Override
            public void onTabUnselected(com.google.android.material.tabs.TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(com.google.android.material.tabs.TabLayout.Tab tab) {
            }
        });
    }
}
