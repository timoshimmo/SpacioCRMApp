package com.android.spaciocrm.home.activity.contacts.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.spaciocrm.R;
import com.android.spaciocrm.util.adapter.ContactAppointmentsAdapter;
import com.android.spaciocrm.util.model.AppointmentsModel;
import com.android.spaciocrm.util.model.DividerItemDecoration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class FragmentContactAppointments extends Fragment {

    private static final String ARG_SECTION_NAME = "section_name";
    private static final String ARG_SELECTED_CONTACT = "selected_contact";

    String selectedContactName;

    protected ArrayList<AppointmentsModel> rowList;

    AppointmentsModel appModel;

    JSONArray appointmentsArray;
    JSONObject aptsObj;
    String appointments;

    public FragmentContactAppointments() {
        // Required empty public constructor
    }


    public static FragmentContactAppointments newInstance(String sectionName, String selectedCName) {
        FragmentContactAppointments fragment = new FragmentContactAppointments();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_NAME, sectionName);
        args.putString(ARG_SELECTED_CONTACT, selectedCName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedContactName = getArguments().getString(ARG_SELECTED_CONTACT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_contact_appointments, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.rvContactsAppointments);

        initDataset();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ContactAppointmentsAdapter adapter = new ContactAppointmentsAdapter(getActivity(), rowList);
        recyclerView.setAdapter(adapter);


        return rootView;

    }

    private void initDataset() {

        rowList = new ArrayList<>();

        SharedPreferences appointmentValues = getActivity().getSharedPreferences("PREFAPTLIST", Context.MODE_PRIVATE);
        appointments = appointmentValues.getString("appointmentsData", "[]");

        try {

            appointmentsArray = new JSONArray(appointments);

            for(int i=0; i < appointmentsArray.length(); i++) {
                aptsObj = appointmentsArray.getJSONObject(i);
                String contactName = aptsObj.getString("CONTACT_NAME");

                if(contactName.equals(selectedContactName.trim())) {
                    String date = aptsObj.getString("DATE_OF_APPOINTMENT");
                    String agenda = aptsObj.getString("AGENDA_OF_APPOINTMENT");
                    appModel = new AppointmentsModel(contactName, date, agenda);

                    rowList.add(appModel);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
