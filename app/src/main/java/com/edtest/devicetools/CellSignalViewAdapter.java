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

public class CellSignalViewAdapter extends RecyclerView.Adapter<CellSignalViewAdapter.ViewHolder> {

    private List<Integer> mViewColors;
    private List<String> mSignalStrengths;
    private List<String> mSignalCounts;
    private LayoutInflater mInflater;
    private MyRecyclerViewAdapter.ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    CellSignalViewAdapter(Context context, List<Integer> colors, List<String> signalStrengths, List<String> signalCounts) {
        this.mInflater = LayoutInflater.from(context);
        this.mViewColors = colors;
        this.mSignalStrengths = signalStrengths;
        this.mSignalCounts = signalCounts;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cell_signal_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int color = mViewColors.get(position);
        String signalStrength = mSignalStrengths.get(position);
        String signalCount = mSignalCounts.get(position);
        holder.myTextView.setText(signalStrength);
        holder.myCountView.setText(signalCount);

        //holder.myImageView.setBackgroundColor(color);
        switch(color) {
            case 1:
                holder.myImageView.setBackground(ContextCompat.getDrawable(context,R.drawable.bar_red_layer_list));
                holder.myTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
                holder.myImageView.getLayoutParams().height = 40;
                holder.myImageView.requestLayout();
                break;
            case 2:
                holder.myImageView.setBackground(ContextCompat.getDrawable(context,R.drawable.bar_orange_layer_list));
                holder.myTextView.setTextColor(ContextCompat.getColor(context, R.color.orange));
                holder.myImageView.getLayoutParams().height = 80;
                holder.myImageView.requestLayout();
                break;
            case 3:
                holder.myImageView.setBackground(ContextCompat.getDrawable(context,R.drawable.bar_yellow_layer_list));
                holder.myTextView.setTextColor(ContextCompat.getColor(context, R.color.yellow));
                holder.myImageView.getLayoutParams().height = 120;
                holder.myImageView.requestLayout();
                break;
            case 4:
                holder.myImageView.setBackground(ContextCompat.getDrawable(context,R.drawable.bar_green_layer_list));
                holder.myTextView.setTextColor(ContextCompat.getColor(context, R.color.green));
                holder.myImageView.getLayoutParams().height = 160;
                holder.myImageView.requestLayout();
                break;
            case 5:
                holder.myImageView.setBackground(ContextCompat.getDrawable(context,R.drawable.bar_blue_layer_list));
                holder.myTextView.setTextColor(ContextCompat.getColor(context, R.color.blue));
                holder.myImageView.getLayoutParams().height = 200;
                holder.myImageView.requestLayout();
                break;
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mSignalStrengths.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView myImageView;
        TextView myTextView;
        TextView myCountView;

        ViewHolder(View itemView) {
            super(itemView);
            myImageView = itemView.findViewById(R.id.barImageView);
            myTextView = itemView.findViewById(R.id.signalStrength);
            myCountView = itemView.findViewById(R.id.signalCount);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mSignalStrengths.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(MyRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}
