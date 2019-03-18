package com.android.spaciocrm.home.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.spaciocrm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPreferences extends DialogFragment {

    EditText txtUsername;
    EditText txtPassword;
    EditText txtWebServicesUrl;
    CheckBox cbSms;
    CheckBox cbEmail;
    CheckBox cbCalendar;
    Spinner spnDayBefore;
    CheckBox cbEnableCallMonitoring;
    Dialog d;

    String selectedDayBefore;

    JSONArray prefsArr;
    JSONObject prefsObj;

    JSONArray savePrefsArr;

    String usersData;
    String preferenceData;
    String activeUser;
    String prefPass;
    String PrefWebUrl;
    String smsSelected;
    String emailSelected;
    String calendarSelected;
    String daysBefore;
    String callMonitoring;



    public FragmentPreferences() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
    }

    @Override
    public void onStart() {
        super.onStart();
        d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_preferences, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.tbPreferences);
        txtUsername = rootView.findViewById(R.id.txtUsername);
        txtPassword = rootView.findViewById(R.id.txtPassword);
        txtWebServicesUrl = rootView.findViewById(R.id.txtWebservicesUrl);

        cbSms = rootView.findViewById(R.id.cbSms);
        cbEmail = rootView.findViewById(R.id.cbEmail);
        cbCalendar = rootView.findViewById(R.id.cbCalendar);
        cbEnableCallMonitoring = rootView.findViewById(R.id.cbEnableCallMonitoring);

        spnDayBefore = rootView.findViewById(R.id.spnDayBefore);

        Button btnSave = rootView.findViewById(R.id.btnSavePreferences);

        SharedPreferences getPreferences = getActivity().getSharedPreferences("PREFPREFERENCES", Context.MODE_PRIVATE);
        preferenceData = getPreferences.getString("preferences", "[]");

        if(!preferenceData.equals("[]")) {

            try {

                prefsArr = new JSONArray(preferenceData);
                prefsObj = prefsArr.getJSONObject(0);

                activeUser = prefsObj.getString("PREFUSERNAME");
                prefPass = prefsObj.getString("PREFPASSWORD");
                PrefWebUrl = prefsObj.getString("PREFWEBSERVICESURL");
                smsSelected = prefsObj.getString("PREFSMSSELECTED");
                emailSelected = prefsObj.getString("PREFEMAILSELECTED");
                calendarSelected = prefsObj.getString("PREFCALENDARSELECTED");
                daysBefore = prefsObj.getString("PREFDAYSBEFORE");
                callMonitoring = prefsObj.getString("PREFCALLMONITORING");

                txtUsername.setText(activeUser);
                txtPassword.setText(prefPass);
                txtWebServicesUrl.setText(PrefWebUrl);

                if(!smsSelected.equals("")) {
                    cbSms.setChecked(true);
                }

                if(!emailSelected.equals("")) {
                    cbEmail.setChecked(true);
                }

                if(!calendarSelected.equals("")) {
                    cbCalendar.setChecked(true);
                }

                if(!callMonitoring.equals("")) {
                    cbEnableCallMonitoring.setChecked(true);
                }

                if(!daysBefore.equals("")) {

                    String[] daysBack = getResources().getStringArray(R.array.days_before_task);
                    int pos = Arrays.asList(daysBack).indexOf(daysBefore) + 1;

                    spnDayBefore.setSelection(pos, true);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {

            SharedPreferences getActiveUser = getActivity().getSharedPreferences("PREFACTIVEUSER", Context.MODE_PRIVATE);
            activeUser = getActiveUser.getString("activeUser", "");

            txtUsername.setText(activeUser);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.cancel();
            }
        });

        spnDayBefore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDayBefore = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject prefssObj = new JSONObject();
                savePrefsArr = new JSONArray();

                String smsChecked = "";
                String emailChecked = "";
                String calendarChecked = "";
                String callMonitoring = "";

                try {

                    if(cbSms.isChecked()) {
                        smsChecked = (String)cbSms.getText();
                    }

                    if(cbEmail.isChecked()) {
                        emailChecked = (String)cbEmail.getText();
                    }

                    if(cbCalendar.isChecked()) {
                        calendarChecked = (String)cbCalendar.getText();
                    }

                    if(cbEnableCallMonitoring.isChecked()) {
                        callMonitoring = (String)cbEnableCallMonitoring.getText();
                    }

                    prefssObj.put("PREFUSERNAME", txtUsername.getText());
                    prefssObj.put("PREFPASSWORD", txtPassword.getText());
                    prefssObj.put("PREFWEBSERVICESURL", txtWebServicesUrl.getText());
                    prefssObj.put("PREFSMSSELECTED", smsChecked);
                    prefssObj.put("PREFEMAILSELECTED", emailChecked);
                    prefssObj.put("PREFCALENDARSELECTED", calendarChecked);
                    prefssObj.put("PREFDAYSBEFORE", selectedDayBefore);
                    prefssObj.put("PREFCALLMONITORING", callMonitoring);

                    savePrefsArr.put(prefssObj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferences prefPref = getActivity().getSharedPreferences("PREFPREFERENCES", Context.MODE_PRIVATE);
                SharedPreferences.Editor listEditor = prefPref.edit();
                listEditor.putString("preferences", savePrefsArr.toString());
                listEditor.apply();

                Toast.makeText(getActivity(), "Preference Updated Successfully", Toast.LENGTH_LONG).show();

                Dismiss();

            }
        });

        return rootView;
    }

    public void Dismiss() {
        DialogFragment dialogFragment = (DialogFragment)getFragmentManager().findFragmentByTag("PreferenceFragment");
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }

}
