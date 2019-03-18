package com.android.spaciocrm.home.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.android.spaciocrm.R;
import com.android.spaciocrm.home.activity.HomeActivity;
import com.android.spaciocrm.home.fragments.contacts.FragmentMultipleMarkersMap;
import com.android.spaciocrm.home.fragments.contacts.FragmentNewContact;
import com.android.spaciocrm.util.adapter.ContactsAdapter;
import com.android.spaciocrm.util.model.ContactsModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.Manifest.permission.READ_CONTACTS;

public class FragContacts extends Fragment {

    private static final String ARG_TITLE = "contactTitle";
    private static final int PICK_CONTACT = 10;

    private String mTitle;
    protected ArrayList<ContactsModel> contactsList;
    ContactsAdapter adapter;

    Context context;

    private FragmentNewContact fragNewContact;
    private FragmentMultipleMarkersMap fragmentMultipleMarkersMap;

    private ArrayList<String> mAddresses;
    private ArrayList<String> mTitles;

    public static AlertDialog cSelectionTypeAlertDialog;
    public static AlertDialog cFilterAlertDialog;

    public static AlertDialog cTypePopupAlertDialog;

    private static final int REQUEST_READ_CONTACTS = 0;

    Button btnDialogCancel;
    Button btnDialogOk;

    private View mProgressView;
    private View mContactsView;

    RecyclerView recyclerView;

    public FragContacts() {
        // Required empty public constructor
    }

