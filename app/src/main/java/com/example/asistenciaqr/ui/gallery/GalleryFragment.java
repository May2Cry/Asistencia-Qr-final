package com.example.asistenciaqr.ui.gallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import com.example.asistenciaqr.R;
import com.example.asistenciaqr.databinding.FragmentGalleryBinding;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GalleryFragment extends Fragment {

    private static final String TAG = "GalleryFragment";
    private static final int TIMEOUT_DURATION = 30000; // 30 seconds
    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    private Handler handler;
    private Runnable timeoutRunnable;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        progressBar = binding.progressBar;

        // Configurar el FloatingActionButton
        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE); // Asegúrate de que el botón es visible
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_nav_gallery_to_nav_slideshow);
            }
        });

        // Cargar el PDF
        loadPdf();

        return root;
    }

    private void loadPdf() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String scannedUrl = bundle.getString("scannedUrl");
            if (scannedUrl != null) {
                // Convertir el enlace de Google Drive a un enlace directo de descarga
                String fileId = scannedUrl.split("/d/")[1].split("/")[0];
                String directDownloadUrl = "https://drive.google.com/uc?export=download&id=" + fileId;
                Log.d(TAG, "Direct Download URL: " + directDownloadUrl);

                // Mostrar el ProgressBar
                progressBar.setVisibility(View.VISIBLE);

                // Iniciar el timeout de 30 segundos
                handler = new Handler();
                timeoutRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // Ocultar el ProgressBar y mostrar mensaje de error
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Failed to download PDF: Timeout", Toast.LENGTH_SHORT).show();
                        // Navegar de vuelta al HomeFragment
                        NavController navController = Navigation.findNavController(getView());
                        navController.navigate(R.id.nav_home, null, new NavOptions.Builder().setPopUpTo(R.id.nav_gallery, true).build());
                    }
                };
                handler.postDelayed(timeoutRunnable, TIMEOUT_DURATION);

                new DownloadPdfTask().execute(directDownloadUrl);
            } else {
                Log.e(TAG, "Scanned URL is null");
                Toast.makeText(getContext(), "Failed to get scanned URL", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e(TAG, "Bundle is null");
            Toast.makeText(getContext(), "Failed to get arguments", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadPdfTask extends AsyncTask<String, Void, File> {

        @Override
        protected File doInBackground(String... strings) {
            String fileUrl = strings[0];
            File pdfFile = null;
            try {
                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                pdfFile = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "downloaded.pdf");
                FileOutputStream outputStream = new FileOutputStream(pdfFile);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.close();
                inputStream.close();
                Log.d(TAG, "PDF downloaded successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error downloading PDF", e);
            }
            return pdfFile;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);

            // Cancelar el timeout
            if (handler != null && timeoutRunnable != null) {
                handler.removeCallbacks(timeoutRunnable);
            }

            // Ocultar el ProgressBar
            progressBar.setVisibility(View.GONE);

            if (file != null) {
                PDFView pdfView = binding.pdfView;
                pdfView.fromFile(file)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .defaultPage(0)
                        .enableAnnotationRendering(true)
                        .password(null)
                        .scrollHandle(null)
                        .onError(new OnErrorListener() {
                            @Override
                            public void onError(Throwable t) {
                                Log.e(TAG, "Error loading PDF", t);
                                Toast.makeText(getContext(), "Failed to load PDF: File not in PDF format or corrupted", Toast.LENGTH_SHORT).show();
                                // Navegar de vuelta al HomeFragment
                                NavController navController = Navigation.findNavController(getView());
                                navController.navigate(R.id.nav_home, null, new NavOptions.Builder().setPopUpTo(R.id.nav_gallery, true).build());
                            }
                        })
                        .load();
                Log.d(TAG, "PDF loaded into PDFView");
            } else {
                Log.e(TAG, "Downloaded file is null");
                Toast.makeText(getContext(), "Failed to download PDF", Toast.LENGTH_SHORT).show();

                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.nav_home, null, new NavOptions.Builder().setPopUpTo(R.id.nav_gallery, true).build());
            }
        }
    }
}
