package com.example.asistenciaqr.ui.slideshow;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class SlideshowViewModel extends AndroidViewModel {

    public SlideshowViewModel(@NonNull Application application) {
        super(application);
    }

    public void onPersonClicked(int personId) {
        String phoneNumber = "+593939796840"; // Número de teléfono de destino
        String message = "hola desea asistencia del tecnico "+ personId;

        String uri = "https://wa.me/" + phoneNumber + "?text=" + Uri.encode(message);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));

        if (intent.resolveActivity(getApplication().getPackageManager()) != null) {
            intent.setPackage("com.whatsapp"); // Especificar el paquete de WhatsApp
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(intent);
        } else {
            // Manejar el caso donde WhatsApp no está instalado
            Intent fallbackIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            fallbackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(fallbackIntent);
        }
    }
}
