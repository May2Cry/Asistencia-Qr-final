package com.example.asistenciaqr.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.asistenciaqr.R;
import com.example.asistenciaqr.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);

        // Inflar el layout usando Data Binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_slideshow, container, false);
        binding.setViewModel(slideshowViewModel);
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }
}
