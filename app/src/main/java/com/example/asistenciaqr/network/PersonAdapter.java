package com.example.asistenciaqr.network;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.asistenciaqr.R;
import com.example.asistenciaqr.databinding.ItemPersonBinding;
import com.example.asistenciaqr.network.Person;
import com.example.asistenciaqr.ui.slideshow.SlideshowViewModel;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    private List<Person> personList;
    private SlideshowViewModel viewModel;

    public PersonAdapter(List<Person> personList, SlideshowViewModel viewModel) {
        this.personList = personList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemPersonBinding binding = ItemPersonBinding.inflate(layoutInflater, parent, false);
        return new PersonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person person = personList.get(position);
        holder.bind(person);
    }

    @Override
    public int getItemCount() {
        return personList != null ? personList.size() : 0;
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        private final ItemPersonBinding binding;

        public PersonViewHolder(ItemPersonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Person person) {
            binding.personName.setText(person.getNombre());
            binding.personProgress.setProgress(person.getTrabajos());

            // Cargar la imagen usando Glide
            Glide.with(binding.personImage.getContext())
                    .load(person.getUrlfoto())
                    //.placeholder(R.drawable.placeholder_image) // Imagen mostrada mientras se carga la principal
                    //.error(R.drawable.error_image) // Imagen mostrada si hay un error
                    .into(binding.personImage);

            binding.getRoot().setOnClickListener(v -> viewModel.onPersonClicked(person.getCodigo()));
        }
    }
}

