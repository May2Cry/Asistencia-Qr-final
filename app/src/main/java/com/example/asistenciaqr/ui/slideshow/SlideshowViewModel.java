package com.example.asistenciaqr.ui.slideshow;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.asistenciaqr.network.*;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlideshowViewModel extends AndroidViewModel {

    private MutableLiveData<List<Person>> _personas = new MutableLiveData<>();
    public LiveData<List<Person>> personas = _personas;

    public SlideshowViewModel(@NonNull Application application) {
        super(application);
        fetchPersonData();
    }


    private void fetchPersonData() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Person>> call = apiService.getPersonas();
        call.enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                if (response.isSuccessful()) {
                    _personas.setValue(response.body());
                } else {
                    // Manejar el caso en que la respuesta no fue exitosa
                }
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                // Manejar el error de la llamada
            }
        });
    }
    public void onPersonClicked(String codigo) {

        String phoneNumber = "+593963759503"; // Número de teléfono de destino
        String message = "Hola, desea asistencia del técnico " + codigo;

        String uri = "https://wa.me/" + phoneNumber + "?text=" + Uri.encode(message);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));

        if (intent.resolveActivity(getApplication().getPackageManager()) != null) {
            intent.setPackage("com.whatsapp");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(intent);
        } else {
            Intent fallbackIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            fallbackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(fallbackIntent);
        }
    }
}
