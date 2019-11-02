package com.ecdc.erestaurant.screens.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ecdc.androidlibs.database.CacheProvider;
import com.ecdc.androidlibs.network.NetworkProvider;
import com.ecdc.erestaurant.R;
import com.ecdc.erestaurant.config.ERestaurant;
import com.ecdc.erestaurant.screens.core.BaseActivity;
import com.ecdc.erestaurant.usecases.LoginUseCase;
import com.ecdc.erestaurant.usecases.LoginUseCaseImpl;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public final class LoginActivity extends BaseActivity<LoginUseCase.Presenter> implements LoginUseCase.View {

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @BindView(R.id.edit_user_name)
    EditText editUserName;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.button)
    Button buttonLogin;


    @Inject
    NetworkProvider provider;

    public static boolean isUpdateProfile = false;

    @Override
    public int getLayout() {
        return R.layout.layout_login_activity;
    }

    @Override
    public void onSyncViews() {
        super.onSyncViews();
        ERestaurant.self(this).getApplicationComponent().inject(this);  // DI
        setPresenter(new LoginUseCaseImpl(this, this, provider)); // Set presenter
        if (CacheProvider.self().isLoggedIn())
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onSyncEvents() {
        super.onSyncEvents();
        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty())
                    buttonLogin.setEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable editable) {
                buttonLogin.setEnabled(!TextUtils.isEmpty(editable.toString()));
            }
        });
    }

    @OnClick(R.id.button)
    public void OnWhichViewClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button:
                String userName = editUserName.getText().toString();
                String password = editPassword.getText().toString();
                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
                    presenter.logIn(userName, password);
                }
                break;
        }
    }

    @Override
    public void onLogInSuccessfully()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailure(String errorMessage) {
       if(errorMessage!=null)
           showError(errorMessage);
    }

    @Override
    public void onTimeout() {

    }
}
