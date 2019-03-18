package com.android.spaciocrm.util.adapter;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.spaciocrm.R;
import com.android.spaciocrm.home.activity.HomeActivity;
import com.android.spaciocrm.home.activity.contacts.activity.ContactDetailsActivity;
import com.android.spaciocrm.home.fragments.contacts.FragmentMap;
import com.android.spaciocrm.util.dialogs.CallLogDialogFragment;
import com.android.spaciocrm.util.model.ContactsModel;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by freshfuturesmy on 12/01/18.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> implements ActivityCompat.OnRequestPermissionsResultCallback {

    private ArrayList<ContactsModel> mContactList;
    private ArrayList<ContactsModel> contactListFiltered;
    private Activity mActivity;
    private String mobileNo;
    private ViewHolder myHolder;
    private String fullname;
    private String filterValue;

    private static AlertDialog cTypePopupAlertDialog;
    private int mShortAnimationDuration;

    private static final int PERMISSION_REQUEST_CALL_CONTACTS = 100;
    private static final int PERMISSION_REQUEST_READ_CONTACTS_WHATSAPP = 101;

//    private static final String TAG = ContactsAdapter.class.getSimpleName();

    public ContactsAdapter(Activity actvty, ArrayList<ContactsModel> cm) {
        this.mContactList = cm;
        this.contactListFiltered = cm;
        this.mActivity = actvty;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.contacts_list_structure, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final ContactsModel model = contactListFiltered.get(position);

        mShortAnimationDuration = mActivity.getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        ImageButton btnCall = holder.btnCall;
        ImageButton btnSms = holder.btnSMS;
        ConstraintLayout btnLocation = holder.btnLocation;
        ConstraintLayout btnContactDetails = holder.btnContactDetails;
        ConstraintLayout btnHistory = holder.btnHistory;
        ConstraintLayout btnWhatsApp= holder.btnWhatsApp;
        final TextView txtContactIndicator = holder.txtContactIndicator;

        final TextView txtContactName = holder.txtContactName;
        final TextView txtContactMobile = holder.txtContactMobile;
        ConstraintLayout contactIndicator = holder.contactIndicator;
        ConstraintLayout btnRow = holder.btnRow;

        final LinearLayout layoutBottomView = holder.layoutBottomView;

        mobileNo = model.getContactMobile();
        fullname = mActivity.getString(R.string.contact_fullname, model.getContactFirstName(), model.getContactLastName());

        txtContactName.setText(fullname);
        txtContactMobile.setText(mobileNo);

        final String mapAddress = model.getContactAddresse() +", " + model.getLocationCity() + " " + model.getLocationCountry();

        switch (model.getContactType()) {

            case "Customer":
                contactIndicator.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.green_oval_background));
                txtContactIndicator.setText("C");
                break;
            case "Prospect":
                contactIndicator.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.yellow_oval_background));
                txtContactIndicator.setText("P");
                break;
            case "Lead":
                contactIndicator.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.red_oval_background));
                txtContactIndicator.setText("L");
                break;
        }

        btnContactDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ContactDetailsActivity.class);
                intent.putExtra("CONTACT_NAME", txtContactName.getText());
                intent.putExtra("ROW_NO", holder.getAdapterPosition());
                mActivity.startActivity(intent);
            }
        });

        contactIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactTypePopupDialog();
                cTypePopupAlertDialog.show();
            }
        });

        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) mActivity
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myHolder = holder;
                callPhoneNumber(holder);
            }
        });

        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + mobileNo));
                mActivity.startActivity(smsIntent);
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tag = "CONTACT_LOCATION";
                FragmentMap fragmentMap = FragmentMap.newInstance(mapAddress, model.getCompanyName());

                FragmentManager fragmentManager = ((HomeActivity)mActivity).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                transaction.add(android.R.id.content, fragmentMap, tag)
                        .addToBackStack(null).commit();
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(layoutBottomView.getVisibility() == View.VISIBLE) {
                    layoutBottomView.setVisibility(View.GONE);
                }
                CallLogDialogFragment callLogDialogFragment = CallLogDialogFragment.newInstance(txtContactName.getText().toString(),
                        txtContactMobile.getText().toString(), txtContactIndicator.getText().toString());
                callLogDialogFragment.show(((HomeActivity)mActivity).getSupportFragmentManager(),"CallLogDialogFragment");
            }
        });

        btnRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutBottomView.getVisibility() == View.GONE) {
                    layoutBottomView.setVisibility(View.VISIBLE);
                    layoutBottomView.animate()
                            .alpha(1f)
                            .setDuration(mShortAnimationDuration)
                            .setListener(null);
                }
                else {
                    layoutBottomView.animate()
                            .alpha(0f)
                            .setDuration(mShortAnimationDuration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    layoutBottomView.setVisibility(View.GONE);
                                }
                            });

                }
            }
        });

    /*    if(isContactWhatsapp(mActivity.getString(R.string.contact_fullname, model.getContactFirstName(), model.getContactLastName())) == 1) {

            btnWhatsApp.setVisibility(View.VISIBLE);
        } */

    }

   /* private int isContactWhatsapp(String contactID) {

        int whatsAppExists = 0;
        boolean hasWhatsApp;

        if (Build.VERSION.SDK_INT > 22  && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_READ_CONTACTS_WHATSAPP);


        }
        else {

            String[] projection = new String[]{ContactsContract.RawContacts._ID};
            String selection = ContactsContract.Data.CONTACT_ID + " = ? AND account_type IN (?)";
            String[] selectionArgs = new String[]{contactID, "com.whatsapp"};
            Cursor cursor = mActivity.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection, selection, selectionArgs, null);
            if (cursor != null) {
                hasWhatsApp = cursor.moveToNext();
                if (hasWhatsApp) {
                    whatsAppExists = 1;
                }
                cursor.close();
            }

        }

        return whatsAppExists;

    } */

    private void callPhoneNumber(final ContactsAdapter.ViewHolder holder) {

        String phoneNumber = contactListFiltered.get(holder.getAdapterPosition()).getContactMobile();

        try {
            if (Build.VERSION.SDK_INT > 22  && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CALL_CONTACTS);

            } else {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                mActivity.startActivity(callIntent);

            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_CALL_CONTACTS)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
             //   Log.d(TAG, "Mobile: " + getMobileNo);
                callPhoneNumber(myHolder);
            }
            else
            {
                Log.e(TAG, "Permission not Granted");
            }
        }

      /*  if(requestCode == PERMISSION_REQUEST_READ_CONTACTS_WHATSAPP)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.d(TAG, "Fullname: " + fullname);
                isContactWhatsapp(fullname);
            }
            else
            {
                Log.e(TAG, "Permission not Granted");
            }
        } */
    }

    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

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
                    Intent i = mActivity.getPackageManager()
                            .getLaunchIntentForPackage(
                                    mActivity.getPackageName());
                    if (i != null) {
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    }
                    mActivity.startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton btnCall;
        ImageButton btnSMS;
        TextView txtContactName;
        TextView txtContactMobile;
        ConstraintLayout contactIndicator;
        ConstraintLayout btnRow;
        ConstraintLayout btnContactDetails;
        ConstraintLayout btnHistory;
        ConstraintLayout btnLocation;
        ConstraintLayout btnWhatsApp;
        TextView txtContactIndicator;
        LinearLayout layoutBottomView;

        ViewHolder(View itemView) {
            super(itemView);

            btnCall = itemView.findViewById(R.id.btnPhoneContact);
            btnSMS = itemView.findViewById(R.id.btnMessageContact);
            txtContactName = itemView.findViewById(R.id.txtContactName);
            txtContactMobile = itemView.findViewById(R.id.txtMobileNo);
            contactIndicator = itemView.findViewById(R.id.customer_type_indicator);
            btnRow = itemView.findViewById(R.id.contactTopView);

            btnContactDetails = itemView.findViewById(R.id.btnContactDetails);
            btnHistory = itemView.findViewById(R.id.btnCallHistory);
            btnLocation = itemView.findViewById(R.id.btnContactLocation);
            btnWhatsApp = itemView.findViewById(R.id.btnWhatsApp);
            txtContactIndicator = itemView.findViewById(R.id.contact_indicator_letter_text);
            layoutBottomView = itemView.findViewById(R.id.more_contact_info_layout);
        }
    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = mContactList;
                }
                else {

                    ArrayList<ContactsModel> filteredList = new ArrayList<>();

                    SharedPreferences getFilterValue = mActivity.getSharedPreferences("PREFCONTACTFILTER", Context.MODE_PRIVATE);
                    filterValue = getFilterValue.getString("CFILTER", "");

                    if(filterValue.equals("Last Name")) {
                        for (ContactsModel row : mContactList) {

                            if (row.getContactLastName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }

                        }
                    }

                    if(filterValue.equals("Contact Type")) {
                        for (ContactsModel row : mContactList) {

                            if (row.getContactType().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }

                        }
                    }


                    if(filterValue.equals("Company")) {
                        for (ContactsModel row : mContactList) {

                            if (row.getCompanyName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }

                        }
                    }

                    if(filterValue.equals("City")) {
                        for (ContactsModel row : mContactList) {

                            if (row.getLocationCity().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }

                        }
                    }

                    if(filterValue.equals("State")) {
                        for (ContactsModel row : mContactList) {

                            if (row.getLocationState().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }

                        }
                    }

                    if(filterValue.equals("Email")) {
                        for (ContactsModel row : mContactList) {

                            if (row.getContactEmail().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }

                        }
                    }

                    contactListFiltered = filteredList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                contactListFiltered = (ArrayList<ContactsModel>) results.values;
                notifyDataSetChanged();
            }
        };

    }

    private void contactTypePopupDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflaters = mActivity.getLayoutInflater();

        @SuppressLint("InflateParams")
        View dialogView = inflaters.inflate(R.layout.contact_type_popup_layout, null);
        builder.setView(dialogView);

        Button btnDialogOk = dialogView.findViewById(R.id.btnOkContactTypePopup);

        cTypePopupAlertDialog = builder.create();

        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cTypePopupAlertDialog.dismiss();
            }
        });

    }

}