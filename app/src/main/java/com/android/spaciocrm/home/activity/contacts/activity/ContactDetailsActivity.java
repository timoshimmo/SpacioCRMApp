package com.android.spaciocrm.home.activity.contacts.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.spaciocrm.R;
import com.android.spaciocrm.home.activity.contacts.fragment.FragmentContactAppointments;
import com.android.spaciocrm.home.activity.contacts.fragment.FragmentContactsProduct;
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

public class ContactDetailsActivity extends AppCompatActivity {

    int selectedRow;
    String contactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        final Toolbar toolbar = findViewById(R.id.newAptToolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        contactName = bundle.getString("CONTACT_NAME");
        selectedRow = bundle.getInt("ROW_NO");

        getSupportActionBar().setTitle(contactName);

        AppBarLayout appbar = findViewById(R.id.contact_appbar);

     /*   toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }); */

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0) {
                    getSupportActionBar().setTitle("");
                }
                else {
                    getSupportActionBar().setTitle(contactName);
                }
            }
        });
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {

        public static AlertDialog cTypeAlertDialog;
        public static AlertDialog ctryAlertDialog;
        public static AlertDialog stateAlertDialog;

        public RelativeLayout btnContactType;
        public RelativeLayout btnState;
        public RelativeLayout btnCountry;

        Spinner spnUserContactType;
        EditText txtUserState;
        EditText txtUserCountry;

        EditText txtUserFirstname;
        EditText txtUserLastname;
        EditText txtUserEmail;
        EditText txtUserCompany;
        EditText txtUserMobile;
        EditText txtUserStreet;
        EditText txtUserCity;
        EditText txtLocationLandMark;

        private static final String ARG_SECTION_NAME = "section_name";
        private static final String ARG_ROW_NO = "row_number";

        static ArrayList<CountryModel> countryList = null;
        static ArrayList<StateModel> stateList = null;

        CountryModel ctryModel;
        StateModel stateModel;

        RadioGroup groupContactOpenClose;
        RadioButton rbOpen;
        RadioButton rbClosed;

        RadioGroup groupAddyType;
        RadioButton rbHomeAddress;
        RadioButton rbOfficeAddress;

        int rowNumber;
        String selectedContact;

        JSONArray contactsArray;
        JSONObject contactsObj;

        String selectedContactType;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(String sectionName, int rowNo) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(ARG_SECTION_NAME, sectionName);
            args.putInt(ARG_ROW_NO, rowNo);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                rowNumber = getArguments().getInt(ARG_ROW_NO);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_contact_details, container, false);

            spnUserContactType = rootView.findViewById(R.id.spnContactType);
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

            ImageButton btnQtnMark = rootView.findViewById(R.id.btnOpenCloseQtn);
            ImageButton btnEditFName = rootView.findViewById(R.id.btnFNameEdit);
            ImageButton btnEditLName = rootView.findViewById(R.id.btnLNameEdit);
            ImageButton btnEditEmail = rootView.findViewById(R.id.btnEmailEdit);
            ImageButton btnEditCompany = rootView.findViewById(R.id.btnCompanyEdit);
            ImageButton btnEditMobile = rootView.findViewById(R.id.btnMobileEdit);
            ImageButton btnEditStreet = rootView.findViewById(R.id.btnStreetEdit);
            ImageButton btnEditCity = rootView.findViewById(R.id.btnCityEdit);
            ImageButton btnEditLandMark = rootView.findViewById(R.id.btnLandmarkEdit);

            btnState= rootView.findViewById(R.id.btnSetState);
            btnCountry = rootView.findViewById(R.id.btnSetCtry);

            groupContactOpenClose = rootView.findViewById(R.id.grp_open_closed);
            rbOpen = rootView.findViewById(R.id.rbOpen);
            rbClosed = rootView.findViewById(R.id.rbClosed);

            groupAddyType = rootView.findViewById(R.id.grpAddressType);
            rbHomeAddress = rootView.findViewById(R.id.rbHomeAddress);
            rbOfficeAddress = rootView.findViewById(R.id.rbOfficeAddress);

            SharedPreferences getFilterValue = getActivity().getSharedPreferences("PREFCONTACTLIST", Context.MODE_PRIVATE);
            selectedContact = getFilterValue.getString("contactData", "[]");

            try {
                contactsArray = new JSONArray(selectedContact);
                contactsObj = contactsArray.getJSONObject(rowNumber);

                if(contactsObj.getString("CONTACT_OPEN_CLOSE").equals("Open")) {
                    rbOpen.setChecked(true);
                }

                if(contactsObj.getString("CONTACT_OPEN_CLOSE").equals("Closed")) {
                    rbClosed.setChecked(true);
                }

                if(contactsObj.getString("CONTACT_ADDRESS_TYPE").equals("Home")) {
                    rbHomeAddress.setChecked(true);
                }

                if(contactsObj.getString("CONTACT_ADDRESS_TYPE").equals("Office")) {
                    rbOfficeAddress.setChecked(true);
                }

                if(contactsObj.getString("CONTACT_TYPE").equals("Lead")) {
                    spnUserContactType.setSelection(1);

                }

                if(contactsObj.getString("CONTACT_TYPE").equals("Prospect")) {
                    spnUserContactType.setSelection(2);
                }

                if(contactsObj.getString("CONTACT_TYPE").equals("Customer")) {
                    spnUserContactType.setSelection(3);
                }

                txtUserFirstname.setText(contactsObj.getString("CONTACT_FIRST_NAME"));
                txtUserLastname.setText(contactsObj.getString("CONTACT_LAST_NAME"));
                txtUserEmail.setText(contactsObj.getString("CONTACT_EMAIL"));
                txtUserCompany.setText(contactsObj.getString("CONTACT_COMPANY_NAME"));
                txtUserMobile.setText(contactsObj.getString("CONTACT_MOBILE"));
                txtUserStreet.setText(contactsObj.getString("CONTACT_ADDRESS"));
                txtUserCity.setText(contactsObj.getString("CONTACT_CITY"));
                txtUserState.setText(contactsObj.getString("CONTACT_STATE"));
                txtUserCountry.setText(contactsObj.getString("CONTACT_COUNTRY"));
                txtLocationLandMark.setText(contactsObj.getString("NEAREST_LANDMARK"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            Button btnUpdateContact = rootView.findViewById(R.id.btnUpdateContact);

            btnEditFName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtUserFirstname.setEnabled(true);
                    txtUserFirstname.setGravity(Gravity.START);
                    txtUserFirstname.requestFocus();
                }
            });

            btnEditLName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtUserLastname.setEnabled(true);
                    txtUserLastname.setGravity(Gravity.START);
                    txtUserLastname.requestFocus();
                }
            });

            btnEditEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtUserEmail.setEnabled(true);
                    txtUserEmail.setGravity(Gravity.START);
                    txtUserEmail.requestFocus();
                }
            });

            btnEditCompany.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtUserCompany.setEnabled(true);
                    txtUserCompany.setGravity(Gravity.START);
                    txtUserCompany.requestFocus();
                }
            });

            btnEditMobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtUserMobile.setEnabled(true);
                    txtUserMobile.setGravity(Gravity.START);
                    txtUserMobile.requestFocus();
                }
            });

            btnEditStreet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtUserStreet.setEnabled(true);
                    txtUserStreet.setGravity(Gravity.START);
                    txtUserStreet.requestFocus();
                }
            });

            btnEditCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtUserCity.setEnabled(true);
                    txtUserCity.setGravity(Gravity.START);
                    txtUserCity.requestFocus();
                }
            });


            btnEditLandMark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtLocationLandMark.setEnabled(true);
                    txtLocationLandMark.requestFocus();
                }
            });

            spnUserContactType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedContactType = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            txtUserFirstname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus) {
                        txtUserFirstname.setGravity(Gravity.END);
                        txtUserFirstname.setEnabled(false);
                        if(!txtUserFirstname.getText().toString().equals("")) {
                            txtUserFirstname.setHint(txtUserFirstname.getText());
                        }

                    }
                }
            });

            txtUserLastname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus) {
                        txtUserLastname.setGravity(Gravity.END);
                        txtUserLastname.setEnabled(false);
                        if(!txtUserLastname.getText().toString().equals("")) {
                            txtUserLastname.setHint(txtUserLastname.getText());
                        }

                    }
                }
            });

            txtUserEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus) {
                        txtUserEmail.setGravity(Gravity.END);
                        txtUserEmail.setEnabled(false);
                        if(!txtUserEmail.getText().toString().equals("")) {
                            txtUserEmail.setHint(txtUserEmail.getText());
                        }

                    }
                }
            });

            txtUserCompany.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus) {
                        txtUserCompany.setGravity(Gravity.END);
                        txtUserCompany.setEnabled(false);
                        if(!txtUserCompany.getText().toString().equals("")) {
                            txtUserCompany.setHint(txtUserCompany.getText());
                        }

                    }
                }
            });

            txtUserMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus) {
                        txtUserMobile.setGravity(Gravity.END);
                        txtUserMobile.setEnabled(false);
                        if(!txtUserMobile.getText().toString().equals("")) {
                            txtUserMobile.setHint(txtUserMobile.getText());
                        }

                    }
                }
            });

            txtUserStreet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus) {
                        txtUserStreet.setGravity(Gravity.END);
                        txtUserStreet.setEnabled(false);
                        if(!txtUserStreet.getText().toString().equals("")) {
                            txtUserStreet.setHint(txtUserStreet.getText());
                        }

                    }
                }
            });

            txtUserCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus) {
                        txtUserCity.setGravity(Gravity.END);
                        txtUserCity.setEnabled(false);
                        if(!txtUserCity.getText().toString().equals("")) {
                            txtUserCity.setHint(txtUserCity.getText());
                        }

                    }
                }
            });

            txtLocationLandMark.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus) {
                        txtLocationLandMark.setEnabled(false);
                        if(!txtLocationLandMark.getText().toString().equals("")) {
                            txtLocationLandMark.setHint(txtLocationLandMark.getText());
                        }

                    }
                }
            });

            btnUpdateContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(txtUserFirstname.getText().toString().equals("") || txtUserLastname.getText().toString().equals("") ||
                            txtUserEmail.getText().toString().equals("") || txtUserCompany.getText().toString().equals("") ||
                            txtUserMobile.getText().toString().equals("") || txtUserStreet.getText().toString().equals("") ||
                            txtUserCity.getText().toString().equals("") || txtUserState.getText().toString().equals("") ||
                            txtUserCountry.getText().toString().equals("") || txtLocationLandMark.getText().toString().equals("") ||
                            selectedContactType.equals("") || groupContactOpenClose.getCheckedRadioButtonId() == -1 ||
                            groupAddyType.getCheckedRadioButtonId() == -1) {

                        Snackbar.make(v, "All Inputs are required!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }

                    else {
                        updateContact();
                    }

                }
            });

            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();

          //  contactTypeDialog();
            countryDialog();
            stateDialog();
        }

        private void updateContact() {

            String openClose = null;
            String addyType = null;

            if(groupContactOpenClose.getCheckedRadioButtonId() == R.id.rbOpen) {
                openClose = "Open";
            }

            if(groupContactOpenClose.getCheckedRadioButtonId() == R.id.rbClosed) {
                openClose = "Closed";
            }

            if(groupAddyType.getCheckedRadioButtonId() == R.id.rbHomeAddress) {
                addyType = "Home";
            }

            if(groupAddyType.getCheckedRadioButtonId() == R.id.rbOfficeAddress) {
                addyType = "Office";
            }

            try {
                contactsObj.put("CONTACT_TYPE", selectedContactType);
                contactsObj.put("CONTACT_OPEN_CLOSE", openClose);
                contactsObj.put("CONTACT_FIRST_NAME", txtUserFirstname.getText().toString());
                contactsObj.put("CONTACT_LAST_NAME", txtUserLastname.getText().toString());
                contactsObj.put("CONTACT_EMAIL", txtUserEmail.getText().toString());
                contactsObj.put("CONTACT_COMPANY_NAME", txtUserCompany.getText().toString());
                contactsObj.put("CONTACT_MOBILE", txtUserMobile.getText().toString());
                contactsObj.put("CONTACT_ADDRESS_TYPE", addyType);
                contactsObj.put("CONTACT_ADDRESS", txtUserStreet.getText().toString());
                contactsObj.put("CONTACT_CITY", txtUserCity.getText().toString());
                contactsObj.put("CONTACT_STATE", txtUserState.getText().toString());
                contactsObj.put("CONTACT_COUNTRY", txtUserCountry.getText().toString());
                contactsObj.put("NEAREST_LANDMARK", txtLocationLandMark.getText().toString());

                contactsArray.put(rowNumber, contactsObj);

                SharedPreferences prefContactList = getActivity().getSharedPreferences("PREFCONTACTLIST", Context.MODE_PRIVATE);
                SharedPreferences.Editor listEditor = prefContactList.edit();
                listEditor.putString("contactData", contactsArray.toString());
                listEditor.apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Toast.makeText(getActivity(), "Contact Successfully Updated!", Toast.LENGTH_LONG).show();
            getActivity().onBackPressed();

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
                    txtUserCountry.setHint(result);

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
                    txtUserState.setHint(result);

                    stateAlertDialog.dismiss();
                }
            });

        }

        @Override
        public void onClick(View v) {

            if(v == btnContactType) {
                cTypeAlertDialog.show();
            }

            if(v == btnCountry) {
                ctryAlertDialog.show();
            }

            if(v == btnState) {
                stateAlertDialog.show();
            }
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch(position) {

                case 0:
                    return PlaceholderFragment.newInstance("Details", selectedRow);
                case 1:
                    return FragmentContactAppointments.newInstance("Appointments", contactName);
                case 2:
                    return FragmentContactsProduct.newInstance(contactName, selectedRow);
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Details";
                case 1:
                    return "Events";
                case 2:
                    return "Products";
            }
            return null;
        }
    }


}
