package com.doomedforfailure.todolist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by aaroncampbell on 12/14/16.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    static String formattedDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //use the current date as the default date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        TextView tv1= (TextView) getActivity().findViewById(R.id.pick_date);
        formattedDate = ("Due Date: "+(view.getMonth()+1)+"/"+view.getDayOfMonth()+"/"+view.getYear());
        tv1.setText(formattedDate);
    }
}
