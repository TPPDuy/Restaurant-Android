package com.ecdc.erestaurant.screens.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.ecdc.androidlibs.CallBack;
import com.ecdc.androidlibs.database.entity.Product;
import com.ecdc.erestaurant.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductOptionDialog extends DialogFragment {
    public static final String TAG = ProductOptionDialog.class.getSimpleName();
    @BindView(R.id.quantity)
    EditText quantity;
    @BindView(R.id.text_unit)
    TextView textUnit;
    @BindView(R.id.note)
    EditText note;

    private CallBack.With<Product> callBack;
    private Product product;

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
        View view = View.inflate(getContext(), R.layout.dialog_note_product, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        ButterKnife.bind(this, view);
        setViews();
        return dialog;
    }

    @OnClick({R.id.btn_cancel, R.id.btn_close})
    @Override
    public void dismiss()
    {
        hideKeyboard();
        super.dismiss();
    }

    @OnClick({R.id.btn_accept})
    public void OnDone()
    {
        if (quantity.getText().toString().isEmpty())
            Toast.makeText(requireContext(), "Vui lòng nhập số lượng", Toast.LENGTH_SHORT).show();
        else {
            product.setQuantity(Integer.parseInt(quantity.getText().toString()));
            product.setNote(note.getText().toString());

            if (callBack != null)
                callBack.run(product);
            this.dismiss();
        }
    }

    @OnClick({R.id.btn_decrease, R.id.btn_increase})
    public void onChangeQuantity(View view)
    {
        int num = Integer.valueOf(quantity.getText().toString());
        if (view.getId() == R.id.btn_decrease)
        {
            if (num>0)
                num--;
        }
        else
            num++;
        quantity.setText(String.format(Locale.getDefault(), "%d", num));
    }

    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager)requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(note.getWindowToken(), 0);
    }
    public void setInfoToDialog(Product product)
    {
        this.product = product;
    }

    public void setViews()
    {
        String realUnit;
        if (product.getUnit().equalsIgnoreCase("disc"))
            realUnit = "phần";
        else
            realUnit = product.getUnit();
        textUnit.setText(String.format("/ %s", realUnit));
        quantity.setText(String.format(Locale.getDefault(), "%d", product.getQuantity()));
        if (product.getNote()!=null && !product.getNote().isEmpty())
            note.setText(product.getNote());

        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==1 && !Character.isDigit(s.charAt(0)))
                    quantity.setText("0");
                if (s.length()>1 && !Character.isDigit(s.charAt(s.length()-1)))
                    quantity.setText(s.subSequence(0, s.length()-1));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void addOnDoneListener(CallBack.With<Product> callBack)
    {
        this.callBack = callBack;
    }
}
