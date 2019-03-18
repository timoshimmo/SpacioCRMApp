package com.android.spaciocrm.util.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.spaciocrm.R;
import com.android.spaciocrm.home.activity.HomeActivity;
import com.android.spaciocrm.home.fragments.contacts.FragmentNewContact;
import com.android.spaciocrm.util.model.ContactsModel;

import java.util.ArrayList;

/**
 * Created by freshfuturesmy on 31/01/18.
 */

public class ExistingPhoneContactAdapter extends RecyclerView.Adapter<ExistingPhoneContactAdapter.ViewHolder> {

    private ArrayList<ContactsModel> mContactList;
    private FragmentNewContact fragNewContact;
    Activity mActivity;
    AlertDialog exDialog;

    public ExistingPhoneContactAdapter(Activity context, ArrayList<ContactsModel> cnm, AlertDialog dialog) {
        this.mContactList = cnm;
        this.mActivity = context;
        this.exDialog = dialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.existing_contact_list_structure, parent, false);
        return new ExistingPhoneContactAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ContactsModel model = mContactList.get(position);

        TextView txtContactName = holder.txtContactName;
        TextView txtContactMobile = holder.txtContactMobile;
        View row = holder.rowView;

        txtContactName.setText(model.getContactFirstName());
        txtContactMobile.setText(model.getContactMobile());

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exDialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mContactList.size();
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

        TextView txtContactName;
        TextView txtContactMobile;
        View rowView;

        ViewHolder(View itemView) {
            super(itemView);
            txtContactName = itemView.findViewById(R.id.txtExistingContactName);
            txtContactMobile = itemView.findViewById(R.id.txtExistingContactMobile);
            rowView = itemView;

        }
    }
}
