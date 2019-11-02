package com.ecdc.erestaurant.screens.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;

import com.ecdc.androidlibs.CallBack;
import com.ecdc.erestaurant.R;
import com.tuyenmonkey.mkloader.MKLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeTableStatusDialog extends DialogFragment {

    public static final String TAG = ChangeTableStatusDialog.class.getSimpleName();

    private CallBack.With<Integer> callBack;
    private int selectedStatus;
    private int originStatus;

    @BindView(R.id.layout_holder_empty_status)
    LinearLayout layoutEmptyStatus;
    @BindView(R.id.layout_holder_serving_status)
    LinearLayout layoutServingStatus;
    @BindView(R.id.layout_holder_reserved_status)
    LinearLayout layoutReservedStatus;

    @BindView(R.id.check_box_empty_status)
    AppCompatImageView checkBoxEmptyStatus;
    @BindView(R.id.check_box_serving_status)
    AppCompatImageView checkBoxServingStatus;
    @BindView(R.id.check_box_reserved_status)
    AppCompatImageView checkBoxReservedStatus;

    @BindView(R.id.text_empty_status)
    TextView textEmptyStatus;
    @BindView(R.id.text_serving_status)
    TextView textServingStatus;
    @BindView(R.id.text_reserved_status)
    TextView textReservedStatus;
    @BindView(R.id.ic_loader)
    MKLoader icLoader;

    @Override
    public void onStart() {
        super.onStart();
        try {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.dialog_table_status, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        ButterKnife.bind(this, view);

        if (originStatus == 2)
        {
            layoutEmptyStatus.setClickable(false);
            layoutReservedStatus.setClickable(false);
        }

        setSelectedStatusView();
        return dialog;
    }

    @OnClick({R.id.layout_holder_empty_status, R.id.layout_holder_serving_status, R.id.layout_holder_reserved_status})
    public void onChooseStatus(View view)
    {
        int id = view.getId();
        switch (id)
        {
            case R.id.layout_holder_empty_status:
                selectedStatus = 1;
                break;
            case R.id.layout_holder_serving_status:
                selectedStatus = 2;
                break;
            case R.id.layout_holder_reserved_status:
                selectedStatus = 3;
                break;
        }
        setSelectedStatusView();
    }

    @OnClick(R.id.btn_dismiss)
    @Override
    public void dismiss()
    {
        super.dismiss();
    }

    @OnClick(R.id.btn_confirm)
    public void onConfirm()
    {
        icLoader.setVisibility(View.VISIBLE);
        (new Handler()).postDelayed(() -> {
            if (originStatus == selectedStatus)
                callBack.run(0);
            else
                callBack.run(selectedStatus);
            dismiss();
        }, 500);

    }

    public void setStatus(int status)
    {
        if (status>=1 && status <=3)
            originStatus = status;
        selectedStatus = originStatus;
    }

    private void onSyncViews()
    {
        layoutEmptyStatus.setBackground(requireContext().getDrawable(R.drawable.box_background));
        layoutServingStatus.setBackground(requireContext().getDrawable(R.drawable.box_background));
        layoutReservedStatus.setBackground(requireContext().getDrawable(R.drawable.box_background));

        checkBoxEmptyStatus.setImageResource(R.drawable.all_style_checkbox_normal);
        checkBoxServingStatus.setImageResource(R.drawable.all_style_checkbox_normal);
        checkBoxReservedStatus.setImageResource(R.drawable.all_style_checkbox_normal);

        textEmptyStatus.setTextColor(getResources().getColor(R.color.colorTextPrimary));
        textServingStatus.setTextColor(getResources().getColor(R.color.colorTextPrimary));
        textReservedStatus.setTextColor(getResources().getColor(R.color.colorTextPrimary));
    }

    private void setSelectedStatusView()
    {
        onSyncViews();
        switch (selectedStatus)
        {
            case 1:
                layoutEmptyStatus.setBackground(requireContext().getDrawable(R.drawable.selected_box_background));
                checkBoxEmptyStatus.setImageResource(R.drawable.all_style_checkbox_selected);
                textEmptyStatus.setTextColor(getResources().getColor(R.color.colorJade));
                break;
            case 2:
                layoutServingStatus.setBackground(requireContext().getDrawable(R.drawable.selected_box_background));
                checkBoxServingStatus.setImageResource(R.drawable.all_style_checkbox_selected);
                textServingStatus.setTextColor(getResources().getColor(R.color.colorJade));
                break;
            case 3:
                layoutReservedStatus.setBackground(requireContext().getDrawable(R.drawable.selected_box_background));
                checkBoxReservedStatus.setImageResource(R.drawable.all_style_checkbox_selected);
                textReservedStatus.setTextColor(getResources().getColor(R.color.colorJade));
                break;
        }
    }

    public void addOnDoneListener(CallBack.With<Integer> callBack)
    {
        this.callBack = callBack;
    }
}
