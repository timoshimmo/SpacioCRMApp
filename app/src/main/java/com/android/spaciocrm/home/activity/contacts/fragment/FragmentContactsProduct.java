package com.android.spaciocrm.home.activity.contacts.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.spaciocrm.R;
import com.android.spaciocrm.util.adapter.ContactProductsAdapter;
import com.android.spaciocrm.util.dialogs.ProductsToContactDialogFragment;
import com.android.spaciocrm.util.model.AppointmentsModel;
import com.android.spaciocrm.util.model.ContactProductsModel;
import com.android.spaciocrm.util.model.DividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class FragmentContactsProduct extends Fragment {

    private static final String ARG_CONTACT_NAME = "contact_name";
    private static final String ARG_SELECTED_ROW = "selected_row";

    String mContactName;
    int selectedRow;
    protected ArrayList<String> productList;
    String contactProducts;
    ArrayList<String> productDesc;

    ContactProductsAdapter adapter;

    JSONArray conprArr;
    JSONObject conprObj;

    JSONArray prArrs;
    JSONObject prObj;

    String products;

    public FragmentContactsProduct() {
        // Required empty public constructor
    }

    public static FragmentContactsProduct newInstance(String contactName, int row) {
        FragmentContactsProduct fragment = new FragmentContactsProduct();
        Bundle args = new Bundle();
        args.putString(ARG_CONTACT_NAME, contactName);
        args.putInt(ARG_SELECTED_ROW, row);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContactName = getArguments().getString(ARG_CONTACT_NAME);
            selectedRow = getArguments().getInt(ARG_SELECTED_ROW);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_contacts_product, container, false);

        FloatingActionButton btnAddContactsProduct = rootView.findViewById(R.id.btnAddContactProduct);

        fetchContactProducts();

        RecyclerView recyclerView = rootView.findViewById(R.id.rvContactsProduct);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new ContactProductsAdapter(productList, productDesc);
        recyclerView.setAdapter(adapter);

        btnAddContactsProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductsToContactDialogFragment productsToContactDialogFragment = ProductsToContactDialogFragment.newInstance(mContactName, selectedRow);
                productsToContactDialogFragment.show(getActivity().getSupportFragmentManager(),"ContactProductsFragment");
            }
        });

        return rootView;
    }

    private void fetchContactProducts() {

        productList = new ArrayList<>();
        productDesc = new ArrayList<>();

        String json;

        SharedPreferences productsValues = getActivity().getSharedPreferences("PREFCONTACTPRODUCTSLIST", Context.MODE_PRIVATE);
        contactProducts = productsValues.getString("contactProductsData", "[]");

        SharedPreferences getFilterValue = getActivity().getSharedPreferences("PREFPRODUCTLIST", Context.MODE_PRIVATE);
        products = getFilterValue.getString("productsData", "[]");

        if(contactProducts.equals("[]")) {
            try {
                if(getActivity() != null) {
                    InputStream is = getActivity().getAssets().open("contact_product/contacts_products.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");

                    conprArr = new JSONArray(json);

                    SharedPreferences prefContactProductList = getActivity().getSharedPreferences("PREFCONTACTPRODUCTSLIST", Context.MODE_PRIVATE);
                    SharedPreferences.Editor listEditor = prefContactProductList.edit();
                    listEditor.putString("contactProductsData", json);
                    listEditor.apply();

                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                conprArr = new JSONArray(contactProducts);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {

            prArrs = new JSONArray(products);

            for(int i=0; i < conprArr.length(); i++) {

                conprObj = conprArr.getJSONObject(i);
                String contactName = conprObj.getString("CONTACT_NAME");

                if(contactName.equals(mContactName)) {

                    JSONArray prArr = conprObj.getJSONArray("PRODUCTS");

                    for(int j=0; j < prArr.length(); j++) {

                        String productsName = prArr.get(j).toString();

                        for(int k=0; k < prArrs.length(); k++) {

                            prObj = prArrs.getJSONObject(k);
                            String productTitle = prObj.getString("PRODUCT_TITLE");

                            if(productsName.equals(productTitle)) {
                                String getDesc = prObj.getString("PRODUCT_DESCRIPTION");
                                productDesc.add(getDesc);
                            }

                        }
                        productList.add(productsName);
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
