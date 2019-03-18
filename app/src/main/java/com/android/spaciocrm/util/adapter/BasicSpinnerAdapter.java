package com.android.spaciocrm.util.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.spaciocrm.R;

import java.util.ArrayList;

/**
 * Created by freshfuturesmy on 14/02/18.
 */

public class BasicSpinnerAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> productTitle;
    LayoutInflater inflter;

    public BasicSpinnerAdapter(Context applicationContext, ArrayList<String> pTitle) {
        this.context = applicationContext;
        this.productTitle = pTitle;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return productTitle.size();
    }

    @Override
    public Object getItem(int position) {
        return productTitle.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflter.inflate(R.layout.simple_list_structure, null);

        TextView productNames = convertView.findViewById(R.id.txtRowName);
        productNames.setText(productTitle.get(position));
        return convertView;
    }
}
