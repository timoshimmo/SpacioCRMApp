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
import com.android.spaciocrm.home.activity.HomeActivity;
import com.android.spaciocrm.util.dialogs.OutgoingReferralDialogFragment;
import com.android.spaciocrm.util.model.ReferralModel;

import java.util.ArrayList;

/**
 * Created by freshfuturesmy on 07/02/18.
 */

public class OutgoingAdapter extends RecyclerView.Adapter<OutgoingAdapter.ViewHolder> {

    private ArrayList<ReferralModel> mReferralList;
    private ArrayList<ReferralModel> mReferralListFiltered;
    private Activity mActivity;

    public OutgoingAdapter(Activity actvy, ArrayList<ReferralModel> pl) {
        this.mActivity = actvy;
        this.mReferralList = pl;
        this.mReferralListFiltered = pl;
    }

    public OutgoingAdapter() {
        super();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.simple_list_structure, parent, false);

        return new OutgoingAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ReferralModel model = mReferralListFiltered.get(position);

        TextView txtProductName = holder.txtReferralTitle;
        View rowView = holder.rowView;
        txtProductName.setText(model.getTitle());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OutgoingReferralDialogFragment outgoingReferralDialogFragment = OutgoingReferralDialogFragment.newInstance(model.getTitle(), holder.getAdapterPosition());
                outgoingReferralDialogFragment.show(((HomeActivity)mActivity).getSupportFragmentManager(), "OutgoingReferralFragment");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReferralListFiltered.size();
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

        TextView txtReferralTitle;
        View rowView;

        ViewHolder(View itemView) {
            super(itemView);
            txtReferralTitle = itemView.findViewById(R.id.txtRowName);
            rowView = itemView;
        }
    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    mReferralListFiltered = mReferralList;
                }
                else {
                    ArrayList<ReferralModel> filteredList = new ArrayList<>();
                    for (ReferralModel row : mReferralList) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    mReferralListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mReferralListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mReferralListFiltered = (ArrayList<ReferralModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
