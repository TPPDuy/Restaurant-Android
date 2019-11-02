package com.ecdc.erestaurant.screens.fragment;

import android.view.View;
import android.widget.TextView;

import com.ecdc.androidlibs.database.CacheProvider;
import com.ecdc.androidlibs.database.entity.User;
import com.ecdc.erestaurant.R;
import com.ecdc.erestaurant.screens.activity.LoginActivity;
import com.ecdc.erestaurant.screens.core.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class AccountFragment extends BaseFragment {

    @BindView(R.id.user_name)
    TextView userName;

    @Override
    public int getLayout()
    {
        return R.layout.layout_account_fragment;
    }

    @Override
    public void onSyncViews(View view)
    {
        super.onSyncViews(view);
        User user = CacheProvider.self().getUser();
        if (user!=null)
            userName.setText(user.getFullName().isEmpty() ? user.getUserName() : user.getFullName());
    }

    @OnClick(R.id.btn_log_out)
    public void onLogout()
    {
        getOwnActivity().showError("Bạn chắc chắn muốn đăng xuất?", () -> {
            CacheProvider.self().logout();
            LoginActivity.start(requireContext());
            getOwnActivity().finish();
        });
    }
}