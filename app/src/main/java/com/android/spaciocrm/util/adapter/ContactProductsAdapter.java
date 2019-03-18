package com.android.spaciocrm.util.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.spaciocrm.R;
import com.android.spaciocrm.util.model.ContactProductsModel;
import java.util.ArrayList;


/**
 * Created by freshfuturesmy on 06/02/18.
 */

public class ContactProductsAdapter extends RecyclerView.Adapter<ContactProductsAdapter.ViewHolder> {

    private ArrayList<String> mContactProductList;
    private ArrayList<String> mProductDescList;

    public ContactProductsAdapter(ArrayList<String> pl, ArrayList<String> pd) {
        this.mContactProductList = pl;
        this.mProductDescList = pd;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.product_list_layout, parent, false);

        return new ContactProductsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TextView txtProductName = holder.txtProductName;
        TextView txtProductDesc = holder.txtProductDesc;

        String titles = mContactProductList.get(position);
        String desc = mProductDescList.get(position);

        txtProductName.setText(titles);
        txtProductDesc.setText(desc);
    }

    @Override
    public int getItemCount() {
        return mContactProductList.size();
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

        TextView txtProductName;
        TextView txtProductDesc;

        ViewHolder(View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductTitle);
            txtProductDesc = itemView.findViewById(R.id.txtProductDesc);
        }
    }
}
