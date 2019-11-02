package com.ecdc.erestaurant.screens.activity;

import androidx.appcompat.app.AppCompatDelegate;

import com.ecdc.erestaurant.R;
import com.ecdc.erestaurant.screens.core.BaseActivity;
import com.ecdc.erestaurant.utils.TabBuilder;

import butterknife.BindView;
import me.imstudio.core.ui.pager.NoneSwipeAbleViewPager;
import me.imstudio.core.ui.widget.TabLayout;

public class MainActivity extends BaseActivity {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @BindView(R.id.view_pager)
    NoneSwipeAbleViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    public static MainActivity instance;

    public static int status = 0;

    @Override
    public int getLayout()
    {
        return R.layout.layout_main_activity;
    }

    @Override
    public void onSyncViews()
    {
        super.onSyncViews();
        TabBuilder.INSTANCE.buildMainTab(this, viewPager, tabLayout);

    }

    @Override
    public void onSyncEvents()
    {
        super.onSyncEvents();
        TabBuilder.INSTANCE.setOnPageChangeListener(integer -> viewPager.setCurrentItem(integer));
    }

    @Override
    public void onSyncData()
    {
        super.onSyncData();
        instance = this;
    }
    @Override
    public void onBackPressed()
    {
        if (viewPager.getCurrentItem() - 1 < 0)
            super.onBackPressed();
        else
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1, true);
    }

    public void scrollToPage(int page)
    {
        if (page>=0 && page<=4) {
            if(page == 2){
                status = 1;
            }
            viewPager.setCurrentItem(page, true);
        }
    }

    public int getStatusResume(){
        return status;
    }

    public void setStatusResume(int mstatus){
      status = mstatus;
    }
}