    public static FragContacts newInstance(String title) {
        FragContacts fragment = new FragContacts();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        Toolbar toolbar = view.findViewById(R.id.tbContacts);
        if (getActivity() != null) {
            ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        }
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(mTitle);

        context = getActivity();

        FloatingActionButton btnAddNew = view.findViewById(R.id.btnFloatingAddContact);
        FloatingActionButton btnAllMap = view.findViewById(R.id.btnFloatingAllMarkersMap);

        mProgressView = view.findViewById(R.id.multiple_location_progress);
     //   mContactsView = view.findViewById(R.id.contacts_body);

        contactsList = new ArrayList<>();
        adapter = new ContactsAdapter(getActivity(), contactsList);

        contactTypePopupDialog();

        SharedPreferences getFilterValue = getActivity().getSharedPreferences("PREFLOGINSTATUS", Context.MODE_PRIVATE);
        int loginStatusNo = getFilterValue.getInt("loginStatus", 0);

        if(loginStatusNo == 0) {

            cTypePopupAlertDialog.show();

            SharedPreferences prefLoginStatus = getActivity().getSharedPreferences("PREFLOGINSTATUS", Context.MODE_PRIVATE);
            SharedPreferences.Editor listEditor = prefLoginStatus.edit();
            listEditor.putInt("loginStatus", 1);
            listEditor.apply();
        }

        fetchContacts();

        recyclerView = view.findViewById(R.id.rvContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

      /*  RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration); */

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        fetchMapData();

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cSelectionTypeAlertDialog.show();

            }
        });

        btnAllMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressView.setVisibility(View.VISIBLE);

                String tag = "MARKERS_MAP";
                fragmentMultipleMarkersMap = FragmentMultipleMarkersMap.newInstance(mAddresses, mTitles);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                transaction.add(android.R.id.content, fragmentMultipleMarkersMap, tag)
                        .addToBackStack(null).commit();

                mProgressView.setVisibility(View.GONE);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        contactSelectionTypeDialog();
        contactFilterDialog();
    }

    public void contactSelectionTypeDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflaters = (getActivity()).getLayoutInflater();

        View dialogView = inflaters.inflate(R.layout.contact_selection_dialog_layout, null);
        builder.setView(dialogView);

        final RadioGroup cSelectionGroup = dialogView.findViewById(R.id.rgDialogContactSelection);
        final RadioButton rbNewContact =  dialogView.findViewById(R.id.rbContactSelectionNewContact);
        final RadioButton rbExistingContact =  dialogView.findViewById(R.id.rbDialogExistingContact);

        btnDialogCancel = dialogView.findViewById(R.id.btnCancelContactSelectionType);
        btnDialogOk = dialogView.findViewById(R.id.btnConfirmContactSelectionType);

        cSelectionTypeAlertDialog = builder.create();

        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cSelectionTypeAlertDialog.cancel();
            }
        });

        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = cSelectionGroup.getCheckedRadioButtonId();

                if(selectedId == rbNewContact.getId()) {

                    String tag = "NEW_CONTACT";
                    fragNewContact = FragmentNewContact.newInstance("","");

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                    transaction.add(android.R.id.content, fragNewContact, tag)
                            .addToBackStack(null).commit();

                    cSelectionTypeAlertDialog.dismiss();

                }
                else if(selectedId == rbExistingContact.getId()) {

                    populateContactList();
                    cSelectionTypeAlertDialog.dismiss();

                }
                else {
                    Toast.makeText(getActivity(), "No selection found.", Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    public void contactFilterDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflaters = (getActivity()).getLayoutInflater();

        View dialogView = inflaters.inflate(R.layout.contact_filter_dialog_layout, null);
        builder.setView(dialogView);

        final RadioGroup cFilterGroup = dialogView.findViewById(R.id.rgDialogContactFilter);
        final RadioButton rbLastname =  dialogView.findViewById(R.id.rbCDialogLastname);
        final RadioButton rbContactType =  dialogView.findViewById(R.id.rbCDialogCType);
        final RadioButton rbCompany =  dialogView.findViewById(R.id.rbCDialogCompany);
        final RadioButton rbCity =  dialogView.findViewById(R.id.rbCdialogCity);
        final RadioButton rbState =  dialogView.findViewById(R.id.rbCDialogState);
        final RadioButton rbEmail =  dialogView.findViewById(R.id.rbCDialogEmail);

        Button btnFilterDialogOk = dialogView.findViewById(R.id.btnConfirmCFilter);

        cFilterAlertDialog = builder.create();

        btnFilterDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = cFilterGroup.getCheckedRadioButtonId();

                if(selectedId == rbLastname.getId()) {
                    SharedPreferences prefContactFilter = getActivity().getSharedPreferences("PREFCONTACTFILTER", Context.MODE_PRIVATE);
                    SharedPreferences.Editor filterEditor = prefContactFilter.edit();
                    filterEditor.putString("CFILTER", "Last Name");
                    filterEditor.apply();

                }

                if(selectedId == rbContactType.getId()) {
                    SharedPreferences prefContactFilter = getActivity().getSharedPreferences("PREFCONTACTFILTER", Context.MODE_PRIVATE);
                    SharedPreferences.Editor filterEditor = prefContactFilter.edit();
                    filterEditor.putString("CFILTER", "Contact Type");
                    filterEditor.apply();

                }

                if(selectedId == rbCompany.getId()) {
                    SharedPreferences prefContactFilter = getActivity().getSharedPreferences("PREFCONTACTFILTER", Context.MODE_PRIVATE);
                    SharedPreferences.Editor filterEditor = prefContactFilter.edit();
                    filterEditor.putString("CFILTER", "Company");
                    filterEditor.apply();

                }

                if(selectedId == rbCity.getId()) {
                    SharedPreferences prefContactFilter = getActivity().getSharedPreferences("PREFCONTACTFILTER", Context.MODE_PRIVATE);
                    SharedPreferences.Editor filterEditor = prefContactFilter.edit();
                    filterEditor.putString("CFILTER", "City");
                    filterEditor.apply();

                }

                if(selectedId == rbState.getId()) {
                    SharedPreferences prefContactFilter = getActivity().getSharedPreferences("PREFCONTACTFILTER", Context.MODE_PRIVATE);
                    SharedPreferences.Editor filterEditor = prefContactFilter.edit();
                    filterEditor.putString("CFILTER", "State");
                    filterEditor.apply();

                }

                if(selectedId == rbEmail.getId()) {
                    SharedPreferences prefContactFilter = getActivity().getSharedPreferences("PREFCONTACTFILTER", Context.MODE_PRIVATE);
                    SharedPreferences.Editor filterEditor = prefContactFilter.edit();
                    filterEditor.putString("CFILTER", "Email");
                    filterEditor.apply();

                }

                cFilterAlertDialog.dismiss();
            }
        });

    }

    public void contactTypePopupDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflaters = (getActivity()).getLayoutInflater();

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

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data){
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch(reqCode) {

                case (PICK_CONTACT):
                    Uri contactData = data.getData();
                    Cursor cursor = getActivity().getContentResolver()
                        .query(contactData,null, null, null,
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

                    if(cursor != null) {
                        if (cursor.moveToFirst()) {
                            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                            if (hasPhone.equalsIgnoreCase("1")) {

                                String cNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                String nameContact = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                                fragNewContact = FragmentNewContact.newInstance(nameContact, cNumber);

                                String tag = "NEW_CONTACT";
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                                transaction.add(android.R.id.content, fragNewContact, tag)
                                        .addToBackStack(null).commit();


                            }
                        }
                    }
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mContactsView.setVisibility(show ? View.GONE : View.VISIBLE);
            mContactsView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mContactsView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mContactsView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void populateContactList() {
        if (!mayRequestContacts()) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(btnDialogOk, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);

            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.contact_menu_search, menu);

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
                cFilterAlertDialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchContacts() {

        String json;
        String contacts;

        ArrayList<ContactsModel> items = null;

        SharedPreferences getFilterValue = getActivity().getSharedPreferences("PREFCONTACTLIST", Context.MODE_PRIVATE);
        contacts = getFilterValue.getString("contactData", "[]");

        if(contacts.equals("[]")) {

            try {
                if(getActivity() != null) {
                    InputStream is = getActivity().getAssets().open("contact/contacts.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");

                    SharedPreferences prefContacts = getActivity().getSharedPreferences("PREFCONTACTLIST", Context.MODE_PRIVATE);
                    SharedPreferences.Editor contactsEditor = prefContacts.edit();
                    contactsEditor.putString("contactData", json);
                    contactsEditor.apply();

                    items = new Gson().fromJson(json, new TypeToken<ArrayList<ContactsModel>>() {
                    }.getType());

                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else {

            items = new Gson().fromJson(contacts, new TypeToken<ArrayList<ContactsModel>>() {
            }.getType());

        }

        contactsList.clear();
        contactsList.addAll(items);

        adapter.notifyDataSetChanged();

    }

    private void fetchMapData() {

        mAddresses = new ArrayList<>();
        mTitles = new ArrayList<>();

        for(int i=0; i < contactsList.size(); i++) {

            ContactsModel model = contactsList.get(i);

            String fillAddress = model.getContactAddresse() +", " + model.getLocationCity() + " " + model.getLocationCountry();
            mAddresses.add(i, fillAddress);
            mTitles.add(i, model.getContactFirstName() + " " + model.getContactLastName());

        }
    }

}