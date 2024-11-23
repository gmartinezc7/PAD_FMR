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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.ucm.fdi.v3findmyroommate.R;


public class EditarAnuncioActivity extends AppCompatActivity {
    private MisViviendasViewModel viewModel;

    private String idAnuncio;
    private String titulo;
    private String ubicacion;
    private String metros;
    private String precio;
    private String descripcion;
    private List<Uri> imagenesUri;


    private ImageView imagenAnuncio;
    private int imagenActualIndex = 0;
    private ImageButton btnPrev,btnNext;


    EditText editTitulo ;
    EditText editUbicacion ;
    EditText editMetros ;
    EditText editPrecio ;
    EditText editDescripcion ;
    private Button btnSeleccionarImagen, btnEliminarImagen;
    private Uri previewPhotoUri;


    //TAGS
    private String categoria;

    //Para la casa:
    private String tipoCasa;
    private String habitaciones;
    private String banos;
    private String exteriorInterior;


    //Para la habitación:
    private String companeros;
    private String genero;
    private String tipoBano;

    Spinner spinnerCategoria;
    Spinner spinnerTipoCasa, spinnerHabitaciones, spinnerBanos, spinnerExteriorInteriorCasa;
    Spinner spinnerCompaneros, spinnerGenero, spinnerExteriorInteriorHabitacion, spinnerTipoBano;

