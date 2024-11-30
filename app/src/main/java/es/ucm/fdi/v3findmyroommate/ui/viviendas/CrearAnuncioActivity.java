package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.ucm.fdi.v3findmyroommate.R;


public class CrearAnuncioActivity extends AppCompatActivity {



    private EditText editTitulo, editUbicacion, editMetros, editPrecio, editDescripcion;
    private Button btnGuardar, btnCancelar;

    private ImageView imagenAnuncio, btnEliminarImagen;

    FloatingActionButton btnSeleccionarImagen;

    private List<Uri> imagenesUri = new ArrayList<>();
    private int imagenActualIndex = 0; // Índice de la imagen actual
    private ImageButton btnPrev, btnNext;

    private Uri previewPhotoUri;


    Spinner spinnerCategoria;
    LinearLayout opcionesCasa;
    LinearLayout opcionesHabitacion, guardarAnuncio;


    Spinner spinnerTipoCasa, spinnerHabitaciones, spinnerBanos, spinnerExteriorInteriorCasa;
    Spinner spinnerCompaneros, spinnerGenero, spinnerExteriorInteriorHabitacion, spinnerTipoBano;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_anuncio_2);
        previewPhotoUri = null;


        enlazarIdsVista();

        establecerAccionesBotones();

        establecerAccionesSpinners();

    }


    private void enlazarIdsVista(){

        editTitulo = findViewById(R.id.create_titulo);
        editUbicacion = findViewById(R.id.create_ubicacion);
        editMetros = findViewById(R.id.create_metros);
        editPrecio = findViewById(R.id.create_precio);
        editDescripcion = findViewById(R.id.create_descripcion);
        btnGuardar = findViewById(R.id.btn_guardar_anuncio);
        btnCancelar = findViewById(R.id.btn_cancelar);
        btnSeleccionarImagen = findViewById(R.id.btn_seleccionar_imagen);
        btnEliminarImagen = findViewById(R.id.btn_eliminar_imagen);
        imagenAnuncio = findViewById(R.id.imagen_anuncio);
        btnPrev = findViewById(R.id.btn_prev);
        btnNext = findViewById(R.id.btn_next);
        btnPrev.setVisibility(View.INVISIBLE);
        btnNext.setVisibility(View.INVISIBLE);
        spinnerCategoria = findViewById(R.id.spinner_categoria);
        opcionesCasa = findViewById(R.id.opciones_casa);
        opcionesHabitacion = findViewById(R.id.opciones_habitacion);
        guardarAnuncio = findViewById(R.id.guardarAnuncio);

        // Spinners de opciones Casa
        spinnerTipoCasa = findViewById(R.id.spinner_tipo_casa);
        spinnerHabitaciones = findViewById(R.id.spinner_habitaciones);
        spinnerBanos = findViewById(R.id.spinner_banos);
        spinnerExteriorInteriorCasa = findViewById(R.id.spinner_exterior_interior_casa);

        // Spinners de opciones Habitación
        spinnerCompaneros = findViewById(R.id.spinner_companeros);
        spinnerGenero = findViewById(R.id.spinner_genero);
        spinnerExteriorInteriorHabitacion = findViewById(R.id.spinner_exterior_interior_habitacion);
        spinnerTipoBano = findViewById(R.id.spinner_tipo_bano);


    }



    private void  establecerAccionesBotones(){

        // Botón para seleccionar imagen
        btnSeleccionarImagen.setOnClickListener(v -> {
            // Verifica y solicita permisos
            requestPermissions();
        });

        // Botón para eliminar imagen
        btnEliminarImagen.setOnClickListener(v -> {
            eliminarImagenSeleccionada();
        });


        btnGuardar.setOnClickListener(v -> {
            guardarAnuncio();
        });

        btnCancelar.setOnClickListener(v -> finish());

        btnPrev.setOnClickListener(v -> mostrarImagenAnterior());
        btnNext.setOnClickListener(v -> mostrarImagenSiguiente());


    }



    private void establecerAccionesSpinners(){

        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guardarAnuncio.getLayoutParams();

                if (position == 0) { // Casa
                    opcionesCasa.setVisibility(View.VISIBLE);
                    opcionesHabitacion.setVisibility(View.GONE);
                    params.topToBottom = opcionesCasa.getId();
                } else if (position == 1) { // Habitación
                    opcionesCasa.setVisibility(View.GONE);
                    opcionesHabitacion.setVisibility(View.VISIBLE);
                    params.topToBottom = opcionesHabitacion.getId();
                }
                guardarAnuncio.setLayoutParams(params);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                opcionesCasa.setVisibility(View.GONE);
                opcionesHabitacion.setVisibility(View.GONE);
            }
        });

    }



    private Intent guardarAnuncio() {

        String titulo = editTitulo.getText().toString();
        String ubicacion = editUbicacion.getText().toString();
        String metros = editMetros.getText().toString();
        String precio = editPrecio.getText().toString();
        String descripcion = editDescripcion.getText().toString();
        // Verifica si todos los campos están llenos
        if (titulo.isEmpty() || ubicacion.isEmpty() || metros.isEmpty()
                || precio.isEmpty() || imagenesUri.isEmpty()) {
            Toast.makeText(this, this.getString(R.string.mensaje_debes_rellenar_todo),
                    Toast.LENGTH_LONG).show();
            return null; // Detiene el flujo y no continúa con la creación del anuncio
        }


        Intent resultIntent = new Intent();
        resultIntent.putExtra(this.getString(R.string.key_titulo), titulo);
        resultIntent.putExtra(this.getString(R.string.key_ubicacion), ubicacion);
        resultIntent.putExtra(this.getString(R.string.key_metros), metros);
        resultIntent.putExtra(this.getString(R.string.key_precio), precio);
        resultIntent.putExtra(this.getString(R.string.key_descripcion), descripcion);
        resultIntent.putParcelableArrayListExtra(this.getString(R.string.key_imagenes_uri),
                new ArrayList<>(imagenesUri));


        //GUARDAMOS TAMBIÉN LAS ETIQUETAS
        String categoria = spinnerCategoria.getSelectedItem().toString();
        resultIntent.putExtra(this.getString(R.string.key_categoria), categoria);

        // Guardamos los datos específicos según la categoría
        if (categoria.equalsIgnoreCase(this.getString(R.string.category_casa))) {

            resultIntent.putExtra(this.getString(R.string.key_tipo_casa), spinnerTipoCasa.getSelectedItem().toString());
            resultIntent.putExtra(this.getString(R.string.key_habitaciones), spinnerHabitaciones.getSelectedItem().toString());
            resultIntent.putExtra(this.getString(R.string.key_banos), spinnerBanos.getSelectedItem().toString());
            resultIntent.putExtra(this.getString(R.string.key_exterior_interior), spinnerExteriorInteriorCasa.getSelectedItem().toString());
        } else if (categoria.equalsIgnoreCase(this.getString(R.string.category_habitacion))) {

            resultIntent.putExtra(this.getString(R.string.key_companeros), spinnerCompaneros.getSelectedItem().toString());
            resultIntent.putExtra(this.getString(R.string.key_genero), spinnerGenero.getSelectedItem().toString());
            resultIntent.putExtra(this.getString(R.string.key_exterior_interior), spinnerExteriorInteriorHabitacion.getSelectedItem().toString());
            resultIntent.putExtra(this.getString(R.string.key_tipo_bano), spinnerTipoBano.getSelectedItem().toString());
        }

        Anuncio nuevoAnuncio = new Anuncio(this, resultIntent);
        MisViviendasFragment.guardarAnuncioEnBD(nuevoAnuncio, this.getApplication());
        resultIntent.putExtra(this.getString(R.string.key_id), nuevoAnuncio.getId()); // Adds the new add ID to the intent.

        setResult(RESULT_OK, resultIntent);
        finish();
        return resultIntent;
    }



    public static void startForResult(ActivityResultLauncher<Intent> launcher, Context context) {
        Intent intent = new Intent(context, CrearAnuncioActivity.class);
        launcher.launch(intent);
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
        } else {
            // Si la lista está vacía, restablecer la vista
            imagenAnuncio.setImageDrawable(null); // Limpia la imagen
            btnPrev.setVisibility(View.INVISIBLE);
            btnNext.setVisibility(View.INVISIBLE);
        }
    }


    private void eliminarImagenSeleccionada(){

        if (imagenesUri != null && !imagenesUri.isEmpty() && imagenActualIndex >= 0) {
            // Eliminar la imagen actual de la lista
            imagenesUri.remove(imagenActualIndex);

            // Ajustar el índice actual si es necesario
            if (imagenActualIndex >= imagenesUri.size()) {
                imagenActualIndex = imagenesUri.size() - 1; // Mover al último índice disponible
            }

            // Actualizar la imagen mostrada
            actualizarImagen();
        }

    }





    //----------------------PERMISOS Y ACCESO A CAMARA/ALAMACENAMIENTO----------------------------------------------------------------------

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
                Toast.makeText(this, this.getString(R.string.mensaje_permiso_camara_denegado), Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == 101){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageSelector(); // Abre selector si se concede el permiso
            } else {
                Toast.makeText(this, this.getString(R.string.mensaje_permiso_almacenamiento_denegado), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, this.getString(R.string.mensaje_error_crear_imagen), Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                previewPhotoUri = FileProvider.getUriForFile(this, this.getString(R.string.file_provider), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, previewPhotoUri);
            }
        } else {
            Toast.makeText(this, this.getString(R.string.mensaje_error_encontrar_camara), Toast.LENGTH_SHORT).show();
        }



        // Intent para seleccionar una imagen de la galería
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Crear un "chooser" que permite elegir entre la cámara o la galería
        Intent chooserIntent = Intent.createChooser(pickPhotoIntent, this.getString(R.string.key_intent_imagen));

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

                if (previewPhotoUri != null) { //SE COMPRUEBA QUE EFECTIVAMENTE SER HAYA ELEGIDO UNA IMAGEN UNA VEZ ABIERTA LA CAMARA
                    // El usuario tomó la foto correctamente
                     agregarImagen(previewPhotoUri);

                } else {
                    // El usuario no tomó una foto o canceló
                    Toast.makeText(this, this.getString(R.string.mensaje_no_se_selecciono_imagen), Toast.LENGTH_SHORT).show();
                }
            }

        }
        else{
            // Si no se seleccionó ninguna imagen o se canceló
            Toast.makeText(this, this.getString(R.string.mensaje_no_se_selecciono_imagen), Toast.LENGTH_SHORT).show();
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
