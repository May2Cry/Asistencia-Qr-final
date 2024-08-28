package com.example.asistenciaqr.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.asistenciaqr.databinding.FragmentSlideshowBinding;
import com.example.asistenciaqr.network.Person;
import com.example.asistenciaqr.network.PersonAdapter;

import java.util.List;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);

        setupRecyclerView();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        slideshowViewModel.personas.observe(getViewLifecycleOwner(), personas -> {
            if (personas != null && !personas.isEmpty()) {
                PersonAdapter adapter = new PersonAdapter(personas, slideshowViewModel);
                binding.recyclerView.setAdapter(adapter);
            }
        });
    }


}
