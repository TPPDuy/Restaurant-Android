package com.ecdc.androidlibs.presentation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ecdc.androidlibs.R;


public final class ProgressDialog extends Dialog {

    private Activity mOwnerActivity;

    public ProgressDialog(Context context) {
        super(context, R.style.CoreTheme_Dialog);
        mOwnerActivity = (context instanceof Activity) ? (Activity) context : null;
        if (mOwnerActivity != null)
            setOwnerActivity(mOwnerActivity);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mOwnerActivity = getOwnerActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.windowAnimations = R.style.CoreTheme_AnimDialog_Fade;
            window.setAttributes(params);
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }
}