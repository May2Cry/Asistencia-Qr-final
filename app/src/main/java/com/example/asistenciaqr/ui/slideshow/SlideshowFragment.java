package com.example.asistenciaqr.ui.slideshow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        // Inicializar el ViewModel compartido usando el ámbito de la actividad
        slideshowViewModel = new ViewModelProvider(requireActivity()).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);

        // Observar los datos compartidos
        slideshowViewModel.getSharedData().observe(getViewLifecycleOwner(), sharedData -> {
            if (sharedData != null) {
                // Mostrar el dato en un TextView o realizar alguna acción
                binding.textViewTipoEquip.setText("Tipo de equipo: " + sharedData);
            } else {
                // Manejar el caso donde no se recibe el dato
                Toast.makeText(getContext(), "No se recibió el dato del tipo de equipo", Toast.LENGTH_SHORT).show();
            }
        });

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
