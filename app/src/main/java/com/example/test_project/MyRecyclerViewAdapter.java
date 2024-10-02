package com.example.test_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    private List<Item> itemList;
    private OnItemLongClickListener longClickListener;

    public MyRecyclerViewAdapter(List<Item> itemList, OnItemLongClickListener listener) {
        this.itemList = itemList;
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item currentItem = itemList.get(position);
        holder.textView.setText(currentItem.getName());
        holder.checkBox.setChecked(currentItem.isChecked()); // Set checkbox state

        // Change text color based on checked state
        if (currentItem.isChecked()) {
            holder.textView.setTextColor(0xFFBDBDBD); // Gray color
        } else {
            holder.textView.setTextColor(0xFF000000); // Black color
        }

        // Remove any previous listener to avoid multiple triggers
        holder.checkBox.setOnCheckedChangeListener(null);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentItem.setChecked(isChecked); // Update item state

            // Change text color based on checked state
            if (isChecked) {
                holder.textView.setTextColor(0xFFBDBDBD); // Gray color
            } else {
                holder.textView.setTextColor(0xFF000000); // Black color
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onItemLongClick(currentItem);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public CheckBox checkBox; // Added CheckBox reference

        public MyViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(android.R.id.text1);
            checkBox = itemView.findViewById(android.R.id.checkbox); // Adjust to your layout
        }
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Item item);
    }
}
