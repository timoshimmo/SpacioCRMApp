package com.android.spaciocrm.util.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.spaciocrm.R;
import com.android.spaciocrm.util.adapter.IncomingAdapter;
import com.android.spaciocrm.util.model.ReferralModel;
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

public class IncomingReferralDialogFragment extends DialogFragment {

    private static final String ARG_INCOMING_TITLE = "incomingTitle";
    private static final String ARG_INCOMING_POSITION = "incomingPos";

    private String mTitle;
    private int mPos;

    EditText txtContactName;
    EditText txtSalespersonName;
    EditText txtReferralNote;
    TextView referralName;

    String referrals;
    Dialog d;

    JSONArray allReferralsArray;
    JSONArray incomingReferralsArray;
    JSONObject incomingReferralsObj;

    ArrayList<ReferralModel> incomingList;
    ReferralModel model;

    IncomingAdapter adapter;

    String incomingReferrals;

    public IncomingReferralDialogFragment() {
    }

    public static IncomingReferralDialogFragment newInstance(String title, int pos) {
        IncomingReferralDialogFragment fragment = new IncomingReferralDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_INCOMING_TITLE, title);
        args.putInt(ARG_INCOMING_POSITION, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_INCOMING_TITLE);
            mPos = getArguments().getInt(ARG_INCOMING_POSITION);
        }
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
        final View rootView = inflater.inflate(R.layout.incoming_referral_layout, container, false);

        txtContactName = rootView.findViewById(R.id.txtIncomingContactName);
        txtSalespersonName = rootView.findViewById(R.id.txtIncomingSalespersonName);
        txtReferralNote = rootView.findViewById(R.id.txtReferralNote);
        referralName = rootView.findViewById(R.id.txtIncomingReferralName);
        Button btnRejectReferral = rootView.findViewById(R.id.btnRejectIncomingReferral);
        Button btnAcceptReferral = rootView.findViewById(R.id.btnAcceptIncomingReferral);
        ImageButton btnClose = rootView.findViewById(R.id.btnCloseIncomingReferral);

        referralName.setText(mTitle);

        incomingList = new ArrayList<>();
        adapter = new IncomingAdapter();
        fetchReferrals();
        model = incomingList.get(mPos);

        txtContactName.setText(model.getContactName());
        txtSalespersonName.setText(model.getSalesPerson());
        txtReferralNote.setText(model.getReferralMessage());

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.cancel();
            }
        });

        btnAcceptReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<ReferralModel> item;

                SharedPreferences appointmentValues = getActivity().getSharedPreferences("PREFINCOMINGLIST", Context.MODE_PRIVATE);
                incomingReferrals = appointmentValues.getString("incomingData", "[]");

                try {
                    incomingReferralsArray = new JSONArray(incomingReferrals);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        incomingReferralsArray.remove(mPos);
                    }
                    else {

                        JSONObject storeObj;
                        JSONArray storedArray = new JSONArray();

                        try {
                            for(int k=0; k < incomingReferralsArray.length(); k++) {
                                if(k != mPos) {
                                    storeObj = incomingReferralsArray.getJSONObject(k);
                                    storedArray.put(k, storeObj);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        incomingReferralsArray = storedArray;
                    }


                    SharedPreferences prefIncoming = getActivity().getSharedPreferences("PREFINCOMINGLIST", Context.MODE_PRIVATE);
                    SharedPreferences.Editor incomingEditor = prefIncoming.edit();
                    incomingEditor.putString("incomingData", incomingReferralsArray.toString());
                    incomingEditor.apply();

                    item = new Gson().fromJson(incomingReferralsArray.toString(), new TypeToken<ArrayList<ReferralModel>>() {
                    }.getType());

                    incomingList.clear();
                    incomingList.addAll(item);

                    adapter.notifyDataSetChanged();

                    Toast.makeText(getActivity(), "Referral Accepted!", Toast.LENGTH_LONG).show();
                    Dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        btnRejectReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<ReferralModel> item;

                SharedPreferences appointmentValues = getActivity().getSharedPreferences("PREFINCOMINGLIST", Context.MODE_PRIVATE);
                incomingReferrals = appointmentValues.getString("incomingData", "[]");

                try {
                    incomingReferralsArray = new JSONArray(incomingReferrals);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        incomingReferralsArray.remove(mPos);
                    }
                    else {

                        JSONObject storeObj;
                        JSONArray storedArray = new JSONArray();

                        try {
                            for(int k=0; k < incomingReferralsArray.length(); k++) {
                                if(k != mPos) {
                                    storeObj = incomingReferralsArray.getJSONObject(k);
                                    storedArray.put(k, storeObj);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        incomingReferralsArray = storedArray;
                    }


                    SharedPreferences prefIncoming = getActivity().getSharedPreferences("PREFINCOMINGLIST", Context.MODE_PRIVATE);
                    SharedPreferences.Editor incomingEditor = prefIncoming.edit();
                    incomingEditor.putString("incomingData", incomingReferralsArray.toString());
                    incomingEditor.apply();

                    item = new Gson().fromJson(incomingReferralsArray.toString(), new TypeToken<ArrayList<ReferralModel>>() {
                    }.getType());

                    incomingList.clear();
                    incomingList.addAll(item);

                    adapter.notifyDataSetChanged();

                    Toast.makeText(getActivity(), "Referral Rejected!", Toast.LENGTH_LONG).show();
                    Dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return rootView;
    }

    private void fetchReferrals() {

        String json;
        ArrayList<ReferralModel> items = null;

        SharedPreferences getFilterValue = getActivity().getSharedPreferences("PREFINCOMINGLIST", Context.MODE_PRIVATE);
        referrals = getFilterValue.getString("incomingData", "[]");

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
                    incomingReferralsObj = allReferralsArray.getJSONObject(0);
                    incomingReferralsArray = incomingReferralsObj.getJSONArray("INCOMING");

                    SharedPreferences prefIncoming = getActivity().getSharedPreferences("PREFINCOMINGLIST", Context.MODE_PRIVATE);
                    SharedPreferences.Editor incomingEditor = prefIncoming.edit();
                    incomingEditor.putString("incomingData", incomingReferralsArray.toString());
                    incomingEditor.apply();

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

        incomingList.clear();
        incomingList.addAll(items);

    }

    public void Dismiss() {
        DialogFragment dialogFragment = (DialogFragment)getFragmentManager().findFragmentByTag("IncomingReferralFragment");
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }
}
