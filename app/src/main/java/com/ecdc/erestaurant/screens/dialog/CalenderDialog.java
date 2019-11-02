package com.ecdc.erestaurant.screens.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.ecdc.androidlibs.CallBack;
import com.ecdc.androidlibs.database.entity.TimeLine;
import com.ecdc.erestaurant.R;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalenderDialog extends DialogFragment {

    public static final String TAG = CalenderDialog.class.getSimpleName();
    public static final String START="start_date";
    public static final String END="end_date";

    private CallBack.With<TimeLine> callback;

    @BindView(R.id.calendar)
    CalendarView calendarView;

    private long startDate;
    private long endDate = 0;

    @Override
    public void onStart(){
        super.onStart();
        try {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        View view=View.inflate(getContext(), R.layout.dialog_calender,null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        ButterKnife.bind(this, view);

        calendarView.setWeekendDays(new HashSet() {{
            add(Calendar.SATURDAY);
            add(Calendar.SUNDAY);
        }});
        calendarView.setSelectionType(SelectionType.RANGE);

        return dialog;
    }

    @OnClick(R.id.btn_confirm_date)
    public void onDone() {
        List<Calendar> selectedDates = calendarView.getSelectedDates();
        if (selectedDates != null && selectedDates.size() >= 1) {
            Calendar start = selectedDates.get(0);
            if (start != null) {
                startDate = getStartDate(start.get(Calendar.DAY_OF_MONTH),
                        start.get(Calendar.MONTH),
                        start.get(Calendar.YEAR));
                Log.e("START_DATE: ", Long.toString(startDate));
            }
            Calendar end = selectedDates.get(selectedDates.size() - 1);
            if (end != null && !end.equals(start)) {
                endDate = getEndDate(end.get(Calendar.DAY_OF_MONTH),
                        end.get(Calendar.MONTH),
                        end.get(Calendar.YEAR));
                Log.e("END_DATE: ", Long.toString(endDate));
            }

        }
        if(startDate == 0 && endDate == 0){
            Toast.makeText(getContext(),"Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
        }else {
            TimeLine timeLine = new TimeLine(startDate, endDate);
            startDate=0;
            endDate=0;
            //Confirm chose calender
            if (callback != null) {
                callback.run(timeLine);
            }
            this.dismiss();
        }
    }
   @OnClick(R.id.btn_close)
    @Override
    public void dismiss()
    {
        super.dismiss();
    }

    private long getStartDate(int dayOfMonth, int month, int year)
    {
        Calendar start = Calendar.getInstance();
        start.set(year, month, dayOfMonth,0,0,0);
        return start.getTimeInMillis();
    }

    private long getEndDate(int dayOfMonth, int month, int year)
    {
        Calendar end = Calendar.getInstance();
        end.set(year, month, dayOfMonth,23,59,59);
        return end.getTimeInMillis();
    }

    public void addOnDoneListener(CallBack.With<TimeLine> callback){
        this.callback = callback;
    }

}
