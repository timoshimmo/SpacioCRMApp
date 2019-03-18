package com.android.spaciocrm.util.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.spaciocrm.R;
import com.android.spaciocrm.home.activity.HomeActivity;
import com.android.spaciocrm.home.activity.contacts.activity.ContactDetailsActivity;
import com.android.spaciocrm.home.fragments.appointments.FragmentAppointmentDetails;
import com.android.spaciocrm.util.model.AppointmentsModel;

import java.util.ArrayList;


public class ContactAppointmentsAdapter extends RecyclerView.Adapter<ContactAppointmentsAdapter.ViewHolder> {

    private ArrayList<AppointmentsModel> mEventsList;
    private Activity mActivity;

    private FragmentAppointmentDetails appointmentDetails;

    public ContactAppointmentsAdapter(Activity actvty, ArrayList<AppointmentsModel> rl) {

        this.mEventsList = rl;
        this.mActivity = actvty;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.contact_appointment_list_structure, parent, false);

        return new ContactAppointmentsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        AppointmentsModel model = mEventsList.get(position);

        final TextView txtRowName = holder.txtRowName;
        TextView txtRowTimeLeft = holder.txtRowTimeLeft;
        TextView txtAptAgenda = holder.txtAptAgenda;
        LinearLayout btnSetAppointment = holder.btnSetAppointment;

        txtRowName.setText(model.getAppointmentTitle());
        txtRowTimeLeft.setText(model.getAppointmentDate());
        txtAptAgenda.setText(model.getAgenda());

        btnSetAppointment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String tag = "aptDetailsTag";
                appointmentDetails = FragmentAppointmentDetails.newInstance((String)txtRowName.getText(), holder.getAdapterPosition());

                FragmentManager fragmentManager = ((ContactDetailsActivity) mActivity).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                transaction.add(android.R.id.content, appointmentDetails, tag)
                        .addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mEventsList.size();
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

        TextView txtRowName;
        TextView txtRowTimeLeft;
        TextView txtAptAgenda;
        LinearLayout btnSetAppointment;

        ViewHolder(View itemView) {
            super(itemView);
            txtRowName = itemView.findViewById(R.id.txtAppointmentTitle);
            txtRowTimeLeft = itemView.findViewById(R.id.txtDaysLeft);
            txtAptAgenda = itemView.findViewById(R.id.txtAgenda);
            btnSetAppointment = itemView.findViewById(R.id.btnAppointmentsRow);

        }
    }

}
