package com.ecdc.androidlibs.presentation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ecdc.androidlibs.R;
import com.ecdc.androidlibs.utils.DimensionUtils;


public final class ErrorDialog extends Dialog {

    private TextView textView = null;
    private Activity mOwnerActivity;
    private CoreCallBack mClickListener = null, mDismissListener = null;

    public ErrorDialog(Context context) {
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
        setContentView(R.layout.dialog_error);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            //params.windowAnimations = R.style.CoreTheme_AnimDialog_Fade;
            params.width = DimensionUtils.getScreenWidth(mOwnerActivity) * 8 / 10;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null)
                    mClickListener.run();
                dismiss();
            }
        });
        textView = findViewById(R.id.text_view);
        findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDismissListener != null)
                    mDismissListener.run();
                dismiss();
            }
        });
    }

    public void show(String title, CoreCallBack onConfirmClickListener) {
        super.show();
        textView.setText(title);
        mClickListener = onConfirmClickListener;
    }

    public void setDismissListener(CoreCallBack dismissListener) {
        mDismissListener = dismissListener;
    }
}