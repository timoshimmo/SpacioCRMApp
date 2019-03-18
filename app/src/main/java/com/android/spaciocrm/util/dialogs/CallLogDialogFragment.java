package com.android.spaciocrm.util.dialogs;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.spaciocrm.R;
import com.android.spaciocrm.util.adapter.CallLogAdapter;
import com.android.spaciocrm.util.model.CallLogModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class CallLogDialogFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // TODO: Rename and change types of parameters
    private static final String ARG_FULL_NAME = "fullname";
    private static final String ARG_PHONE_NUMBER = "phone_number";
    private static final String ARG_CONTACT_TYPE = "contact_type";

    private String mFullname;
    private String mobileNum;
    private String mCType;

    private static final String TAG = "CallLog";
    private static final int URL_LOADER = 1;
    private static final int PERMISSION_REQUEST_READ_CALL_LOGS = 101;
    private static final int PERMISSION_REQUEST_READ_CONTACTS = 100;

    Dialog d;

    protected ArrayList<CallLogModel> callLogList;
    CallLogAdapter adapter;

    Context context;


    public CallLogDialogFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CallLogDialogFragment newInstance(String fullname, String mobileNum, String cType) {
        CallLogDialogFragment fragment = new CallLogDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FULL_NAME, fullname);
        args.putString(ARG_PHONE_NUMBER, mobileNum);
        args.putString(ARG_CONTACT_TYPE, cType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFullname = getArguments().getString(ARG_FULL_NAME);
            mobileNum = getArguments().getString(ARG_PHONE_NUMBER);
            mCType = getArguments().getString(ARG_CONTACT_TYPE);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();
        d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(d.getWindow()).setLayout(width, height);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_log_dialog, container, false);

        Toolbar toolbar = view.findViewById(R.id.tbCallHistory);

        TextView txtContactName = view.findViewById(R.id.txtContactName);
        final TextView txtContactMobile = view.findViewById(R.id.txtMobileNo);
        TextView txtContactIndicator = view.findViewById(R.id.contact_indicator_letter_text);

        ConstraintLayout contactIndicator = view.findViewById(R.id.customer_type_indicator);

        context = getActivity();

        callLogList = new ArrayList<>();
        initialize();
        adapter = new CallLogAdapter(getActivity(), callLogList);

        ImageButton btnClose = toolbar.findViewById(R.id.btnCloseCallHistory);
        ImageButton btnCall = view.findViewById(R.id.btnPhoneContact);
        ImageButton btnSMS = view.findViewById(R.id.btnMessageContact);
        RecyclerView recyclerView = view.findViewById(R.id.rvCallLogList);

        txtContactName.setText(mFullname);
        txtContactMobile.setText(mobileNum);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.cancel();
            }
        });

        switch (mCType) {

            case "C":
                contactIndicator.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.green_oval_background));
                txtContactIndicator.setText(mCType);
                break;
            case "P":
                contactIndicator.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.yellow_oval_background));
                txtContactIndicator.setText(mCType);
                break;
            case "L":
                contactIndicator.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.red_oval_background));
                txtContactIndicator.setText(mCType);
                break;
        }

        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) Objects.requireNonNull(getActivity())
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhoneNumber(String.valueOf(txtContactMobile.getText()));
            }
        });

        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + String.valueOf(txtContactMobile.getText())));
                Objects.requireNonNull(getActivity()).startActivity(smsIntent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void callPhoneNumber(String phoneNumber) {

        try {
            if (Build.VERSION.SDK_INT > 22 && ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_READ_CONTACTS);

            } else {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                Objects.requireNonNull(getActivity()).startActivity(callIntent);

            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_READ_CALL_LOGS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initialize();
            } else {
                Log.e(TAG, "Permission not Granted");
            }
        }

        if (requestCode == PERMISSION_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhoneNumber(mobileNum);
            } else {
                Log.e(TAG, "Permission not Granted");
            }
        }

    }

    private void initialize() {

        Log.d(TAG, "initialize()");

        Log.d(TAG, "initialize() >> initialise loader");

        if (Build.VERSION.SDK_INT > 22 && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CALL_LOG}, PERMISSION_REQUEST_READ_CALL_LOGS);

        }
        else {
            getLoaderManager().initLoader(URL_LOADER, null, this);
        }

    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        Log.d(TAG, "onCreateLoader() >> loaderID : " + id);

        // Defines a string to contain the selection clause
        String mSelectionClause = CallLog.Calls.NUMBER+ " = ?";

        // Initializes an array to contain selection arguments
        String[] mSelectionArgs = { mobileNum };


        switch (id) {

            case URL_LOADER:

                return new CursorLoader(
                        getActivity(),   // Parent activity context
                        CallLog.Calls.CONTENT_URI,        // Table to query
                        null,     // Projection to return
                        mSelectionClause,            // No selection clause
                        mSelectionArgs,            // No selection arguments
                        CallLog.Calls.DATE + " DESC"             // Default sort order
                );

                default:
                    return null;
        }

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor managedCursor) {

        Log.d(TAG, "onLoadFinished()");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
     //   StringBuffer stringBuffer = new StringBuffer();

      //  int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        CallLogModel cmodel = null; 

        while (managedCursor.moveToNext()) {

            cmodel = new CallLogModel();

          //  String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDateTime = new Date(Long.valueOf(callDate));

            String formattedDate = format.format(callDateTime);
            String callDuration = managedCursor.getString(duration);
            String dir = null;

            int callTypeCode = Integer.parseInt(callType);

            switch (callTypeCode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "Outgoing Call";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "Incoming Call";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "Missed Call";
                    break;
            }

            cmodel.setCallType(dir);
            cmodel.setCallDateTime(formattedDate);
            cmodel.setCallDuration(callDuration);

            callLogList.add(cmodel);

        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        Log.d(TAG, "onLoaderReset()");
    }

    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {

                Log.i(LOG_TAG, "OFFHOOK");
                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
                    Intent i = Objects.requireNonNull(getActivity()).getPackageManager()
                            .getLaunchIntentForPackage(
                                    getActivity().getPackageName());
                    if (i != null) {
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    }
                    getActivity().startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }

}
