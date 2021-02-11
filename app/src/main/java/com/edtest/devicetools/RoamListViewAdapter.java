package com.edtest.devicetools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RoamListViewAdapter extends RecyclerView.Adapter<RoamListViewAdapter.ViewHolder>  {
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private List<String> apMacs;
    private List<String> signals;
    private List<String> bands;
    private List<String> oldNew;

    RoamListViewAdapter(Context context, List<String> apMacs, List<String> signals, List<String> bands, List<String> oldNew) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.bands = bands;
        this.signals = signals;
        this.apMacs = apMacs;
        this.oldNew = oldNew;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.roamview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull RoamListViewAdapter.ViewHolder holder, int position) {
        String signal = signals.get(position);
        String band = bands.get(position);
        String apMac = apMacs.get(position);
        String oN = oldNew.get(position);

        holder.signalTextView.setText(signal);
        holder.bandTextView.setText(band);
        holder.apMacTextView.setText(apMac);
        holder.oldNewTextView.setText(oN);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return apMacs.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView signalTextView;
        TextView bandTextView;
        TextView apMacTextView;
        TextView oldNewTextView;

        ViewHolder(View itemView) {
            super(itemView);
            signalTextView = itemView.findViewById(R.id.signalStrengthRoamList);
            bandTextView = itemView.findViewById(R.id.apBandRoamList);
            apMacTextView = itemView.findViewById(R.id.apMacRoamList);
            oldNewTextView = itemView.findViewById(R.id.oldNewRoamList);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return apMacs.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(RoamListViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
