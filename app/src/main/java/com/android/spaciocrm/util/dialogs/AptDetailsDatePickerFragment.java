package com.android.spaciocrm.util.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.spaciocrm.home.fragments.appointments.FragmentAppointmentDetails;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by tokmang on 26/01/18.
 */

public class AptDetailsDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Activity mActivity;
    Fragment myFragment;
    FragmentAppointmentDetails fragmentAppointmentDetails;

    String strDate;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current date as the default date in the date picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if(getActivity() != null) {
            mActivity = getActivity();
        }

        return new DatePickerDialog(mActivity ,this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        strDate = format.format(calendar.getTime());

        Intent intent = new Intent();
        intent.putExtra("SELECTED_DATE", strDate);

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

    }
}
