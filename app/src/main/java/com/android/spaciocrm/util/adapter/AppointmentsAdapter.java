package com.android.spaciocrm.util.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.spaciocrm.R;
import com.android.spaciocrm.home.activity.HomeActivity;
import com.android.spaciocrm.home.fragments.appointments.FragmentAppointmentDetails;
import com.android.spaciocrm.util.model.AppointmentsModel;

import java.util.ArrayList;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

    private ArrayList<AppointmentsModel> mEventsList;
    private ArrayList<AppointmentsModel> eventsFiltered;
    private Activity mActivity;

    private FragmentAppointmentDetails appointmentDetails;
    private String filterValue;

    public AppointmentsAdapter(Activity actvty, ArrayList<AppointmentsModel> rl) {
        this.mEventsList = rl;
        this.eventsFiltered = rl;
        this.mActivity = actvty;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.appointments_list_structure, parent, false);

        return new AppointmentsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        AppointmentsModel model = eventsFiltered.get(position);

        final TextView txtRowName = holder.txtRowName;
        TextView txtRowTimeLeft = holder.txtRowTimeLeft;
        TextView txtRowContactName = holder.txtRowContactName;
        LinearLayout btnSetAppointment = holder.btnSetAppointment;

        txtRowName.setText(model.getAppointmentTitle());
        txtRowTimeLeft.setText(model.getAppointmentDate());
        txtRowContactName.setText(model.getContactName());

        btnSetAppointment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String tag = "aptDetailsTag";
                appointmentDetails = FragmentAppointmentDetails.newInstance((String)txtRowName.getText(), holder.getAdapterPosition());

                FragmentManager fragmentManager = ((HomeActivity) mActivity).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                transaction.add(android.R.id.content, appointmentDetails, tag)
                        .addToBackStack(null).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return eventsFiltered.size();
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
        TextView txtRowContactName;
        LinearLayout btnSetAppointment;

        ViewHolder(View itemView) {
            super(itemView);
            txtRowName = itemView.findViewById(R.id.txtAppointmentTitle);
            txtRowTimeLeft = itemView.findViewById(R.id.txtDaysLeft);
            txtRowContactName = itemView.findViewById(R.id.txtAppointmentContact);
            btnSetAppointment = itemView.findViewById(R.id.btnAppointmentsRow);

        }
    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    eventsFiltered = mEventsList;
                }
                else {

                    ArrayList<AppointmentsModel> filteredList = new ArrayList<>();

                    SharedPreferences getFilterValue = mActivity.getSharedPreferences("PREFAPTFILTER", Context.MODE_PRIVATE);
                    filterValue = getFilterValue.getString("APTFILTER", "");

                    if(filterValue.equals("Title")) {
                        for (AppointmentsModel row : mEventsList) {

                            if (row.getAppointmentTitle().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }

                        }
                    }

                    if(filterValue.equals("Contact Name")) {
                        for (AppointmentsModel row : mEventsList) {

                            if (row.getContactName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }

                        }
                    }

                    if(filterValue.equals("Date")) {
                        for (AppointmentsModel row : mEventsList) {

                            if (row.getAppointmentDate().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }

                        }
                    }


                    if(filterValue.equals("City")) {
                        for (AppointmentsModel row : mEventsList) {

                            if (row.getAppointmentCity().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }

                        }
                    }

                    if(filterValue.equals("State")) {
                        for (AppointmentsModel row : mEventsList) {

                            if (row.getAppointmentState().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }

                        }
                    }


                    eventsFiltered = filteredList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = eventsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                eventsFiltered = (ArrayList<AppointmentsModel>) results.values;
                notifyDataSetChanged();
            }
        };

    }

}
