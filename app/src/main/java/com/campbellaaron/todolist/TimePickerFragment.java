package com.campbellaaron.todolist;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by aaroncampbell on 12/14/16.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    String formattedTime;
    String amPm;
    String separator = ":";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (hourOfDay == 0) {
            hourOfDay += 12;
            amPm = "AM";
        }
        else if (hourOfDay == 12) {
            amPm = "PM";
        } else if (hourOfDay > 12) {
            hourOfDay -= 12;
            amPm = "PM";
        } else {
            amPm = "AM";
        }
        if (minute < 10){
            separator = ":0";
        }
        TextView formattedTime = (TextView) getActivity().findViewById(R.id.post_time);
        formattedTime.clearFocus();
        formattedTime.setText("Due Time: " + "\n" + hourOfDay + separator + minute + " " + amPm);
    }
}