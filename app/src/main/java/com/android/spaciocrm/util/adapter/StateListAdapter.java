package com.android.spaciocrm.util.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.android.spaciocrm.R;
import com.android.spaciocrm.util.model.CountryModel;
import com.android.spaciocrm.util.model.StateModel;

import java.util.ArrayList;

/**
 * Created by freshfuturesmy on 23/01/18.
 */

public class StateListAdapter extends RecyclerView.Adapter<StateListAdapter.ViewHolder> {

    private ArrayList<StateModel> mStateNames;
    private int selectedIndex = -1;
    private StateModel mSModel;

    public StateListAdapter(StateModel sm, ArrayList<StateModel> snm) {

        this.mStateNames = snm;
        this.mSModel = sm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.country_list_structure, parent, false);
        return new StateListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final StateModel model = mStateNames.get(position);
        final RadioButton rbStateName = holder.rbStateName;

        rbStateName.setText(model.getStateName());
        rbStateName.setChecked(position == selectedIndex);

        rbStateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.getAdapterPosition() == selectedIndex) {
                    rbStateName.setChecked(false);
                    selectedIndex = -1;
                }
                else {

                    selectedIndex = holder.getAdapterPosition();
                    rbStateName.setChecked(true);
                    mSModel.setStateName((String)rbStateName.getText());
                    notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mStateNames.size();
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

        RadioButton rbStateName;

        ViewHolder(View itemView) {
            super(itemView);

            rbStateName = itemView.findViewById(R.id.rbCtryName);

        }
    }
}
