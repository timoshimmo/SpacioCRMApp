package com.android.spaciocrm.home.fragments.referrals;


import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.spaciocrm.R;
import com.android.spaciocrm.util.adapter.OutgoingAdapter;
import com.android.spaciocrm.util.model.DividerItemDecoration;
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
 * A simple {@link Fragment} subclass.
 */
public class FragOutgoing extends Fragment {

    String json;
    String outgoingReferral;

    JSONArray allReferralsArray;
    JSONArray outgoingReferralsArray;
    JSONObject outgoingReferralsObj;

    private ArrayList<ReferralModel> mOutgoingList;

    LinearLayout notFound;
    RecyclerView recyclerView;

    OutgoingAdapter adapter;

    public FragOutgoing() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outgoing, container, false);

        notFound = view.findViewById(R.id.not_found_layout);
        recyclerView = view.findViewById(R.id.rvOutgoing);

        mOutgoingList = new ArrayList<>();
        adapter = new OutgoingAdapter(getActivity(), mOutgoingList);

        fetchOutgoing();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.products_menu_search, menu);

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

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchOutgoing() {

        ArrayList<ReferralModel> items = null;

        if(notFound.getVisibility() == View.VISIBLE) {
            notFound.setVisibility(View.GONE);
        }

        if(recyclerView.getVisibility() == View.GONE) {
            recyclerView.setVisibility(View.VISIBLE);
        }

        SharedPreferences getFilterValue = getActivity().getSharedPreferences("PREFOUTGOINGLIST", Context.MODE_PRIVATE);
        outgoingReferral = getFilterValue.getString("outgoingData", "[]");

        if(outgoingReferral.equals("[]")) {

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

                    SharedPreferences prefContacts = getActivity().getSharedPreferences("PREFOUTGOINGLIST", Context.MODE_PRIVATE);
                    SharedPreferences.Editor contactsEditor = prefContacts.edit();
                    contactsEditor.putString("outgoingData", outgoingReferralsArray.toString());
                    contactsEditor.apply();

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
            items = new Gson().fromJson(outgoingReferral, new TypeToken<ArrayList<ReferralModel>>() {
            }.getType());
        }

        mOutgoingList.clear();
        mOutgoingList.addAll(items);

        adapter.notifyDataSetChanged();

    }

}
