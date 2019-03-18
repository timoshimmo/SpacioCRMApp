package com.android.spaciocrm.util.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.android.spaciocrm.R;
import com.android.spaciocrm.util.model.ProductsModel;

import java.util.ArrayList;

/**
 * Created by freshfuturesmy on 24/01/18.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private ArrayList<ProductsModel> mProductList;
    private ArrayList<ProductsModel> mProductListFiltered;
    private Activity mActivity;

    public ProductsAdapter(Activity actvty, ArrayList<ProductsModel> pl) {
        this.mProductList = pl;
        this.mProductListFiltered = pl;
        this.mActivity = actvty;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.product_list_layout, parent, false);

        return new ProductsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ProductsModel model = mProductListFiltered.get(position);

        TextView txtProductName = holder.txtProductName;
        TextView txtProductDesc = holder.txtProductDesc;

        View rowView = holder.rowView;
        txtProductName.setText(model.getProductTitle());
        txtProductDesc.setText(model.getProductDesc());

    }

    @Override
    public int getItemCount() {
        return mProductListFiltered.size();
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
        View rowView;

        ViewHolder(View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductTitle);
            txtProductDesc = itemView.findViewById(R.id.txtProductDesc);
            rowView = itemView;

        }
    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    mProductListFiltered = mProductList;
                }
                else {
                    ArrayList<ProductsModel> filteredList = new ArrayList<>();
                    for (ProductsModel row : mProductList) {
                        if (row.getProductTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    mProductListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mProductListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mProductListFiltered = (ArrayList<ProductsModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
