package com.android.spaciocrm.util.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.android.spaciocrm.home.fragments.appointments.FragmentAppointmentDetails;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by tokmang on 26/01/18.
 */

public class AptDetailsTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    Activity mActivity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current date as the default date in the date picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        if(getActivity() != null) {
            mActivity = getActivity();
        }


        return new TimePickerDialog(mActivity, this, hour, min, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String strTime = String.valueOf(hourOfDay)+":"+String.valueOf(minute);

        Intent intent = new Intent();
        intent.putExtra("SELECTED_TIME", strTime);

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

    }
}
