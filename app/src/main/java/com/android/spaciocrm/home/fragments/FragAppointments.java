package com.android.spaciocrm.home.fragments;


import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.spaciocrm.R;
import com.android.spaciocrm.home.activity.HomeActivity;
import com.android.spaciocrm.home.fragments.appointments.FragmentNewAppointment;
import com.android.spaciocrm.util.adapter.AppointmentsAdapter;
import com.android.spaciocrm.util.model.AppointmentsModel;
import com.android.spaciocrm.util.model.DividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class FragAppointments extends Fragment {

    private static final String ARG_TITLE = "appointmentTitle";
    private String mTitle;
    protected ArrayList<AppointmentsModel> aptList;

    public AppointmentsAdapter adapter;
    FragmentNewAppointment fragmentNewAppointment;
    public static AlertDialog aptFilterAlertDialog;


    public FragAppointments() {
        // Required empty public constructor
    }


    public static FragAppointments newInstance(String title) {
        FragAppointments fragment = new FragAppointments();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_appointments, container, false);

        Toolbar toolbar = view.findViewById(R.id.tbAppointments);
        if (getActivity() != null) {
            ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        }

        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(mTitle);

        FloatingActionButton btnAddNew = view.findViewById(R.id.btnFloatingNewAppointment);

        aptList = new ArrayList<>();
        adapter = new AppointmentsAdapter(getActivity(), aptList);

        fetchAppointments();

        RecyclerView recyclerView = view.findViewById(R.id.rvAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.setAdapter(adapter);

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tag = "NEW_APPOINTMENT";
                fragmentNewAppointment = new FragmentNewAppointment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                transaction.add(android.R.id.content, fragmentNewAppointment, tag)
                        .addToBackStack(null).commit();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        contactFilterDialog();
    }

    public void contactFilterDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflaters = (getActivity()).getLayoutInflater();

        View dialogView = inflaters.inflate(R.layout.appointment_filter_dialog_layout, null);
        builder.setView(dialogView);

        final RadioGroup aptFilterGroup = dialogView.findViewById(R.id.rgDialogAptFilter);
        final RadioButton rbTitle =  dialogView.findViewById(R.id.rbADialogTitle);
        final RadioButton rbContactName =  dialogView.findViewById(R.id.rbADialogContactName);
        final RadioButton rbDate =  dialogView.findViewById(R.id.rbADialogDate);
        final RadioButton rbCity =  dialogView.findViewById(R.id.rbADialogCity);
        final RadioButton rbState =  dialogView.findViewById(R.id.rbADialogState);

        Button btnAptDialogOk = dialogView.findViewById(R.id.btnConfirmAFilter);

        aptFilterAlertDialog = builder.create();

        btnAptDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = aptFilterGroup.getCheckedRadioButtonId();

                if(selectedId == rbTitle.getId()) {
                    SharedPreferences prefAptFilter = getActivity().getSharedPreferences("PREFAPTFILTER", Context.MODE_PRIVATE);
                    SharedPreferences.Editor filterEditor = prefAptFilter.edit();
                    filterEditor.putString("APTFILTER", "Title");
                    filterEditor.apply();

                }

                if(selectedId == rbContactName.getId()) {
                    SharedPreferences prefAptFilter = getActivity().getSharedPreferences("PREFAPTFILTER", Context.MODE_PRIVATE);
                    SharedPreferences.Editor filterEditor = prefAptFilter.edit();
                    filterEditor.putString("APTFILTER", "Contact Name");
                    filterEditor.apply();

                }

                if(selectedId == rbDate.getId()) {
                    SharedPreferences prefAptFilter = getActivity().getSharedPreferences("PREFAPTFILTER", Context.MODE_PRIVATE);
                    SharedPreferences.Editor filterEditor = prefAptFilter.edit();
                    filterEditor.putString("APTFILTER", "Date");
                    filterEditor.apply();

                }

                if(selectedId == rbCity.getId()) {
                    SharedPreferences prefAptFilter = getActivity().getSharedPreferences("PREFAPTFILTER", Context.MODE_PRIVATE);
                    SharedPreferences.Editor filterEditor = prefAptFilter.edit();
                    filterEditor.putString("APTFILTER", "City");
                    filterEditor.apply();

                }

                if(selectedId == rbState.getId()) {
                    SharedPreferences prefAptFilter = getActivity().getSharedPreferences("PREFAPTFILTER", Context.MODE_PRIVATE);
                    SharedPreferences.Editor filterEditor = prefAptFilter.edit();
                    filterEditor.putString("APTFILTER", "State");
                    filterEditor.apply();

                }

                aptFilterAlertDialog.dismiss();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.appointment_menu_search, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search)
                .getActionView();

        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.app_bar_filter:
                aptFilterAlertDialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchAppointments() {

        String json;
        String appointments;

        ArrayList<AppointmentsModel> items = null;

        SharedPreferences appointmentList = getActivity().getSharedPreferences("PREFAPTLIST", Context.MODE_PRIVATE);
        appointments = appointmentList.getString("appointmentsData", "[]");

        if(appointments.equals("[]")) {

            try {
                if(getActivity() != null) {
                    InputStream is = getActivity().getAssets().open("appointments/appointments.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");

                    SharedPreferences prefApts = getActivity().getSharedPreferences("PREFAPTLIST", Context.MODE_PRIVATE);
                    SharedPreferences.Editor aptsEditor = prefApts.edit();
                    aptsEditor.putString("appointmentsData", json);
                    aptsEditor.apply();

                    items = new Gson().fromJson(json, new TypeToken<ArrayList<AppointmentsModel>>() {
                    }.getType());

                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else {

            items = new Gson().fromJson(appointments, new TypeToken<ArrayList<AppointmentsModel>>() {
            }.getType());

        }

        aptList.clear();
        aptList.addAll(items);

        adapter.notifyDataSetChanged();

    }

}
