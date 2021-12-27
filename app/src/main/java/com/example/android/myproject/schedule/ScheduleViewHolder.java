package com.example.android.myproject.schedule;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.myproject.database.ScheduleEntity;
import com.example.android.myproject.databinding.ScheduleItemBinding;

public class ScheduleViewHolder extends RecyclerView.ViewHolder {
    private final ScheduleItemBinding binding;

    public ScheduleViewHolder(ScheduleItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding ;
    }

    public void bind(ScheduleEntity current) {
        binding.scheduleItemType.setText(current.getType());
        binding.scheduleItemTime.setText(current.getTime());
        binding.scheduleItemAmount.setText(String.valueOf(current.getAmount()));
        binding.scheduleItemDescription.setText(current.getDescription());
        // Выполнить немедленно
        binding.executePendingBindings();

    }

    static ScheduleViewHolder create(ViewGroup parent) {
        LayoutInflater layoutInflater =
                android.view.LayoutInflater.from(parent.getContext());
        ScheduleItemBinding scheduleBinding =
                ScheduleItemBinding.inflate(layoutInflater, parent, false);

        return new ScheduleViewHolder(scheduleBinding);

    }

}
