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

public class InfoRecyclerViewAdapter extends RecyclerView.Adapter<InfoRecyclerViewAdapter.ViewHolder>{

    private List<String> titles;
    private List<String> values;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    InfoRecyclerViewAdapter(Context context, List<String> titles, List<String> values) {
        this.mInflater = LayoutInflater.from(context);
        this.titles = titles;
        this.values = values;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.info_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = titles.get(position);
        String value = values.get(position);
        holder.titleTextView.setText(title);
        holder.valueTextView.setText(value);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return titles.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTextView;
        TextView valueTextView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.infoTitle);
            valueTextView = itemView.findViewById(R.id.infoValue);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return values.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}