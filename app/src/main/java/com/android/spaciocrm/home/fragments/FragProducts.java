package com.android.spaciocrm.home.fragments;


import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

import com.android.spaciocrm.R;
import com.android.spaciocrm.home.activity.HomeActivity;
import com.android.spaciocrm.util.adapter.ProductsAdapter;
import com.android.spaciocrm.util.dialogs.NewProductDailogFragment;
import com.android.spaciocrm.util.model.ContactsModel;
import com.android.spaciocrm.util.model.DividerItemDecoration;
import com.android.spaciocrm.util.model.ProductsModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FragProducts extends Fragment {

    private static final String ARG_TITLE = "productTitle";
    private String mTitle;

    protected ArrayList<ProductsModel> productList;
    ProductsAdapter adapter;

    public FragProducts() {
    }


    public static FragProducts newInstance(String title) {
        FragProducts fragment = new FragProducts();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        Toolbar toolbar = view.findViewById(R.id.tbProducts);
        if (getActivity() != null) {
            ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        }

        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(mTitle);

        productList = new ArrayList<>();
        adapter = new ProductsAdapter(getActivity(), productList);

        fetchProducts();

        FloatingActionButton btnAdd = view.findViewById(R.id.btnAddProduct);

        RecyclerView recyclerView = view.findViewById(R.id.rvProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewProductDailogFragment newProductDailogFragment = new NewProductDailogFragment();
                newProductDailogFragment.show(getActivity().getSupportFragmentManager(),"NewProductFragment");
            }
        });

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

    private void fetchProducts() {

        String json;
        String products;

        ArrayList<ProductsModel> items = null;

        SharedPreferences getFilterValue = getActivity().getSharedPreferences("PREFPRODUCTLIST", Context.MODE_PRIVATE);
        products = getFilterValue.getString("productsData", "[]");

        if(products.equals("[]")) {

            try {
                if(getActivity() != null) {
                    InputStream is = getActivity().getAssets().open("product/products.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");

                    SharedPreferences prefProducts = getActivity().getSharedPreferences("PREFPRODUCTLIST", Context.MODE_PRIVATE);
                    SharedPreferences.Editor productsEditor = prefProducts.edit();
                    productsEditor.putString("productsData", json);
                    productsEditor.apply();

                    items = new Gson().fromJson(json, new TypeToken<ArrayList<ProductsModel>>() {
                    }.getType());

                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

        else {
            items = new Gson().fromJson(products, new TypeToken<ArrayList<ProductsModel>>() {
            }.getType());
        }

        productList.clear();
        productList.addAll(items);

        adapter.notifyDataSetChanged();

    }

}