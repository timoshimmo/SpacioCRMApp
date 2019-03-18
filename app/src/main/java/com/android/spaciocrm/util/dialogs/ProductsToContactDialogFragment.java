package com.android.spaciocrm.util.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.spaciocrm.R;
import com.android.spaciocrm.util.adapter.BasicSpinnerAdapter;
import com.android.spaciocrm.util.model.ContactProductsModel;
import com.android.spaciocrm.util.model.ProductsModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by freshfuturesmy on 05/02/18.
 */

public class ProductsToContactDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private static final String ARG_CONTACT_NAME = "contact_name";
    private static final String ARG_SELECTED_ROW = "selected_row";

    String mContactName;
    int selectedRow;

    EditText txtContactName;
    Spinner spnProducts;

    String products;
    String contactProducts;

    ArrayList<ProductsModel> productList;
    ArrayList<ContactProductsModel> contactProductList;

    ArrayList<String> productSelection;
    ProductsModel productsModel;
    ContactProductsModel contactProductsModel;

    String contactName;
    String[] getProductTitle;
    String productName;

    int indexUpdate = 0;
    JSONObject contactProductObjs;
    JSONArray allContactProductsArray;
    JSONArray contactsProductsArray;

    String selectedProduct;

    Dialog d;

    boolean available = false;

    public ProductsToContactDialogFragment() {
        // Required empty public constructor
    }


    public static ProductsToContactDialogFragment newInstance(String contactName, int row) {
        ProductsToContactDialogFragment fragment = new ProductsToContactDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CONTACT_NAME, contactName);
        args.putInt(ARG_SELECTED_ROW, row);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
        if (getArguments() != null) {
            mContactName = getArguments().getString(ARG_CONTACT_NAME);
            selectedRow = getArguments().getInt(ARG_SELECTED_ROW);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.add_product_to_contact_layout, container, false);

        txtContactName = rootView.findViewById(R.id.txtPrdtCntContactName);
        spnProducts = rootView.findViewById(R.id.spnProducts);

        Button btnSave = rootView.findViewById(R.id.btnSaveProductToContact);
        ImageButton btnClose = rootView.findViewById(R.id.btnCloseProductToContact);

        productList = new ArrayList<>();
        contactProductList = new ArrayList<>();
        productSelection = new ArrayList<>();

        fetchProducts();
        fetchContactProducts();

        for(int i=0; i < productList.size(); i++) {
            productsModel = productList.get(i);

            String title = productsModel.getProductTitle();
            productSelection.add(title);
        }

        txtContactName.setText(mContactName);
        spnProducts.setOnItemSelectedListener(this);

        BasicSpinnerAdapter productsAdapter = new BasicSpinnerAdapter(getActivity(), productSelection);
        spnProducts.setAdapter(productsAdapter);

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for(int i=0; i < contactProductList.size(); i++) {

                    contactProductsModel = contactProductList.get(i);
                    contactName = contactProductsModel.getContactName();
                    indexUpdate = i;

                    if(contactName.equals(mContactName)) {
                        getProductTitle = contactProductsModel.getProducts();

                        for (String aGetProductTitle : getProductTitle) {
                            productName = aGetProductTitle;

                            if (selectedProduct.equals(productName)) {
                                available = true;
                            }
                        }
                    }
                }

                if(available) {
                    Toast.makeText(getActivity(), "This contact already has this product", Toast.LENGTH_LONG).show();
                }
                else {

                    try {
                        allContactProductsArray = new JSONArray(contactProducts);

                        contactProductObjs = allContactProductsArray.getJSONObject(selectedRow);
                        contactsProductsArray = contactProductObjs.getJSONArray("PRODUCTS");

                        contactsProductsArray.put(selectedProduct);
                        contactProductObjs.put("PRODUCTS", contactsProductsArray);

                        allContactProductsArray.put(selectedRow, contactProductObjs);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    SharedPreferences prefContactProductList = getActivity().getSharedPreferences("PREFCONTACTPRODUCTSLIST", Context.MODE_PRIVATE);
                    SharedPreferences.Editor listEditor = prefContactProductList.edit();
                    listEditor.putString("contactProductsData", allContactProductsArray.toString());
                    listEditor.apply();

                    Toast.makeText(getActivity(), "Product Successfully added to Contact", Toast.LENGTH_LONG).show();
                    Dismiss();
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.cancel();
            }
        });

        return  rootView;

    }

    public void Dismiss() {
        DialogFragment dialogFragment = (DialogFragment)getFragmentManager().findFragmentByTag("ContactProductsFragment");
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }

    private void fetchProducts() {

        ArrayList<ProductsModel> items;

        SharedPreferences getProdcutsValue = getActivity().getSharedPreferences("PREFPRODUCTLIST", Context.MODE_PRIVATE);
        products = getProdcutsValue.getString("productsData", "[]");

        items = new Gson().fromJson(products, new TypeToken<ArrayList<ProductsModel>>() {
        }.getType());

        productList.clear();
        productList.addAll(items);

    }

    private void fetchContactProducts() {

        String json;
        ArrayList<ContactProductsModel> items = null;

        SharedPreferences getContactProductsValues = getActivity().getSharedPreferences("PREFCONTACTPRODUCTSLIST", Context.MODE_PRIVATE);
        contactProducts = getContactProductsValues.getString("contactProductsData", "[]");

        if(contactProducts.equals("[]")) {

            try {

                if(getActivity() != null) {
                    InputStream is = getActivity().getAssets().open("contact_product/contacts_products.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");

                    SharedPreferences prefContactProductList = getActivity().getSharedPreferences("PREFCONTACTPRODUCTSLIST", Context.MODE_PRIVATE);
                    SharedPreferences.Editor listEditor = prefContactProductList.edit();
                    listEditor.putString("contactProductsData", json);
                    listEditor.apply();

                    items = new Gson().fromJson(json, new TypeToken<ArrayList<ContactProductsModel>>() {
                    }.getType());

                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

        else {
            items = new Gson().fromJson(contactProducts, new TypeToken<ArrayList<ContactProductsModel>>() {
            }.getType());
        }

        contactProductList.clear();
        contactProductList.addAll(items);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedProduct = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
