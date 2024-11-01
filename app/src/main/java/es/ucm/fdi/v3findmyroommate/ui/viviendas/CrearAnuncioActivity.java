package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.ucm.fdi.v3findmyroommate.R;

public class CrearAnuncioActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText editTitulo, editUbicacion, editMetros, editPrecio;
    private Button btnGuardar, btnCancelar, btnSeleccionarImagen;
    private ImageView imagenAnuncio;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_anuncio);

        // Inicialización de vistas
        editTitulo = findViewById(R.id.edit_titulo);
        editUbicacion = findViewById(R.id.edit_ubicacion);
        editMetros = findViewById(R.id.edit_metros);
        editPrecio = findViewById(R.id.edit_precio);
        btnGuardar = findViewById(R.id.btn_guardar_anuncio);
        btnCancelar = findViewById(R.id.btn_cancelar);
        btnSeleccionarImagen = findViewById(R.id.btn_seleccionar_imagen);
        imagenAnuncio = findViewById(R.id.imagen_anuncio);

        // Botón para seleccionar imagen
        btnSeleccionarImagen.setOnClickListener(v -> {
            // Verifica y solicita permisos
            requestPermissions();
        });

        btnGuardar.setOnClickListener(v -> {
            String titulo = editTitulo.getText().toString();
            String ubicacion = editUbicacion.getText().toString();
            String metros = editMetros.getText().toString();
            String precio = editPrecio.getText().toString();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("titulo", titulo);
            resultIntent.putExtra("ubicacion", ubicacion);
            resultIntent.putExtra("metros", metros);
            resultIntent.putExtra("precio", precio);
             if (photoUri != null) {
                resultIntent.putExtra("imagenUri", photoUri);
            }

            setResult(RESULT_OK, resultIntent);
            finish();
        });

        btnCancelar.setOnClickListener(v -> finish());
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        } else {
            openCamera(); // Abre la cámara si los permisos ya están concedidos
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera(); // Abre la cámara si se concede el permiso
            } else {
                Toast.makeText(this, "Permiso de cámara no concedido", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                Toast.makeText(this, "Error al crear archivo de imagen", Toast.LENGTH_SHORT).show();
            }


            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, "es.ucm.fdi.v3findmyroommate.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            Toast.makeText(this, "No se encontró aplicación de cámara", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (photoUri != null) {
                imagenAnuncio.setImageURI(photoUri);
            } else {
                Toast.makeText(this, "No se obtuvo la URI de la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
