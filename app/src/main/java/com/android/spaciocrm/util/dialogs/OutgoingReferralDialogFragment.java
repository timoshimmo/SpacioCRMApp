package com.android.spaciocrm.util.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import com.android.spaciocrm.util.adapter.OutgoingAdapter;
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
 * Created by freshfuturesmy on 08/02/18.
 */

public class OutgoingReferralDialogFragment extends DialogFragment {

    private static final String ARG_OUTGOING_TITLE = "outgoingTitle";
    private static final String ARG_OUTGOING_POSITION = "outgoingPos";

    private String mTitle;
    private int mPos;

    EditText txtContactName;
    EditText txtSalespersonName;
    EditText txtReferralNote;
    TextView referralName;

    String referrals;
    Dialog d;

    JSONArray allReferralsArray;
    JSONArray outgoingReferralsArray;
    JSONObject outgoingReferralsObj;

    ArrayList<ReferralModel> outgoingList;
    ReferralModel model;

    OutgoingAdapter adapter;

    String outgoingReferrals;

    public OutgoingReferralDialogFragment() {
    }

    public static OutgoingReferralDialogFragment newInstance(String title, int pos) {
        OutgoingReferralDialogFragment fragment = new OutgoingReferralDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_OUTGOING_TITLE, title);
        args.putInt(ARG_OUTGOING_POSITION, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_OUTGOING_TITLE);
            mPos = getArguments().getInt(ARG_OUTGOING_POSITION);
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

        final View rootView = inflater.inflate(R.layout.outgoing_referral_layout, container, false);

        txtContactName = rootView.findViewById(R.id.txtOutgoingContactName);
        txtSalespersonName = rootView.findViewById(R.id.txtOutgoingSalesperson);
        txtReferralNote = rootView.findViewById(R.id.txtReferralNote);
        referralName = rootView.findViewById(R.id.txtOutgoingReferralName);

        Button btnRecallReferral = rootView.findViewById(R.id.btnRecallOutgoingReferral);
        ImageButton btnClose = rootView.findViewById(R.id.btnCloseOutgoingReferral);

        referralName.setText(mTitle);

        outgoingList = new ArrayList<>();
        adapter = new OutgoingAdapter();

        fetchReferrals();

        model = outgoingList.get(mPos);

        txtContactName.setText(model.getContactName());
        txtSalespersonName.setText(model.getSalesPerson());
        txtReferralNote.setText(model.getReferralMessage());

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.cancel();
            }
        });

        btnRecallReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ReferralModel> item;

                SharedPreferences appointmentValues = getActivity().getSharedPreferences("PREFOUTGOINGLIST", Context.MODE_PRIVATE);
                outgoingReferrals = appointmentValues.getString("outgoingData", "[]");

                try {
                    outgoingReferralsArray = new JSONArray(outgoingReferrals);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        outgoingReferralsArray.remove(mPos);
                    }
                    else {

                        JSONObject storeObj;
                        JSONArray storedArray = new JSONArray();

                        try {
                            for(int k=0; k < outgoingReferralsArray.length(); k++) {
                                if(k != mPos) {
                                    storeObj = outgoingReferralsArray.getJSONObject(k);
                                    storedArray.put(k, storeObj);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        outgoingReferralsArray = storedArray;
                    }

                    SharedPreferences prefIncoming = getActivity().getSharedPreferences("PREFOUTGOINGLIST", Context.MODE_PRIVATE);
                    SharedPreferences.Editor incomingEditor = prefIncoming.edit();
                    incomingEditor.putString("outgoingData", outgoingReferralsArray.toString());
                    incomingEditor.apply();

                    item = new Gson().fromJson(outgoingReferralsArray.toString(), new TypeToken<ArrayList<ReferralModel>>() {
                    }.getType());

                    outgoingList.clear();
                    outgoingList.addAll(item);

                    adapter.notifyDataSetChanged();

                    Toast.makeText(getActivity(), "Referral Recalled!", Toast.LENGTH_LONG).show();

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

                    SharedPreferences prefIncoming = getActivity().getSharedPreferences("PREFOUTGOINGLIST", Context.MODE_PRIVATE);
                    SharedPreferences.Editor outgoingEditor = prefIncoming.edit();
                    outgoingEditor.putString("outgoingData", outgoingReferralsArray.toString());
                    outgoingEditor.apply();

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
        DialogFragment dialogFragment = (DialogFragment)getFragmentManager().findFragmentByTag("OutgoingReferralFragment");
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }
}
