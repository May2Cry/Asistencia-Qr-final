package com.example.asistenciaqr.ui.slideshow;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

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

    // LiveData para el dato compartido desde GalleryFragment
    private MutableLiveData<String> _sharedData = new MutableLiveData<>();
    public LiveData<String> getSharedData() {
        return _sharedData;
    }

    public SlideshowViewModel(@NonNull Application application) {
        super(application);
        fetchPersonData();
    }

    // Método para actualizar el dato compartido
    public void setSharedData(String data) {
        _sharedData.setValue(data);
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
                    Toast.makeText(getApplication(), "Ocurrio un error vuelva a ingresar", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                Toast.makeText(getApplication(), "Ocurrio un error vuelva a ingresar", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void onPersonClicked(String codigo) {

        String phoneNumber = "+593963759503"; // Número de teléfono de destino
        String message = "Asistencia Tecnico : " + codigo +"__"+_sharedData.getValue();

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
