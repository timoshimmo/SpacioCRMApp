package com.android.spaciocrm.home.fragments.appointments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.spaciocrm.R;
import com.android.spaciocrm.util.adapter.AppointmentContactsAdapter;
import com.android.spaciocrm.util.adapter.CountryListAdapter;
import com.android.spaciocrm.util.adapter.StateListAdapter;
import com.android.spaciocrm.util.dialogs.AptDetailsDatePickerFragment;
import com.android.spaciocrm.util.dialogs.AptDetailsTimePickerFragment;
import com.android.spaciocrm.util.model.ContactsModel;
import com.android.spaciocrm.util.model.CountryModel;
import com.android.spaciocrm.util.model.DividerItemDecoration;
import com.android.spaciocrm.util.model.StateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FragmentAppointmentDetails extends Fragment implements View.OnClickListener {

    public static AlertDialog ctryAlertDialog;
    public static AlertDialog stateAlertDialog;
    public static AlertDialog contactAlertDialog;
    DialogFragment dateFragment;
    DialogFragment timeFragment;

    private static final String ARG_APPOINTMENT = "appointment_title";
    private static final String ARG_POS = "appointment_pos";

    private String mAppointmentTitle;
    private int mAppointmentPos;

    Context mContext;
    TextView txtTitle;

    EditText txtAptContactName;
    EditText txtAptDate;
    EditText txtAptTime;
    EditText txtAptState;
    EditText txtAptCountry;

    EditText txtAgenda;
    EditText txtOutcome;
    EditText txtStreet;
    EditText txtCity;
    EditText txtAppointmentName;

    CheckBox cbSms;
    CheckBox cbEmail;

    public RelativeLayout btnSetContact;
    public RelativeLayout btnSetDate;
    public RelativeLayout btnSetTime;
    public RelativeLayout btnSetState;
    public RelativeLayout btnSetCtry;

    static ArrayList<CountryModel> countryList = null;
    static ArrayList<StateModel> stateList = null;

    CountryModel ctryModel;
    StateModel stateModel;
    ContactsModel cModel;

    String selectedApts;

    protected ArrayList<ContactsModel> contactsList;

    JSONArray aptsArray;
    JSONObject aptsObj;

    String contacts;
    JSONArray contactsArray;
    JSONObject contactsObj;

    public static final int DATEPICKER_FRAGMENT = 1;
    public static final int TIMEPICKER_FRAGMENT = 2;

    public FragmentAppointmentDetails() {
        // Required empty public constructor
    }


    public static FragmentAppointmentDetails newInstance(String param, int pos) {
        FragmentAppointmentDetails fragment = new FragmentAppointmentDetails();
        Bundle args = new Bundle();
        args.putString(ARG_APPOINTMENT, param);
        args.putInt(ARG_POS, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAppointmentTitle = getArguments().getString(ARG_APPOINTMENT);
            mAppointmentPos = getArguments().getInt(ARG_POS);

        }

        initDataset();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_appointment_details, container, false);

        mContext = getActivity();

        txtTitle = rootView.findViewById(R.id.txtAppointmentTitleName);
        ImageButton btnCancel = rootView.findViewById(R.id.btnCloseNewAppointment);

        txtAptContactName = rootView.findViewById(R.id.txtAptContactName);
        txtAppointmentName = rootView.findViewById(R.id.txtAptTitleValue);
        txtAptDate = rootView.findViewById(R.id.txtAptDateValue);
        txtAptTime = rootView.findViewById(R.id.txtAptTimeValue);
        txtAgenda = rootView.findViewById(R.id.txtAptAgendaValue);
        txtOutcome = rootView.findViewById(R.id.txtAptOutcomeValue);
        txtStreet = rootView.findViewById(R.id.txtAptStreetValue);
        txtCity = rootView.findViewById(R.id.txtAptCityValue);
        txtAptState = rootView.findViewById(R.id.txtAptStateValue);
        txtAptCountry = rootView.findViewById(R.id.txtAptCountryValue);

        ImageButton btnEditAppointment = rootView.findViewById(R.id.btnAppointmentEdit);
        ImageButton btnEditAgenda = rootView.findViewById(R.id.btnAgendaEdit);
        ImageButton btnEditOutcome = rootView.findViewById(R.id.btnOutcomeEdit);
        ImageButton btnEditStreet = rootView.findViewById(R.id.btnStreetEdit);
        ImageButton btnEditCity = rootView.findViewById(R.id.btnCityEdit);

        cbSms = rootView.findViewById(R.id.cbSms);
        cbEmail = rootView.findViewById(R.id.cbEmail);

        btnSetContact = rootView.findViewById(R.id.btnSetAptContactName);
        btnSetDate = rootView.findViewById(R.id.btnSetAptDate);
        btnSetTime= rootView.findViewById(R.id.btnSetAptTime);
        btnSetState= rootView.findViewById(R.id.btnSetAptState);
        btnSetCtry = rootView.findViewById(R.id.btnSetAptCtry);

        Button btnUpdateAppointment = rootView.findViewById(R.id.btnUpdateAppointment);
        txtTitle.setText(mAppointmentTitle);

        SharedPreferences appointmentValues = getActivity().getSharedPreferences("PREFAPTLIST", Context.MODE_PRIVATE);
        selectedApts = appointmentValues.getString("appointmentsData", "[]");

        try {

            aptsArray = new JSONArray(selectedApts);
            aptsObj = aptsArray.getJSONObject(mAppointmentPos);

            txtAptContactName.setText(aptsObj.getString("CONTACT_NAME"));
            txtAppointmentName.setText(aptsObj.getString("EVENTS_TITLE"));
            txtAptDate.setText(aptsObj.getString("DATE_OF_APPOINTMENT"));
            txtAptTime.setText(aptsObj.getString("TIME_OF_APPOINTMENT"));
            txtAgenda.setText(aptsObj.getString("AGENDA_OF_APPOINTMENT"));
            txtOutcome.setText(aptsObj.getString("OUTCOME_OF_MEETING"));
            txtStreet.setText(aptsObj.getString("APPOINTMENT_ADDRESS"));
            txtCity.setText(aptsObj.getString("APPOINTMENT_CITY"));
            txtAptState.setText(aptsObj.getString("APPOINTMENT_STATE"));
            txtAptCountry.setText(aptsObj.getString("APPOINTMENT_COUNTRY"));

            if(aptsObj.getString("SEND_REMINDER_BY").equals("SMS")) {
                cbSms.setChecked(true);
            }

            if(aptsObj.getString("SEND_REMINDER_BY").equals("Email")) {
                cbEmail.setChecked(true);
            }

            if(aptsObj.getString("SEND_REMINDER_BY").equals("SMS|Email")) {
                cbSms.setChecked(true);
                cbEmail.setChecked(true);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getActivity() != null) {
                    getActivity().onBackPressed();
                }

            }
        });

        btnEditAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAppointmentName.setEnabled(true);
                txtAppointmentName.setGravity(Gravity.START);
                txtAppointmentName.requestFocus();
            }
        });

        btnEditAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAgenda.setEnabled(true);
                txtAgenda.requestFocus();
            }
        });

        btnEditOutcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtOutcome.setEnabled(true);
                txtOutcome.requestFocus();
            }
        });

        btnEditStreet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStreet.setEnabled(true);
                txtStreet.setGravity(Gravity.START);
                txtStreet.requestFocus();
            }
        });

        btnEditCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCity.setEnabled(true);
                txtCity.setGravity(Gravity.START);
                txtCity.requestFocus();
            }
        });

        txtAppointmentName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    txtAppointmentName.setGravity(Gravity.END);
                    txtAppointmentName.setEnabled(false);
                    if(!txtAppointmentName.getText().toString().equals("")) {
                        txtAppointmentName.setHint(txtAppointmentName.getText());
                    }

                }
            }
        });

        txtAgenda.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    txtAgenda.setEnabled(false);
                    if(!txtAgenda.getText().toString().equals("")) {
                        txtAgenda.setHint(txtAgenda.getText());
                    }

                }
            }
        });

        txtOutcome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    txtOutcome.setEnabled(false);
                    if(!txtOutcome.getText().toString().equals("")) {
                        txtOutcome.setHint(txtOutcome.getText());
                    }

                }
            }
        });

        txtStreet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    txtStreet.setGravity(Gravity.END);
                    txtStreet.setEnabled(false);
                    if(!txtStreet.getText().toString().equals("")) {
                        txtStreet.setHint(txtStreet.getText());
                    }

                }
            }
        });

        txtCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    txtCity.setGravity(Gravity.END);
                    txtCity.setEnabled(false);
                    if(!txtCity.getText().toString().equals("")) {
                        txtCity.setHint(txtCity.getText());
                    }

                }
            }
        });

        btnUpdateAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtAptContactName.getText().toString().equals("") || txtAppointmentName.getText().toString().equals("") ||
                        txtAptDate.getText().toString().equals("") || txtAptTime.getText().toString().equals("") ||
                        txtAgenda.getText().toString().equals("") || txtOutcome.getText().toString().equals("") ||
                        txtStreet.getText().toString().equals("") || txtCity.getText().toString().equals("") ||
                        txtAptState.getText().toString().equals("") || txtAptCountry.getText().toString().equals("")) {

                    Snackbar.make(rootView, "All Inputs are required!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                else {
                    updateAppointment();
                }
            }
        });

        btnSetDate.setOnClickListener(this);
        btnSetTime.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        countryDialog();
        stateDialog();
        contactDialog();

    }

    private void updateAppointment() {

        try {

            JSONObject aptObj = new JSONObject();

            aptObj.put("EVENTS_TITLE", txtAppointmentName.getText().toString());
            aptObj.put("CONTACT_NAME", txtAptContactName.getText().toString());
            aptObj.put("DATE_OF_APPOINTMENT", txtAptDate.getText().toString());
            aptObj.put("TIME_OF_APPOINTMENT", txtAptTime.getText().toString());
            aptObj.put("AGENDA_OF_APPOINTMENT", txtAgenda.getText().toString());
            aptObj.put("OUTCOME_OF_MEETING", txtOutcome.getText().toString());
            aptObj.put("APPOINTMENT_ADDRESS", txtStreet.getText().toString());
            aptObj.put("APPOINTMENT_CITY", txtCity.getText().toString());
            aptObj.put("APPOINTMENT_STATE", txtAptState.getText().toString());
            aptObj.put("APPOINTMENT_COUNTRY", txtAptCountry.getText().toString());

            if(cbSms.isChecked()  && !cbEmail.isChecked()) {
                aptObj.put("SEND_REMINDER_BY", cbSms.getText().toString());
            }

            if(cbEmail.isChecked() && !cbSms.isChecked()) {
                aptObj.put("SEND_REMINDER_BY", cbEmail.getText().toString());
            }

            if(cbEmail.isChecked() && cbSms.isChecked()) {
                aptObj.put("SEND_REMINDER_BY", cbSms.getText().toString()+"|"+cbEmail.getText().toString());
            }

            if(!cbEmail.isChecked() && cbSms.isChecked()) {
                aptObj.put("SEND_REMINDER_BY", "");
            }


            aptsArray.put(mAppointmentPos, aptObj);

            SharedPreferences prefAptList = getActivity().getSharedPreferences("PREFAPTLIST", Context.MODE_PRIVATE);
            SharedPreferences.Editor listEditor = prefAptList.edit();
            listEditor.putString("appointmentsData", aptsArray.toString());
            listEditor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(getActivity(), "Appointment Successfully Updated!", Toast.LENGTH_LONG).show();
        if(getActivity() != null) {
            getActivity().onBackPressed();
        }

    }

    public String loadCountryJSONFromAsset() {
        String json = null;
        try {
            if(getActivity() != null) {
                InputStream is = getActivity().getAssets().open("country/country_code.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String loadStateJSONFromAsset() {
        String json = null;
        try {

            if(getActivity() != null) {
                InputStream is = getActivity().getAssets().open("state/state_list.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public void countryDialog() {

        btnSetCtry.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        assert (getActivity()) != null;
        LayoutInflater inflaters = (getActivity()).getLayoutInflater();

        View dialogView = inflaters.inflate(R.layout.country_dialog_layout, null);
        builder.setView(dialogView);

        Button btnDialogCancel = dialogView.findViewById(R.id.btnCancelCountry);
        Button btnDialogOk = dialogView.findViewById(R.id.btnConfirmCountry);

        countryList = new ArrayList<>();
        final CountryModel mCModel = new CountryModel();

        try {

            JSONArray obj = new JSONArray(loadCountryJSONFromAsset());

            for (int i = 0; i < obj.length(); i++) {

                JSONObject currObj = obj.getJSONObject(i);

                String countryName = currObj.getString("COUNTRY_TXT");
                String countryCode = currObj.getString("COUNTRY_CODE");

                ctryModel = new CountryModel(countryName, countryCode);

                countryList.add(ctryModel);

            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        final RecyclerView recyclerView = dialogView.findViewById(R.id.rvCountry);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        CountryListAdapter adapter = new CountryListAdapter(mCModel, countryList);
        recyclerView.setAdapter(adapter);

        ctryAlertDialog = builder.create();

        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctryAlertDialog.cancel();
            }
        });

        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = mCModel.getCtryName();
                txtAptCountry.setHint(result);

                ctryAlertDialog.dismiss();
            }
        });

    }

    public void stateDialog() {

        btnSetState.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        assert (getActivity()) != null;
        LayoutInflater inflaters = (getActivity()).getLayoutInflater();

        View dialogView = inflaters.inflate(R.layout.state_dialog_layout, null);
        builder.setView(dialogView);

        Button btnDialogCancel = dialogView.findViewById(R.id.btnCancelState);
        Button btnDialogOk = dialogView.findViewById(R.id.btnConfirmState);

        stateList = new ArrayList<>();
        final StateModel mSModel = new StateModel();

        try {

            JSONArray obj = new JSONArray(loadStateJSONFromAsset());

            for (int i = 0; i < obj.length(); i++) {
                JSONObject currObj = obj.getJSONObject(i);
                String stateName = currObj.getString("STATE_NAME");

                stateModel = new StateModel(stateName);

                stateList.add(stateModel);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        final RecyclerView recyclerView = dialogView.findViewById(R.id.rvState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        StateListAdapter adapter = new StateListAdapter(mSModel, stateList);
        recyclerView.setAdapter(adapter);

        stateAlertDialog = builder.create();

        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateAlertDialog.cancel();
            }
        });

        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = mSModel.getStateName();
                txtAptState.setHint(result);

                stateAlertDialog.dismiss();
            }
        });

    }


    public void contactDialog() {

        btnSetContact.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        assert (getActivity()) != null;
        LayoutInflater inflaters = (getActivity()).getLayoutInflater();

        View dialogView = inflaters.inflate(R.layout.appoinment_contacts_dialog, null);
        builder.setView(dialogView);

        Button btnDialogCancel = dialogView.findViewById(R.id.btnCancelAptContact);
        Button btnDialogOk = dialogView.findViewById(R.id.btnConfirmAptContact);

        final ContactsModel mCModel = new ContactsModel();

        final RecyclerView recyclerView = dialogView.findViewById(R.id.rvAppointmentContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        AppointmentContactsAdapter adapter = new AppointmentContactsAdapter(mCModel, contactsList);
        recyclerView.setAdapter(adapter);

        contactAlertDialog = builder.create();

        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = mCModel.getContactFirstName() + " " + mCModel.getContactLastName();
                txtAptContactName.setHint(result);

                contactAlertDialog.dismiss();
            }
        });

        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactAlertDialog.cancel();
            }
        });

    }

    @Override
    public void onClick(View v) {

        if(v == btnSetContact) {
            contactAlertDialog.show();
        }

        if(v == btnSetCtry) {
            ctryAlertDialog.show();
        }

        if(v == btnSetState) {
            stateAlertDialog.show();
        }

        if(v == btnSetDate) {
            dateFragment = new AptDetailsDatePickerFragment();
            dateFragment.setTargetFragment(this, DATEPICKER_FRAGMENT);
            dateFragment.show(getActivity().getSupportFragmentManager(), "Appointment Date Picker");
        }

        if(v == btnSetTime) {
            timeFragment = new AptDetailsTimePickerFragment();
            timeFragment.setTargetFragment(this, TIMEPICKER_FRAGMENT);
            timeFragment.show(getActivity().getSupportFragmentManager(), "Appointment Time Picker");
        }

    }

    private void initDataset() {

        contactsList = new ArrayList<>();

        SharedPreferences getFilterValue = getActivity().getSharedPreferences("PREFCONTACTLIST", Context.MODE_PRIVATE);
        contacts = getFilterValue.getString("contactData", "[]");

        try {
            contactsArray = new JSONArray(contacts);

            for(int i=0; i < contactsArray.length(); i++) {

                contactsObj = contactsArray.getJSONObject(i);

                String contactName = contactsObj.getString("CONTACT_FIRST_NAME") + " " + contactsObj.getString("CONTACT_LAST_NAME");
                cModel = new ContactsModel(contactName);
                contactsList.add(i, cModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DATEPICKER_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String selectedDate = bundle.getString("SELECTED_DATE", "");

                    txtAptDate.setText(selectedDate);

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getActivity(), "Date Selection was interrupted!", Toast.LENGTH_LONG).show();
                }
                break;

            case TIMEPICKER_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String selectedTime = bundle.getString("SELECTED_TIME", "");

                    txtAptTime.setText(selectedTime);

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getActivity(), "Time Selection was interrupted!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}