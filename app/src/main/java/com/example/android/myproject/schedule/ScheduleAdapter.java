package com.example.android.myproject.schedule;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.android.myproject.database.ScheduleEntity;

public class ScheduleAdapter extends ListAdapter<ScheduleEntity, ScheduleViewHolder> {

    protected ScheduleAdapter(@NonNull DiffUtil.ItemCallback<ScheduleEntity> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ScheduleViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        ScheduleEntity current = getItem(position);
        holder.bind(current);
    }

    static class ScheduleDiff extends DiffUtil.ItemCallback<ScheduleEntity> {
        @Override
        public boolean areItemsTheSame(@NonNull ScheduleEntity oldItem, @NonNull ScheduleEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ScheduleEntity oldItem, @NonNull ScheduleEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }
    }
}
