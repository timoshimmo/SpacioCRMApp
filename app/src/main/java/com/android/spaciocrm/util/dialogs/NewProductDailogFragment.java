package com.android.spaciocrm.util.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.spaciocrm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by freshfuturesmy on 05/02/18.
 */

public class NewProductDailogFragment extends DialogFragment {

    EditText txtProfileTitle;
    EditText txtProfileDesc;
    EditText txtEmail;

    String products;
    JSONArray contactsArray;

    Dialog d;

    public NewProductDailogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.add_new_product_layout, container, false);

        txtProfileTitle = rootView.findViewById(R.id.txtNewProductTitleValue);
        txtProfileDesc = rootView.findViewById(R.id.txtProductDescValue);
        txtEmail = rootView.findViewById(R.id.txtProductEmailValue);

        Button btnSave = rootView.findViewById(R.id.btnSaveNewProduct);
        ImageButton btnClose = rootView.findViewById(R.id.btnCloseNewProduct);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtProfileTitle.getText().toString().equals("") || txtProfileDesc.getText().toString().equals("") ||
                        txtEmail.getText().toString().equals("")) {

                    Snackbar.make(rootView, "All Inputs are required!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    updateProduct();
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

        return rootView;
    }

    private void updateProduct() {

        SharedPreferences getFilterValue = getActivity().getSharedPreferences("PREFPRODUCTLIST", Context.MODE_PRIVATE);
        products = getFilterValue.getString("productsData", "[]");

        try {
            contactsArray = new JSONArray(products);
            JSONObject productObj = new JSONObject();

            productObj.put("PRODUCT_TITLE", txtProfileTitle.getText().toString());
            productObj.put("PRODUCT_DESCRIPTION", txtProfileDesc.getText().toString());
            productObj.put("PRODUCT_EMAIL", txtEmail.getText().toString());

            contactsArray.put(contactsArray.length(), productObj);

            SharedPreferences prefContactProductList = getActivity().getSharedPreferences("PREFPRODUCTLIST", Context.MODE_PRIVATE);
            SharedPreferences.Editor listEditor = prefContactProductList.edit();
            listEditor.putString("productsData", contactsArray.toString());
            listEditor.apply();

            Dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(getActivity(), "Product Successfully Added!", Toast.LENGTH_LONG).show();
    }

    public void Dismiss() {
        DialogFragment dialogFragment = (DialogFragment)getFragmentManager().findFragmentByTag("NewProductFragment");
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }

}
