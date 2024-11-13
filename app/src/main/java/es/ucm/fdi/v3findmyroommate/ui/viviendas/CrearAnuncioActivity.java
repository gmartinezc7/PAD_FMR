package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.ucm.fdi.v3findmyroommate.R;

public class CrearAnuncioActivity extends AppCompatActivity {



    private EditText editTitulo, editUbicacion, editMetros, editPrecio, editDescripcion;
    private Button btnGuardar, btnCancelar, btnSeleccionarImagen;
    private ImageView imagenAnuncio;


    private List<Uri> imagenesUri = new ArrayList<>();
    private int imagenActualIndex = 0; // Índice de la imagen actual
    private ImageButton btnPrev, btnNext;



    private Uri previewPhotoUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_anuncio);

        // Inicialización de vistas
        editTitulo = findViewById(R.id.edit_titulo);
        editUbicacion = findViewById(R.id.edit_ubicacion);
        editMetros = findViewById(R.id.edit_metros);
        editPrecio = findViewById(R.id.edit_precio);
        editDescripcion = findViewById(R.id.edit_descripcion);


        btnGuardar = findViewById(R.id.btn_guardar_anuncio);
        btnCancelar = findViewById(R.id.btn_cancelar);
        btnSeleccionarImagen = findViewById(R.id.btn_seleccionar_imagen);
        imagenAnuncio = findViewById(R.id.imagen_anuncio);

        btnPrev = findViewById(R.id.btn_prev);
        btnNext = findViewById(R.id.btn_next);

        btnPrev.setVisibility(View.INVISIBLE);
        btnNext.setVisibility(View.INVISIBLE);


        previewPhotoUri = null;


        // Botón para seleccionar imagen
        btnSeleccionarImagen.setOnClickListener(v -> {
            // Verifica y solicita permisos
            requestPermissions();
        });

        btnGuardar.setOnClickListener(v -> {
            guardarAnuncio();
        });

        btnCancelar.setOnClickListener(v -> finish());

        btnPrev.setOnClickListener(v -> mostrarImagenAnterior());
        btnNext.setOnClickListener(v -> mostrarImagenSiguiente());

    }

    private void guardarAnuncio() {
        String titulo = editTitulo.getText().toString();
        String ubicacion = editUbicacion.getText().toString();
        String metros = editMetros.getText().toString();
        String precio = editPrecio.getText().toString();
        String descripcion = editDescripcion.getText().toString();
        // Verifica si todos los campos están llenos
        if (titulo.isEmpty() || ubicacion.isEmpty() || metros.isEmpty()
                || precio.isEmpty() || imagenesUri.isEmpty() ) {
            Toast.makeText(this, "Debes rellenar toda la información " +
                    "para poder crear un anuncio", Toast.LENGTH_LONG).show();
            return; // Detiene el flujo y no continúa con la creación del anuncio
        }


        Intent resultIntent = new Intent();
        resultIntent.putExtra("titulo", titulo);
        resultIntent.putExtra("ubicacion", ubicacion);
        resultIntent.putExtra("metros", metros);
        resultIntent.putExtra("precio", precio);
        resultIntent.putExtra("descripcion", descripcion);
        resultIntent.putParcelableArrayListExtra("imagenesUri", new ArrayList<>(imagenesUri));

        setResult(RESULT_OK, resultIntent);
        finish();
    }


    private void mostrarImagenAnterior() {
        if (imagenActualIndex > 0) {
            imagenActualIndex--;
            actualizarImagen();
        }
    }

    private void mostrarImagenSiguiente() {
        if (imagenActualIndex < imagenesUri.size() - 1) {
            imagenActualIndex++;
            actualizarImagen();
        }
    }



    private void actualizarImagen() {
        if (!imagenesUri.isEmpty()) {
            imagenAnuncio.setImageURI(imagenesUri.get(imagenActualIndex));
            btnPrev.setVisibility(imagenActualIndex > 0 ? View.VISIBLE : View.INVISIBLE);
            btnNext.setVisibility(imagenActualIndex < imagenesUri.size() - 1 ? View.VISIBLE : View.INVISIBLE);
        }
    }



    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }
        else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 101);
        }
        else {
            openImageSelector(); // Abre selector de imagens si los permisos ya están concedidos
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageSelector(); // Abre selector si se concede el permiso
            } else {
                Toast.makeText(this, "Permiso de cámara no concedido", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == 101){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageSelector(); // Abre selector si se concede el permiso
            } else {
                Toast.makeText(this, "Permiso de almacenamiento no concedido", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openImageSelector() {

        // Intent para tomar una foto con la cámara JUNTO AL PROCESO DE CREACION DE LA IMAGEN
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
                previewPhotoUri = FileProvider.getUriForFile(this, "es.ucm.fdi.v3findmyroommate.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, previewPhotoUri);
            }
        } else {
            Toast.makeText(this, "No se encontró aplicación de cámara", Toast.LENGTH_SHORT).show();
        }



        // Intent para seleccionar una imagen de la galería
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Crear un "chooser" que permite elegir entre la cámara o la galería
        Intent chooserIntent = Intent.createChooser(pickPhotoIntent, "Selecciona una imagen");

        // Añadir la opción de tomar una foto con la cámara
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { takePictureIntent });

        // Llamar al "chooser" para que el usuario elija
        imagePickerLauncher.launch(chooserIntent);
    }



    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

        if (result.getResultCode() == RESULT_OK) {

            if (result != null && result.getData() != null) {
                previewPhotoUri = result.getData().getData(); // URI de la imagen seleccionada (galería)
                agregarImagen(previewPhotoUri);

            } else { //EN CASO DE QUE SEA NULL ES PORQUE LA IMAGEN QUE SE HA ESCOGIDO ES DE LA CAMARA

                if (previewPhotoUri != null) { //SE COMPRUEBA QUE EFECTIVAMENTE SER HAYA ELEGIDO UNA IMAGEN UNA VEZ ABEIRTA LA CAMARA
                    // El usuario tomó la foto correctamente
                     agregarImagen(previewPhotoUri);

                } else {
                    // El usuario no tomó una foto o canceló
                    Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else{
            // Si no se seleccionó ninguna imagen o se canceló
            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show();
             previewPhotoUri = null; // Restablecemos previewPhotoUri
        }


});

    private void agregarImagen(Uri uri) {
        imagenesUri.add(uri);
        imagenActualIndex = imagenesUri.size() - 1;
        actualizarImagen();
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);


    }
}
