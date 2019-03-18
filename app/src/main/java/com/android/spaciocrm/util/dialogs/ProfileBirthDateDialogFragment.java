package com.android.spaciocrm.util.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import com.android.spaciocrm.home.fragments.FragmentProfile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by freshfuturesmy on 13/02/18.
 */

public class ProfileBirthDateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Activity mActivity;
    Fragment myFragment;
    FragmentProfile fragmentProfile;

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
        myFragment = getActivity().getSupportFragmentManager().findFragmentByTag("ProfileFragment");

        if(myFragment != null) {
            fragmentProfile = (FragmentProfile) myFragment;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate = format.format(calendar.getTime());

        fragmentProfile.setSelectedDate(strDate);
    }
}
