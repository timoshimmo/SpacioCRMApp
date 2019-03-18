package com.android.spaciocrm.util.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import com.android.spaciocrm.R;
import com.android.spaciocrm.util.model.CountryModel;

import java.util.ArrayList;

/**
 * Created by freshfuturesmy on 22/01/18.
 */

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder>{

    private ArrayList<CountryModel> mCtryNames;
    private int selectedIndex = -1;

    private CountryModel mCModel;

    public CountryListAdapter(CountryModel cm, ArrayList<CountryModel> cnm) {

        this.mCtryNames = cnm;
        this.mCModel = cm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.country_list_structure, parent, false);
        return new CountryListAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,  int position) {

        final CountryModel model = mCtryNames.get(position);
        final RadioButton rbCtryName = holder.rbCtryName;

        rbCtryName.setText(model.getCtryName() + " " + "("+model.getCtryCode()+")");
        rbCtryName.setChecked(position == selectedIndex);

        rbCtryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.getAdapterPosition() == selectedIndex) {
                    rbCtryName.setChecked(false);
                    selectedIndex = -1;
                }
                else {

                    selectedIndex = holder.getAdapterPosition();
                    rbCtryName.setChecked(true);
                    mCModel.setCtryName((String)rbCtryName.getText());
                    notifyDataSetChanged();
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return mCtryNames.size();
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

        RadioButton rbCtryName;

        ViewHolder(View itemView) {
            super(itemView);

            rbCtryName = itemView.findViewById(R.id.rbCtryName);

        }
    }
}
