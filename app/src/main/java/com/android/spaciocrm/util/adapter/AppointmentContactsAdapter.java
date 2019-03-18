package com.android.spaciocrm.util.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.spaciocrm.R;
import com.android.spaciocrm.util.model.ContactsModel;

import java.util.ArrayList;

/**
 * Created by freshfuturesmy on 26/01/18.
 */

public class AppointmentContactsAdapter extends RecyclerView.Adapter<AppointmentContactsAdapter.ViewHolder> {

    private ArrayList<ContactsModel> mContactNames;
    private int selectedIndex = -1;
    private ContactsModel mContactModel;

    public AppointmentContactsAdapter(ContactsModel cm, ArrayList<ContactsModel> cnm) {

        this.mContactNames = cnm;
        this.mContactModel = cm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.country_list_structure, parent, false);
        return new AppointmentContactsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ContactsModel model = mContactNames.get(position);
        final RadioButton rbContactName = holder.rbContactName;

        rbContactName.setText(model.getContactFirstName());
        rbContactName.setChecked(position == selectedIndex);

        rbContactName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.getAdapterPosition() == selectedIndex) {
                    rbContactName.setChecked(false);
                    selectedIndex = -1;
                }
                else {

                    selectedIndex = holder.getAdapterPosition();
                    rbContactName.setChecked(true);
                    mContactModel.setContactName((String)rbContactName.getText());
                    notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mContactNames.size();
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

        RadioButton rbContactName;

        ViewHolder(View itemView) {
            super(itemView);
            rbContactName = itemView.findViewById(R.id.rbCtryName);

        }
    }
}
