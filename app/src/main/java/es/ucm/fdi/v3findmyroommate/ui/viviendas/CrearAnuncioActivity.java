package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.Manifest;
import android.app.Application;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import es.ucm.fdi.v3findmyroommate.R;


public class CrearAnuncioActivity extends AppCompatActivity {



    private EditText editTitulo, editUbicacion, editMetros, editPrecio, editDescripcion;
    private Button btnGuardar, btnCancelar, btnSeleccionarImagen, btnEliminarImagen ;
    private ImageView imagenAnuncio;


    private List<Uri> imagenesUri = new ArrayList<>();
    private int imagenActualIndex = 0; // Índice de la imagen actual
    private ImageButton btnPrev, btnNext;

    private Uri previewPhotoUri;


    Spinner spinnerCategoria;
    LinearLayout opcionesCasa;
    LinearLayout opcionesHabitacion;

    Spinner spinnerTipoCasa, spinnerHabitaciones, spinnerBanos, spinnerExteriorInteriorCasa;
    Spinner spinnerCompaneros, spinnerGenero, spinnerExteriorInteriorHabitacion, spinnerTipoBano;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_anuncio);

        // Inicialización de vistas
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


        previewPhotoUri = null;


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

    }

    private void guardarAnuncio() {
        String titulo = editTitulo.getText().toString();
        String ubicacion = editUbicacion.getText().toString();
        String metros = editMetros.getText().toString();
        String precio = editPrecio.getText().toString();
        String descripcion = editDescripcion.getText().toString();
        // Verifica si todos los campos están llenos
        if (titulo.isEmpty() || ubicacion.isEmpty() || metros.isEmpty()
                || precio.isEmpty() /*|| imagenesUri.isEmpty()*/) {
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

        Anuncio nuevoAnuncio = new Anuncio(resultIntent);
        CrearAnuncioActivity.guardarAnuncioEnBD(nuevoAnuncio, this.getApplication());


        setResult(RESULT_OK, resultIntent);
        finish();
    }


    // Función que guarda el anuncio en la base de datos.
    public static void guardarAnuncioEnBD(Anuncio nuevoAnuncio, Application application) {

        // Guarda el ID del anuncio en la lista de anuncios de este usuario.
        CrearAnuncioActivity.guardarAnuncioListaAnunciosUsuario(nuevoAnuncio, application);

        // Obtiene una instancia de la BD.
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(application.
                getApplicationContext().getString(R.string.database_url));

        // Obtiene la referencia a la BD de anuncios.
        DatabaseReference databaseAddReference = databaseInstance.getReference("adds")
                .child(nuevoAnuncio.getId());

            databaseAddReference.child(application.getString(R.string.add_title_db_label))
                .setValue(nuevoAnuncio.getTitulo());
        databaseAddReference.child(application.getString((R.string.add_location_db_label)))
                .setValue(nuevoAnuncio.getUbicacion());
        databaseAddReference.child(application.getString((R.string.add_square_meters_db_label)))
                .setValue(nuevoAnuncio.getMetros());
        databaseAddReference.child(application.getString((R.string.add_price_db_label)))
                .setValue(nuevoAnuncio.getPrecio());
        databaseAddReference.child(application.getString((R.string.add_description_db_label)))
                .setValue(nuevoAnuncio.getDescripcion());

        List<String> listaImagenesAnuncioFormatoString = MisViviendasFragment.convierteListaUrisAListaStrings(nuevoAnuncio.getImagenesUri());
        databaseAddReference.child(application.getString((R.string.add_uri_list_db_label)))
                .setValue(listaImagenesAnuncioFormatoString);

        String categoria = nuevoAnuncio.getCategoria();
        databaseAddReference.child(application.getString((R.string.property_type_db_label)))
                .setValue(categoria);

        if (categoria.equals(application.getString(R.string.house_property_type_label))) {  // Si selecciona una casa.
            databaseAddReference.child(application.getString(R.string.add_house_type_db_label))
                    .setValue(nuevoAnuncio.getTipoCasa());
            databaseAddReference.child(application.getString(R.string.num_rooms_db_label))
                    .setValue(nuevoAnuncio.getHabitaciones());
            databaseAddReference.child(application.getString(R.string.num_bathrooms_db_label))
                    .setValue(nuevoAnuncio.getBanos());
            databaseAddReference.child(application.getString(R.string.orientation_db_label))
                    .setValue(nuevoAnuncio.getExteriorInterior());
        }
        else if (categoria.equals(application.getString(R.string.room_property_type_label))) {  // Si selecciona una habitación
            databaseAddReference.child(application.getString(R.string.max_num_roommates_db_label))
                    .setValue(nuevoAnuncio.getCompaneros());
            databaseAddReference.child(application.getString(R.string.roommate_gender_db_label))
                    .setValue(nuevoAnuncio.getGenero());
            databaseAddReference.child(application.getString(R.string.orientation_db_label))
                    .setValue(nuevoAnuncio.getExteriorInterior());
            databaseAddReference.child(application.getString(R.string.bathroom_type_db_label))
                    .setValue(nuevoAnuncio.getTipoBano());
        }

    }


    // Función que guarda el anuncio actual en la lista de anuncios del usuario.
    private static void guardarAnuncioListaAnunciosUsuario(Anuncio nuevoAnuncio, Application application) {
        // Obtiene el usuario actual.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            // Obtiene una instancia de la BD.
            FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(application.
                    getApplicationContext().getString(R.string.database_url));

            // Obtiene la referencia a la BD de usuarios.
            DatabaseReference databaseUserReference = databaseInstance.getReference("users")
                    .child(user.getUid());

            databaseUserReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    // Obtiene la lista actual de anuncios de ese usuario
                    List<String> userAddsIdsList = new ArrayList<String>();

                    List<String> userAddsInDB = snapshot.child(application
                        .getString(R.string.user_adds_list_db_label)).getValue(
                        new GenericTypeIndicator<List<String>>() {
                        });

                    if (userAddsInDB != null) {
                        // Añade el ID del anuncio a la lista y la actualiza en la BD.
                        userAddsInDB.add(nuevoAnuncio.getId());
                        databaseUserReference.child(application.getString(R.string.user_adds_list_db_label))
                            .setValue(userAddsInDB);
                    }
                    else {
                        userAddsIdsList.add(nuevoAnuncio.getId());
                        databaseUserReference.child(application.getString(R.string.user_adds_list_db_label))
                            .setValue(userAddsIdsList);
                    }
                }
            });
        }
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
