package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.ucm.fdi.v3findmyroommate.R;


public class EditarAnuncioActivity extends AppCompatActivity {


    //STRINGS SIMPLEMENTE PARA MANEJAR LOS DATOS QUE VIENEN DE LOS INTENTS,
    // PONERLOS EN LOS TEXTVIEWS O VOLVER A METERLO EN INTENTS.
    private String idAnuncio;
    private String titulo;
    private String ubicacion;
    private String metros;
    private String precio;
    private String descripcion;
    //LISTA DE URIS PARA LAS IMAGENES, MINIMO OBLIGATORIO TENER 1
    private List<Uri> imagenesUri;

    //IMAGEN DEL ANUNCIO QUE SE MUESTRA
    private ImageView imagenAnuncio,btnEliminarImagen;
    //INDICE DE LA IMAGEN QUE SE MUESTRA
    private int imagenActualIndex = 0;



    EditText editTitulo , editUbicacion,editMetros,editPrecio,editDescripcion;
    TextView editTituloPantalla ;
    private Button btnGuardar, btnCancelar;
    FloatingActionButton btnSeleccionarImagen;

    //BOTONES NECESARIOS, EL DE SELECCIONAR IMAGEN, ELIMINARLA  Y EL DE LAS FLECHAS PARA PASAR LAS IMAGENES

    private ImageButton btnPrev,btnNext;



    private Uri previewPhotoUri;


    //------------------------------------------------------------------------------------------------
    //PARTE DE LOS TAGS: (AL IGUAL QUE EN LO ANTERIOR, SE USAN STRINGS PARA GUARDAR LA INFO Y MANEJARLA)
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

    //SPINNERS DE SELECCION PARA LOS TAGS
    Spinner spinnerCategoria;
    Spinner spinnerTipoCasa, spinnerHabitaciones, spinnerBanos, spinnerExteriorInteriorCasa;
    Spinner spinnerCompaneros, spinnerGenero, spinnerExteriorInteriorHabitacion, spinnerTipoBano;

