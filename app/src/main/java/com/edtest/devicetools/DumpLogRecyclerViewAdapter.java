package com.edtest.devicetools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DumpLogRecyclerViewAdapter extends RecyclerView.Adapter<DumpLogRecyclerViewAdapter.ViewHolder>{

    private List<String> values;
    private LayoutInflater mInflater;
    private InfoRecyclerViewAdapter.ItemClickListener mClickListener;
    private Context context;

    DumpLogRecyclerViewAdapter(Context context, List<String> values) {
        this.mInflater = LayoutInflater.from(context);
        this.values = values;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.log_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String value = values.get(position);
        holder.valueTextView.setText(value);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return values.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView valueTextView;

        ViewHolder(View itemView) {
            super(itemView);
            valueTextView = itemView.findViewById(R.id.logListValue);
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
    public void setClickListener(InfoRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
