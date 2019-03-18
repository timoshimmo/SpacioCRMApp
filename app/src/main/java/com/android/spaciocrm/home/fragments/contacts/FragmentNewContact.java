package com.android.spaciocrm.home.fragments.contacts;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.spaciocrm.R;
import com.android.spaciocrm.util.adapter.CountryListAdapter;
import com.android.spaciocrm.util.adapter.StateListAdapter;
import com.android.spaciocrm.util.model.CountryModel;
import com.android.spaciocrm.util.model.DividerItemDecoration;
import com.android.spaciocrm.util.model.StateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FragmentNewContact extends Fragment implements View.OnClickListener {

    public static AlertDialog ctryAlertDialog;
    public static AlertDialog stateAlertDialog;

    public RelativeLayout btnState;
    public RelativeLayout btnCountry;

    EditText txtUserState;
    EditText txtUserCountry;

    static ArrayList<CountryModel> countryList = null;
    static ArrayList<StateModel> stateList = null;

    CountryModel ctryModel;
    StateModel stateModel;

    private String mgetFName;
    private String mgetMobileNo;

    private static final String ARG_FIRST_NAME = "contact_fname";
    private static final String ARG_MOBILE_NO = "contact_mobile";

    EditText txtUserFirstname;
    EditText txtUserLastname;
    EditText txtUserEmail;
    EditText txtUserCompany;
    EditText txtUserMobile;
    EditText txtUserStreet;
    EditText txtUserCity;
    EditText txtLocationLandMark;

    RadioGroup groupContactType;
    RadioButton rbLead;
    RadioButton rbProspect;

    RadioGroup groupContactOpenClose;
    RadioButton rbOpen;
    RadioButton rbClosed;

    RadioGroup groupAddyType;
    RadioButton rbHomeAddress;
    RadioButton rbOfficeAddress;

    JSONArray contactsArray;
    String contacts;

    public FragmentNewContact() {
        // Required empty public constructor
    }

    public static FragmentNewContact newInstance(String fullname, String mobile) {
        FragmentNewContact fragment = new FragmentNewContact();
        Bundle args = new Bundle();
        args.putString(ARG_FIRST_NAME, fullname);
        args.putString(ARG_MOBILE_NO, mobile);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mgetFName = getArguments().getString(ARG_FIRST_NAME);
            mgetMobileNo = getArguments().getString(ARG_MOBILE_NO);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_new_contact, container, false);

        txtUserFirstname = rootView.findViewById(R.id.txtUserFirstName);
        txtUserLastname = rootView.findViewById(R.id.txtUserLastName);
        txtUserEmail = rootView.findViewById(R.id.txtUserEmail);
        txtUserCompany = rootView.findViewById(R.id.txtUserCompany);
        txtUserMobile = rootView.findViewById(R.id.txtUserMobile);
        txtUserStreet = rootView.findViewById(R.id.txtUserStreet);
        txtUserCity = rootView.findViewById(R.id.txtUserCity);
        txtUserState = rootView.findViewById(R.id.txtUserState);
        txtUserCountry = rootView.findViewById(R.id.txtUserCountry);
        txtLocationLandMark = rootView.findViewById(R.id.txtAddressLandMark);

        btnState= rootView.findViewById(R.id.btnSetState);
        btnCountry = rootView.findViewById(R.id.btnSetCtry);

        groupContactType = rootView.findViewById(R.id.grpContactType);
        rbLead = rootView.findViewById(R.id.rbLead);
        rbProspect = rootView.findViewById(R.id.rbProspect);

        groupContactOpenClose = rootView.findViewById(R.id.grp_open_closed);
        rbOpen = rootView.findViewById(R.id.rbOpen);
        rbClosed = rootView.findViewById(R.id.rbClosed);

        groupAddyType = rootView.findViewById(R.id.grpAddressType);
        rbHomeAddress = rootView.findViewById(R.id.rbHomeAddress);
        rbOfficeAddress = rootView.findViewById(R.id.rbOfficeAddress);

        Button btnUpdateContact = rootView.findViewById(R.id.btnUpdateContact);
        ImageButton btnCloseNewContact = rootView.findViewById(R.id.btnCancelNewContact);

       if(!mgetFName.equals("") || mgetFName != null) {
            txtUserFirstname.setText(mgetFName);
        }

        if(!mgetMobileNo.equals("") || mgetMobileNo != null) {
            txtUserMobile.setText(mgetMobileNo);
        }


        btnUpdateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtUserFirstname.getText().toString().equals("") || txtUserLastname.getText().toString().equals("") ||
                        txtUserEmail.getText().toString().equals("") || txtUserCompany.getText().toString().equals("") ||
                        txtUserMobile.getText().toString().equals("") || txtUserStreet.getText().toString().equals("") ||
                        txtUserCity.getText().toString().equals("") || txtUserState.getText().toString().equals("") ||
                        txtUserCountry.getText().toString().equals("") || txtLocationLandMark.getText().toString().equals("") ||
                        groupContactType.getCheckedRadioButtonId() == -1 || groupContactOpenClose.getCheckedRadioButtonId() == -1 ||
                        groupAddyType.getCheckedRadioButtonId() == -1) {

                    Snackbar.make(rootView, "All Inputs are required!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                else {
                    updateContact();

                }
            }
        });

        btnCloseNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        countryDialog();
        stateDialog();
    }

    private void updateContact() {

        SharedPreferences getFilterValue = getActivity().getSharedPreferences("PREFCONTACTLIST", Context.MODE_PRIVATE);
        contacts = getFilterValue.getString("contactData", "[]");

        try {

            contactsArray = new JSONArray(contacts);
            JSONObject contactObj = new JSONObject();

            int contactTypeId = groupContactType.getCheckedRadioButtonId();
            int contactOpenCloseId = groupContactOpenClose.getCheckedRadioButtonId();
            int addressTypeId = groupAddyType.getCheckedRadioButtonId();

            if(contactTypeId == rbLead.getId()) {
                contactObj.put("CONTACT_TYPE", "Lead");
            }

            if(contactTypeId == rbProspect.getId()) {
                contactObj.put("CONTACT_TYPE", "Prospect");
            }

            if(contactOpenCloseId == rbOpen.getId()) {
                contactObj.put("CONTACT_OPEN_CLOSE", "Open");
            }

            if(contactOpenCloseId == rbClosed.getId()) {
                contactObj.put("CONTACT_OPEN_CLOSE", "Closed");
            }

            contactObj.put("CONTACT_FIRST_NAME", txtUserFirstname.getText().toString());
            contactObj.put("CONTACT_LAST_NAME", txtUserLastname.getText().toString());
            contactObj.put("CONTACT_EMAIL", txtUserEmail.getText().toString());
            contactObj.put("CONTACT_COMPANY_NAME", txtUserCompany.getText().toString());
            contactObj.put("CONTACT_MOBILE", txtUserMobile.getText().toString());

            if(addressTypeId == rbHomeAddress.getId()) {
                contactObj.put("CONTACT_ADDRESS_TYPE", "Home");
            }

            if(addressTypeId == rbOfficeAddress.getId()) {
                contactObj.put("CONTACT_ADDRESS_TYPE", "Office");
            }

            contactObj.put("CONTACT_ADDRESS", txtUserStreet.getText().toString());
            contactObj.put("CONTACT_CITY", txtUserCity.getText().toString());
            contactObj.put("CONTACT_STATE", txtUserState.getText().toString());
            contactObj.put("CONTACT_COUNTRY", txtUserCountry.getText().toString());
            contactObj.put("NEAREST_LANDMARK", txtLocationLandMark.getText().toString());

            contactsArray.put(contactsArray.length(), contactObj);

            SharedPreferences prefContactList = getActivity().getSharedPreferences("PREFCONTACTLIST", Context.MODE_PRIVATE);
            SharedPreferences.Editor listEditor = prefContactList.edit();
            listEditor.putString("contactData", contactsArray.toString());
            listEditor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(getActivity(), "Contact Successfully Saved!", Toast.LENGTH_LONG).show();

    }

    public String loadCountryJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("country/country_code.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String loadStateJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("state/state_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void countryDialog() {

        btnCountry.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                txtUserCountry.setText(result);

                ctryAlertDialog.dismiss();
            }
        });

    }

    public void stateDialog() {

        btnState.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                txtUserState.setText(result);

                stateAlertDialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v == btnCountry) {
            ctryAlertDialog.show();
        }

        if(v == btnState) {
            stateAlertDialog.show();
        }
    }
}
