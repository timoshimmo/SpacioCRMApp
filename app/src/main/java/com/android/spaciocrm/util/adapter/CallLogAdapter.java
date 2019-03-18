package com.android.spaciocrm.util.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.spaciocrm.R;
import com.android.spaciocrm.util.model.CallLogModel;

import java.util.ArrayList;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.ViewHolder> {

    private ArrayList<CallLogModel> mCallLogList;
    private Activity mActivity;

    public CallLogAdapter(Activity actvty, ArrayList<CallLogModel> cm) {
        this.mCallLogList = cm;
        this.mActivity = actvty;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.call_log_list_layout, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final CallLogModel model = mCallLogList.get(position);

        TextView txtCallType = holder.txtCallType;
        TextView txtCallDateTime = holder.txtCallDateTime;
        TextView txtCallDuration = holder.txtCallDuration;

        txtCallType.setText(model.getCallType());
        txtCallDateTime.setText(model.getCallDateTime());
        txtCallDuration.setText(mActivity.getString(R.string.call_duration, model.getCallDuration()));

    }

    @Override
    public int getItemCount() {
        return mCallLogList.size();
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

        TextView txtCallType;
        TextView txtCallDateTime;
        TextView txtCallDuration;

        public ViewHolder(View itemView) {
            super(itemView);

            txtCallType = itemView.findViewById(R.id.txtCallLogs);
            txtCallDateTime = itemView.findViewById(R.id.txtCallDateTime);
            txtCallDuration = itemView.findViewById(R.id.txtCallDuration);
        }
    }
}
