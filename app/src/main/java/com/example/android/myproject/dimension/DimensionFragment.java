package com.example.android.myproject.dimension;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.myproject.R;

public class DimensionFragment extends Fragment {

    private DimensionViewModel mViewModel;

    public static DimensionFragment newInstance() {
        return new DimensionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dimension_fragment, container, false);
    }

}