    //LINEARLAYOUTS QUE APARECEN O DESAPARECEN DEPENDIENDO DE SI LA OPCION ESCOGIDA ES UNA CASA O HABITACION
    LinearLayout opcionesCasa;
    LinearLayout opcionesHabitacion,guardarAnuncio;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_anuncio_2);
        previewPhotoUri = null;

        enlazarIdsVista();

        obtenerDatosAnuncioInicial();



        establecerAccionesSpinners();


        iniciarNavImagenes();


        establecerAccionesBotones();



    }



    private void enlazarIdsVista(){

        imagenAnuncio = findViewById(R.id.imagen_anuncio);
        btnPrev = findViewById(R.id.btn_prev);
        btnNext = findViewById(R.id.btn_next);
        btnSeleccionarImagen = findViewById(R.id.btn_seleccionar_imagen);
        btnEliminarImagen = findViewById(R.id.btn_eliminar_imagen);
        editTituloPantalla = findViewById(R.id.titulo_crear);
        editTituloPantalla.setText(R.string.edit_house_listing);
        guardarAnuncio = findViewById(R.id.guardarAnuncio);


        // Llenar los campos con la información del anuncio
        editTitulo = findViewById(R.id.create_titulo);
        editUbicacion = findViewById(R.id.create_ubicacion);
        editMetros = findViewById(R.id.create_metros);
        editPrecio = findViewById(R.id.create_precio);
        editDescripcion = findViewById(R.id.create_descripcion);

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


    }


    //SE RELLENAN LOS CUADROS Y SPINNERS CON LA INFORMACION QUE TENIA EL ANUNCIO
    private void obtenerDatosAnuncioInicial(){

        Intent intent = getIntent();
        this.idAnuncio = intent.getStringExtra(this.getString(R.string.key_id));
        this.titulo = intent.getStringExtra(this.getString(R.string.key_titulo));
        this.ubicacion = intent.getStringExtra(this.getString(R.string.key_ubicacion));
        this.metros = intent.getStringExtra(this.getString(R.string.key_metros));
        this.precio = intent.getStringExtra(this.getString(R.string.key_precio));
        this.descripcion = intent.getStringExtra(this.getString(R.string.key_descripcion));
        this.imagenesUri = new ArrayList<>(intent.getParcelableArrayListExtra(this.getString(R.string.key_imagenes_uri)));




        editTitulo.setText( this.titulo );
        editUbicacion.setText(this.ubicacion);
        editMetros.setText(String.valueOf(this.metros));
        editPrecio.setText(String.valueOf(this.precio));
        editDescripcion.setText(this.descripcion);




        this.categoria = intent.getStringExtra(this.getString(R.string.key_categoria));

        if(this.categoria.equalsIgnoreCase(this.getString(R.string.category_casa))){

            spinnerCategoria.setSelection(0);
            opcionesCasa.setVisibility(View.VISIBLE);
            opcionesHabitacion.setVisibility(View.GONE);


            this.tipoCasa = intent.getStringExtra(this.getString(R.string.key_tipo_casa));
            this.habitaciones = intent.getStringExtra(this.getString(R.string.key_habitaciones));
            this.banos = intent.getStringExtra(this.getString(R.string.key_banos));
            this.exteriorInterior = intent.getStringExtra(this.getString(R.string.key_exterior_interior));

            setSpinnerValue(spinnerTipoCasa, tipoCasa);
            setSpinnerValue(spinnerHabitaciones, habitaciones);
            setSpinnerValue(spinnerBanos, banos);
            setSpinnerValue(spinnerExteriorInteriorCasa, exteriorInterior);

        }
        else if(categoria.equalsIgnoreCase(this.getString(R.string.category_habitacion))){

            spinnerCategoria.setSelection(1);
            opcionesCasa.setVisibility(View.GONE);
            opcionesHabitacion.setVisibility(View.VISIBLE);

            this.companeros = intent.getStringExtra(this.getString(R.string.key_companeros));
            this.genero = intent.getStringExtra(this.getString(R.string.key_genero));
            this.exteriorInterior = intent.getStringExtra(this.getString(R.string.key_exterior_interior));
            this.tipoBano = intent.getStringExtra(this.getString(R.string.key_tipo_bano));

            setSpinnerValue(spinnerCompaneros, companeros);
            setSpinnerValue(spinnerGenero, genero);
            setSpinnerValue(spinnerExteriorInteriorHabitacion, exteriorInterior);
            setSpinnerValue(spinnerTipoBano, tipoBano);

        }






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


    private void establecerAccionesBotones(){

        // Botón para seleccionar imagen
        btnSeleccionarImagen.setOnClickListener(v -> {
            // Verifica y solicita permisos
            requestPermissions();
        });

        // Botón para eliminar imagen
        btnEliminarImagen.setOnClickListener(v -> {
            eliminarImagenSeleccionada();
        });

        btnGuardar = findViewById(R.id.btn_guardar_anuncio);
        btnGuardar.setOnClickListener(v -> {
            actualizarAnuncio();
        });


        // Botón de cancelar
        btnCancelar = findViewById(R.id.btn_cancelar);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);

            } else {
                openImageSelector();
            }
        } else {
            openImageSelector();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Llamada a la implementación base
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageSelector();
            } else {
                Toast.makeText(this, this.getString(R.string.mensaje_permisos_requeridos_denegados), Toast.LENGTH_SHORT).show();
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

                        if (previewPhotoUri != null) { //SE COMPRUEBA QUE EFECTIVAMENTE SER HAYA ELEGIDO UNA IMAGEN UNA VEZ ABEIRTA LA CAMARA
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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Android 11+ verifica acceso a Scoped Storage
        if (storageDir == null) {
            throw new IOException("No se pudo acceder al directorio.");
        }

        return File.createTempFile(imageFileName, ".jpg", storageDir);
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
            Toast.makeText(this, this.getString(R.string.mensaje_debes_rellenar_todo), Toast.LENGTH_LONG).show();
            return; // Detiene el flujo y no continúa con la creación del anuncio
        }


        Intent resultIntent = new Intent();
        resultIntent.putExtra(this.getString(R.string.key_id), idAnuncio);
        resultIntent.putExtra(this.getString(R.string.key_titulo), titulo);
        resultIntent.putExtra(this.getString(R.string.key_ubicacion), ubicacion);
        resultIntent.putExtra(this.getString(R.string.key_metros), metros);
        resultIntent.putExtra(this.getString(R.string.key_precio), precio);
        resultIntent.putExtra(this.getString(R.string.key_descripcion), descripcion);
        resultIntent.putParcelableArrayListExtra(this.getString(R.string.key_imagenes_uri), new ArrayList<>(imagenesUri));


        List<String> urlPicturesList = new ArrayList<>();

        for (Uri currentPictureUri : this.imagenesUri) {

            MisViviendasFragment.uploadImage(currentPictureUri, getApplication());
            String currentPictureUrlStringFormat = MisViviendasFragment.generateUrl(getApplication());
            urlPicturesList.add(currentPictureUrlStringFormat);

        }


        //GUARDAMOS TAMBIÉN LAS ETIQUETAS
        String categoria = spinnerCategoria.getSelectedItem().toString();
        resultIntent.putExtra(this.getString(R.string.key_categoria), categoria);

        // Guardamos los datos específicos según la categoría
        if (categoria.equalsIgnoreCase(this.getString(R.string.house_property_type_label))) {

            resultIntent.putExtra(this.getString(R.string.key_tipo_casa), spinnerTipoCasa.getSelectedItem().toString());
            resultIntent.putExtra(this.getString(R.string.key_habitaciones), spinnerHabitaciones.getSelectedItem().toString());
            resultIntent.putExtra(this.getString(R.string.key_banos), spinnerBanos.getSelectedItem().toString());
            resultIntent.putExtra(this.getString(R.string.key_exterior_interior), spinnerExteriorInteriorCasa.getSelectedItem().toString());
        } else if (categoria.equalsIgnoreCase(this.getString(R.string.room_property_type_label))) {

            resultIntent.putExtra(this.getString(R.string.key_companeros), spinnerCompaneros.getSelectedItem().toString());
            resultIntent.putExtra(this.getString(R.string.key_genero), spinnerGenero.getSelectedItem().toString());
            resultIntent.putExtra(this.getString(R.string.key_exterior_interior), spinnerExteriorInteriorHabitacion.getSelectedItem().toString());
            resultIntent.putExtra(this.getString(R.string.key_tipo_bano), spinnerTipoBano.getSelectedItem().toString());
        }


        MisViviendasFragment.guardarOActualizarAnuncioEnBD(new Anuncio(this, resultIntent), this.getApplication());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

}