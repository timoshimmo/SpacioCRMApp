package com.android.spaciocrm.util.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.spaciocrm.R;
import com.android.spaciocrm.util.model.ContactsModel;
import com.android.spaciocrm.util.model.ReferralModel;
import com.android.spaciocrm.util.model.SalesPersonModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by freshfuturesmy on 07/02/18.
 */

public class NewReferralDialogFragment extends DialogFragment {

    Spinner spnContact;
    Spinner spnSalesPerson;
    EditText txtReferralMessage;

    String referrals;
    String contacts;
    String salespersons;

    JSONArray allReferralsArray;
    JSONArray outgoingReferralsArray;
    JSONObject outgoingReferralsObj;

    ContactsModel contactsModel;
    SalesPersonModel salespersonModel;

    ArrayList<ContactsModel> contactList;
    ArrayList<SalesPersonModel> salespersonList;
    ArrayList<ReferralModel> outgoingList;

    ArrayList<String> contactStringList;
    ArrayList<String> salespersonStringList;

    String selectedContact;
    String selectedSalesperson;

    Dialog d;

    public NewReferralDialogFragment() {
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
        if (d!=null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.new_referral_layout, container, false);

        spnContact = rootView.findViewById(R.id.spnContacts);
        spnSalesPerson = rootView.findViewById(R.id.spnSales);
        txtReferralMessage = rootView.findViewById(R.id.txtReferralMessage);

        Button btnAddReferral = rootView.findViewById(R.id.btnSaveNewReferral);
        ImageButton btnClose = rootView.findViewById(R.id.btnCloseMakeNewReferral);

        contactList = new ArrayList<>();
        salespersonList = new ArrayList<>();
        outgoingList = new ArrayList<>();
        contactStringList = new ArrayList<>();
        salespersonStringList = new ArrayList<>();

        fetchContacts();
        fetchSalesperson();
        fetchReferrals();

        for(int i=0; i < contactList.size(); i++) {
            contactsModel = contactList.get(i);

            String contactName = contactsModel.getContactFirstName() + " " + contactsModel.getContactLastName();
            contactStringList.add(contactName);
        }

        for(int i=0; i < salespersonList.size(); i++) {
            salespersonModel = salespersonList.get(i);

            String salesName = salespersonModel.getFirstName() + " " + salespersonModel.getLastName();
            salespersonStringList.add(salesName);
        }

        ArrayAdapter<String> contactsAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_hint_layout, contactStringList);

        ArrayAdapter<String> salespersonAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_hint_layout, salespersonStringList);

        spnContact.setAdapter(contactsAdapter);
        spnSalesPerson.setAdapter(salespersonAdapter);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.cancel();
            }
        });

        btnAddReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateReferral();
            }
        });

        spnContact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedContact = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnSalesPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSalesperson = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    private void updateReferral() {

        String outgoingReferrals;
        JSONObject referralObj;

        if(selectedContact.equals("") || selectedSalesperson.equals("") || txtReferralMessage.getText().equals("")) {
            Toast.makeText(getActivity(), "All fields are required!", Toast.LENGTH_LONG).show();
        }
        else {

            SharedPreferences getFilterValue = getActivity().getSharedPreferences("PREFOUTGOINGLIST", Context.MODE_PRIVATE);
            outgoingReferrals = getFilterValue.getString("outgoingData", "[]");


            try {

                referralObj = new JSONObject();
                outgoingReferralsArray = new JSONArray(outgoingReferrals);


                int count = 100+ outgoingReferralsArray.length() + 1;

                referralObj.put("TITLE", "Outgoing Referral " + String.valueOf(count));
                referralObj.put("CONTACT_NAME", selectedContact);
                referralObj.put("SALES_PERSON", selectedSalesperson);
                referralObj.put("REFERRAL_MESSAGE", txtReferralMessage.getText());

                outgoingReferralsArray.put(outgoingReferralsArray.length(), referralObj);

                SharedPreferences prefOutgoingReferral = getActivity().getSharedPreferences("PREFOUTGOINGLIST", Context.MODE_PRIVATE);
                SharedPreferences.Editor outgoingEditor = prefOutgoingReferral.edit();
                outgoingEditor.putString("outgoingData", outgoingReferralsArray.toString());
                outgoingEditor.apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Toast.makeText(getActivity(), "Referral Successfully Added!", Toast.LENGTH_LONG).show();

            Dismiss();
        }

    }

    private void fetchContacts() {

        ArrayList<ContactsModel> items;

        SharedPreferences getProdcutsValue = getActivity().getSharedPreferences("PREFCONTACTLIST", Context.MODE_PRIVATE);
        contacts = getProdcutsValue.getString("contactData", "[]");

        items = new Gson().fromJson(contacts, new TypeToken<ArrayList<ContactsModel>>() {
        }.getType());

        contactList.clear();
        contactList.addAll(items);

    }

    private void fetchSalesperson() {

        String json;
        ArrayList<SalesPersonModel> items = null;

        SharedPreferences getProdcutsValue = getActivity().getSharedPreferences("PREFSALESPERSONLIST", Context.MODE_PRIVATE);
        salespersons = getProdcutsValue.getString("salespersonsData", "[]");

        if(salespersons.equals("[]")) {

            try {

                if(getActivity() != null) {
                    InputStream is = getActivity().getAssets().open("salesperson/salespersons.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");

                    SharedPreferences prefSalespersonList = getActivity().getSharedPreferences("PREFSALESPERSONLIST", Context.MODE_PRIVATE);
                    SharedPreferences.Editor listEditor = prefSalespersonList.edit();
                    listEditor.putString("salespersonsData", json);
                    listEditor.apply();

                    items = new Gson().fromJson(json, new TypeToken<ArrayList<SalesPersonModel>>() {
                    }.getType());

                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }


        }

        else {
            items = new Gson().fromJson(salespersons, new TypeToken<ArrayList<SalesPersonModel>>() {
            }.getType());
        }

        salespersonList.clear();
        salespersonList.addAll(items);

    }

    private void fetchReferrals() {

        String json;
        ArrayList<ReferralModel> items = null;

        SharedPreferences getFilterValue = getActivity().getSharedPreferences("PREFOUTGOINGLIST", Context.MODE_PRIVATE);
        referrals = getFilterValue.getString("outgoingData", "[]");

        if(referrals.equals("[]")) {

            try {
                if(getActivity() != null) {
                    InputStream is = getActivity().getAssets().open("referrals/referrals.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");

                    allReferralsArray = new JSONArray(json);
                    outgoingReferralsObj = allReferralsArray.getJSONObject(0);
                    outgoingReferralsArray = outgoingReferralsObj.getJSONArray("OUTGOING");

                    SharedPreferences prefContacts = getActivity().getSharedPreferences("PREFOUTGOINGLIST", Context.MODE_PRIVATE);
                    SharedPreferences.Editor contactsEditor = prefContacts.edit();
                    contactsEditor.putString("outgoingData", outgoingReferralsArray.toString());
                    contactsEditor.apply();

                    items = new Gson().fromJson(json, new TypeToken<ArrayList<ReferralModel>>() {
                    }.getType());

                }

            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            items = new Gson().fromJson(referrals, new TypeToken<ArrayList<ReferralModel>>() {
            }.getType());
        }

        outgoingList.clear();
        outgoingList.addAll(items);

    }

    public void Dismiss() {
        DialogFragment dialogFragment = (DialogFragment)getFragmentManager().findFragmentByTag("NewReferralFragment");
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }

}
