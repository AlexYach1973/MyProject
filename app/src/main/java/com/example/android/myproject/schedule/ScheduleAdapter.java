package com.example.android.myproject.schedule;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.android.myproject.database.ScheduleEntity;

public class ScheduleAdapter extends ListAdapter<ScheduleEntity, ScheduleViewHolder> {

    // Интерфейс нажатия на элемент списка
    interface OnStateClickListener{
        void onStateClick(ScheduleEntity scheduleEntity, int position);
    }

    private final OnStateClickListener onClickListener;

    protected ScheduleAdapter(@NonNull DiffUtil.ItemCallback<ScheduleEntity> diffCallback,
                              OnStateClickListener onClickListener) {
        super(diffCallback);
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ScheduleViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {

        ScheduleEntity current = getItem(position);
        holder.bind(current);

        // Обработка нажатия
        holder.itemView.setOnClickListener(v -> {
            // вызываем метод слушателя, передавая ему данные
            onClickListener.onStateClick(current, position);
        });
    }

    static class ScheduleDiff extends DiffUtil.ItemCallback<ScheduleEntity> {
        @Override
        public boolean areItemsTheSame(@NonNull ScheduleEntity oldItem,
                                       @NonNull ScheduleEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ScheduleEntity oldItem,
                                          @NonNull ScheduleEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }
    }
}