    LinearLayout opcionesCasa;
    LinearLayout opcionesHabitacion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_anuncio);

        Intent intent = getIntent();
        this.idAnuncio = intent.getStringExtra("id");
        this.titulo = intent.getStringExtra("titulo");
        this.ubicacion = intent.getStringExtra("ubicacion");
        this.metros = intent.getStringExtra("metros");
        this.precio = intent.getStringExtra("precio");
        this.descripcion = intent.getStringExtra("descripcion");
        this.imagenesUri = new ArrayList<>(intent.getParcelableArrayListExtra("imagenesUri"));




        imagenAnuncio = findViewById(R.id.imagen_anuncio);
        btnPrev = findViewById(R.id.btn_prev);
        btnNext = findViewById(R.id.btn_next);
        btnSeleccionarImagen = findViewById(R.id.btn_seleccionar_imagen);
        btnEliminarImagen = findViewById(R.id.btn_eliminar_imagen);

            // Llenar los campos con la información del anuncio
             editTitulo = findViewById(R.id.edit_titulo);
             editUbicacion = findViewById(R.id.edit_ubicacion);
             editMetros = findViewById(R.id.edit_metros);
             editPrecio = findViewById(R.id.edit_precio);
             editDescripcion = findViewById(R.id.edit_descripcion);

            editTitulo.setText( this.titulo );
            editUbicacion.setText(this.ubicacion);
            editMetros.setText(String.valueOf(this.metros));
            editPrecio.setText(String.valueOf(this.precio));
            editDescripcion.setText(this.descripcion);

        previewPhotoUri = null;





        //TAGS
        spinnerCategoria = findViewById(R.id.spinner_categoria);
        opcionesCasa = findViewById(R.id.opciones_casa);
        opcionesHabitacion = findViewById(R.id.opciones_habitacion);

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


        this.categoria = intent.getStringExtra("categoria");

        if(this.categoria.equalsIgnoreCase("Casa")){

            spinnerCategoria.setSelection(0);
            opcionesCasa.setVisibility(View.VISIBLE);
            opcionesHabitacion.setVisibility(View.GONE);


            this.tipoCasa = intent.getStringExtra("tipoCasa");
            this.habitaciones = intent.getStringExtra("habitaciones");
            this.banos = intent.getStringExtra("banos");
            this.exteriorInterior = intent.getStringExtra("exteriorInterior");

            setSpinnerValue(spinnerTipoCasa, tipoCasa);
            setSpinnerValue(spinnerHabitaciones, habitaciones);
            setSpinnerValue(spinnerBanos, banos);
            setSpinnerValue(spinnerExteriorInteriorCasa, exteriorInterior);

        }
        else if(categoria.equalsIgnoreCase("Habitación")){

            spinnerCategoria.setSelection(1);
            opcionesCasa.setVisibility(View.GONE);
            opcionesHabitacion.setVisibility(View.VISIBLE);

            this.companeros = intent.getStringExtra("companeros");
            this.genero = intent.getStringExtra("genero");
            this.exteriorInterior = intent.getStringExtra("exteriorInterior");
            this.tipoBano = intent.getStringExtra("tipoBano");

            setSpinnerValue(spinnerCompaneros, companeros);
            setSpinnerValue(spinnerGenero, genero);
            setSpinnerValue(spinnerExteriorInteriorHabitacion, exteriorInterior);
            setSpinnerValue(spinnerTipoBano, tipoBano);

        }


        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { // Casa
                    opcionesCasa.setVisibility(View.VISIBLE);
                    opcionesHabitacion.setVisibility(View.GONE);
                } else if (position == 1) { // Habitación
                    opcionesCasa.setVisibility(View.GONE);
                    opcionesHabitacion.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                opcionesCasa.setVisibility(View.GONE);
                opcionesHabitacion.setVisibility(View.GONE);
            }
        });



        iniciarNavImagenes();



        Button btnGuardar = findViewById(R.id.btn_actualizar_anuncio);
        btnGuardar.setOnClickListener(v -> {
            actualizarAnuncio();
        });

        // Botón para seleccionar imagen
        btnSeleccionarImagen.setOnClickListener(v -> {
            // Verifica y solicita permisos
            requestPermissions();
        });

        // Botón para eliminar imagen
        btnEliminarImagen.setOnClickListener(v -> {
            eliminarImagenSeleccionada();
        });


        // Botón de cancelar
        Button btnCancelar = findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(v -> finish());
    }





    private void setSpinnerValue(Spinner spinner, String value) {
        if (value != null) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
            int position = adapter.getPosition(value);
            if (position >= 0) {
                spinner.setSelection(position);
            }
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

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);


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



    private void actualizarAnuncio() {
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
        resultIntent.putExtra("id", idAnuncio);
        resultIntent.putExtra("titulo", titulo);
        resultIntent.putExtra("ubicacion", ubicacion);
        resultIntent.putExtra("metros", metros);
        resultIntent.putExtra("precio", precio);
        resultIntent.putExtra("descripcion", descripcion);
        resultIntent.putParcelableArrayListExtra("imagenesUri", new ArrayList<>(imagenesUri));


        //GUARDAMOS TAMBIÉN LAS ETIQUETAS
        String categoria = spinnerCategoria.getSelectedItem().toString();
        resultIntent.putExtra("categoria", categoria);

        // Guardamos los datos específicos según la categoría
        if (categoria.equalsIgnoreCase("Casa")) {

            resultIntent.putExtra("tipoCasa", spinnerTipoCasa.getSelectedItem().toString());
            resultIntent.putExtra("habitaciones", spinnerHabitaciones.getSelectedItem().toString());
            resultIntent.putExtra("banos", spinnerBanos.getSelectedItem().toString());
            resultIntent.putExtra("exteriorInterior", spinnerExteriorInteriorCasa.getSelectedItem().toString());
        } else if (categoria.equalsIgnoreCase("Habitación")) {

            resultIntent.putExtra("companeros", spinnerCompaneros.getSelectedItem().toString());
            resultIntent.putExtra("genero", spinnerGenero.getSelectedItem().toString());
            resultIntent.putExtra("exteriorInterior", spinnerExteriorInteriorHabitacion.getSelectedItem().toString());
            resultIntent.putExtra("tipoBano", spinnerTipoBano.getSelectedItem().toString());
        }

        CrearAnuncioActivity.guardarAnuncioEnBD(new Anuncio(resultIntent), this.getApplication());
        setResult(RESULT_OK, resultIntent);
        finish();
    }




    private void iniciarNavImagenes(){

        imagenAnuncio.setImageURI(imagenesUri.get(imagenActualIndex));
        btnPrev.setVisibility(imagenActualIndex > 0 ? View.VISIBLE : View.INVISIBLE);
        btnNext.setVisibility(imagenActualIndex < imagenesUri.size() - 1 ? View.VISIBLE : View.INVISIBLE);


        // Navegar hacia la imagen anterior
        btnPrev.setOnClickListener(v -> navigateImage( -1));
        // Navegar hacia la imagen siguiente
        btnNext.setOnClickListener(v -> navigateImage( 1));


    }


    private void navigateImage( int direction) {
        int newIndex = imagenActualIndex + direction;
        if (newIndex >= 0 && newIndex < imagenesUri.size()) {
            imagenActualIndex = newIndex;
            imagenAnuncio.setImageURI(imagenesUri.get(imagenActualIndex));
            btnPrev.setVisibility(imagenActualIndex > 0 ? View.VISIBLE : View.INVISIBLE);
            btnNext.setVisibility(imagenActualIndex < imagenesUri.size() - 1 ? View.VISIBLE : View.INVISIBLE);
        }
    }

}