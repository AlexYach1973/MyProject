package com.example.android.myproject.schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.myproject.R;
import com.example.android.myproject.database.ScheduleEntity;

public class ScheduleViewHolder extends RecyclerView.ViewHolder {
    private final TextView itemType;
    private final TextView itemTime;
    private final TextView itemAmount;
    private final TextView itemDescription;


    public ScheduleViewHolder(View itemView) {
        super(itemView);
        itemType = itemView.findViewById(R.id.schedule_item_type);
        itemTime = itemView.findViewById(R.id.schedule_item_time);
        itemAmount = itemView.findViewById(R.id.schedule_item_amount);
        itemDescription = itemView.findViewById(R.id.schedule_item_description);

    }

    public void bind(ScheduleEntity current) {
        itemType.setText(current.getType());
        itemTime.setText(current.getTime());
        itemAmount.setText(String.valueOf(current.getAmount()));
        itemDescription.setText(current.getDescription());
    }

    static ScheduleViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_item, parent, false);
        return new ScheduleViewHolder(view);
    }

}
