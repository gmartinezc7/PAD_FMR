package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.v3findmyroommate.R;
import androidx.lifecycle.ViewModelProvider;


/*
ACTIVIDAD UTILIZADA PARA MOSTRAR EN DETALLE LA INFORMACIÓN DEL ANUNCIO.
NOS PERMITE VER INFORMACIÓN EXTRA QUE DESDE LA LISTA DE ANUNCIOS NO PODEMOS VER,
COMO POR EJEMPLO LA DESCRIPCIÓN DEL ANUNCIO.

TAMBIÉN TENEMOS ACCESO A "EditarAnuncioActivity" A TRAVÉS DEL BOTÓN "btnEditar"

LA POSICIÓN DE ESTE BOTÓN EN ESTA VIEW ES MUY IMPORTANTE, YA QUE EXPLICA GRAN PARTE DEL FLUJO DE INFORMACIÓN
QUE HAY EN ESTE APARTADO DE LA APLICACIÓN

ESTE ES EL MOTIVO POR EL QUE SE CREA UN editarAnuncioLauncher, PARA PODER RECIBIR LOS DATOS RESULTANTES DE
EDITAR UN ANUNCIO Y A SU VEZ, PODER PASARLOS DE VUELTA AL FRAGMENT CUANDO SE CIERRE ESTA ACTIVITY
(RECORDAR EL "verAnuncioLauncher" DECLARADO EN "MisViviendasFragment"), PARA PODER RELAIZAR LOS CAMBIOS
SOBRE LA LISTA DE ANUNCIOS DEL VIEWMODEL QUE TIENE EL FRAGMENT ("MisViviendasViewModel" DECLARADO EN "MisViviendasFragment").
 */
public class AnuncioDetalleActivity extends AppCompatActivity {


    private TextView tituloText;
    private TextView ubicacionText;
    private TextView metrosText;
    private TextView precioText;
    private TextView descripcionText;


    private ImageView imagenAnuncio;
    private Button btnVolver;
    private Button btnEditar;
    private List<Uri> imagenesUri;
    private int imagenActualIndex = 0;
    private ImageButton btnPrev,btnNext;

    private String titulo;
    private String ubicacion;
    private String metros;
    private String precio;
    private String descripcion;

    //TAGS
    private String categoria;
    private TextView categoriaText;

    //Para la casa:
    private String tipoCasa;
    private String habitaciones;
    private String banos;
    private String exteriorInterior;
    private TextView tipoCasaText;
    private TextView numHabitacionesText;
    private TextView numBanosText;
    private TextView orientacionText;

    //Para la habitación:
    private String companeros;
    private String genero;
    private String tipoBano;
    private TextView numCompanerosText;
    private TextView generoHabitacionesText;
    private TextView orientacionHabitacionText;
    private TextView tipoBanoText;

    LinearLayout opcionesCasa;
    LinearLayout opcionesHabitacion;

    private ActivityResultLauncher<Intent> editarAnuncioLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio_detalle);

        tituloText = findViewById(R.id.titulo_text);
        ubicacionText = findViewById(R.id.ubicacion_text);
        metrosText = findViewById(R.id.metros_text);
        precioText = findViewById(R.id.precio_text);
        descripcionText = findViewById(R.id.descripcion_text);

        imagenAnuncio = findViewById(R.id.imagen_anuncio);

        btnVolver = findViewById(R.id.btn_volver);
        btnEditar = findViewById(R.id.btn_editar);

        btnPrev = findViewById(R.id.btn_prev);
        btnNext = findViewById(R.id.btn_next);


        opcionesCasa = findViewById(R.id.opciones_casa);
        opcionesHabitacion = findViewById(R.id.opciones_habitacion);

        categoriaText = findViewById(R.id.categoria_text);

        tipoCasaText = findViewById(R.id.tipo_casa_text);
        numHabitacionesText = findViewById(R.id.num_habitaciones_text);
        numBanosText = findViewById(R.id.num_banos_text);
        orientacionText = findViewById(R.id.orientacion_text);

        numCompanerosText = findViewById(R.id.num_companeros_text);
        generoHabitacionesText = findViewById(R.id.genero_habitantes_text);
        orientacionHabitacionText = findViewById(R.id.orientacion_habitacion_text);
        tipoBanoText = findViewById(R.id.tipo_bano_text);




        // Obtener el anuncio del intent
        Intent intent = getIntent();
        this.titulo = intent.getStringExtra("titulo");
        this.ubicacion = intent.getStringExtra("ubicacion");
        this.metros = intent.getStringExtra("metros");
        this.precio = intent.getStringExtra("precio");
        this.descripcion = intent.getStringExtra("descripcion");
        this.imagenesUri = new ArrayList<>(intent.getParcelableArrayListExtra("imagenesUri"));

        //TAGS
        this.categoria = intent.getStringExtra("categoria");
        categoriaText.setText(this.categoria);

        if(this.categoria.equalsIgnoreCase("Casa")){

            opcionesCasa.setVisibility(View.VISIBLE);
            opcionesHabitacion.setVisibility(View.GONE);

            this.tipoCasa = intent.getStringExtra("tipoCasa");
            this.habitaciones = intent.getStringExtra("habitaciones");
            this.banos = intent.getStringExtra("banos");
            this.exteriorInterior = intent.getStringExtra("exteriorInterior");

            tipoCasaText.setText(tipoCasa);
            numHabitacionesText.setText( habitaciones);
            numBanosText.setText( banos);
            orientacionText.setText( exteriorInterior);

        }
        else if(categoria.equalsIgnoreCase("Habitación")){

            opcionesCasa.setVisibility(View.GONE);
            opcionesHabitacion.setVisibility(View.VISIBLE);

            this.companeros = intent.getStringExtra("companeros");
            this.genero = intent.getStringExtra("genero");
            this.exteriorInterior = intent.getStringExtra("exteriorInterior");
            this.tipoBano = intent.getStringExtra("tipoBano");

            numCompanerosText.setText(companeros);
            generoHabitacionesText.setText( genero);
            orientacionHabitacionText.setText( exteriorInterior);
            tipoBanoText.setText( tipoBano);

        }


        tituloText.setText(titulo);
        ubicacionText.setText( ubicacion);
        metrosText.setText( metros);
        precioText.setText( precio);
        descripcionText.setText(descripcion);


        iniciarNavImagenes();


        // Inicializar el editarAnuncioLauncher
        editarAnuncioLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();

                        this.titulo = data.getStringExtra("titulo");
                        this.ubicacion = data.getStringExtra("ubicacion");
                        this.metros = data.getStringExtra("metros");
                        this.precio = data.getStringExtra("precio");
                        this.descripcion = data.getStringExtra("descripcion");
                        this.imagenesUri = data.getParcelableArrayListExtra("imagenesUri");
                        iniciarNavImagenes();


                        tituloText.setText(titulo);
                        ubicacionText.setText( ubicacion);
                        metrosText.setText( metros);
                        precioText.setText( precio);
                        descripcionText.setText( descripcion);



                        //TAGS
                        this.categoria = data.getStringExtra("categoria");
                        categoriaText.setText(this.categoria);

                        if(this.categoria.equalsIgnoreCase("Casa")){

                            opcionesCasa.setVisibility(View.VISIBLE);
                            opcionesHabitacion.setVisibility(View.GONE);

                            this.tipoCasa = data.getStringExtra("tipoCasa");
                            this.habitaciones = data.getStringExtra("habitaciones");
                            this.banos = data.getStringExtra("banos");
                            this.exteriorInterior = data.getStringExtra("exteriorInterior");

                            tipoCasaText.setText(tipoCasa);
                            numHabitacionesText.setText( habitaciones);
                            numBanosText.setText( banos);
                            orientacionText.setText( exteriorInterior);

                        }
                        else if(categoria.equalsIgnoreCase("Habitación")){

                            opcionesCasa.setVisibility(View.GONE);
                            opcionesHabitacion.setVisibility(View.VISIBLE);

                            this.companeros = data.getStringExtra("companeros");
                            this.genero = data.getStringExtra("genero");
                            this.exteriorInterior = data.getStringExtra("exteriorInterior");
                            this.tipoBano = data.getStringExtra("tipoBano");

                            numCompanerosText.setText(companeros);
                            generoHabitacionesText.setText( genero);
                            orientacionHabitacionText.setText( exteriorInterior);
                            tipoBanoText.setText( tipoBano);

                        }

                    }
                }
        );


        btnVolver.setOnClickListener(v -> terminarYRegresarInfo());
        btnEditar.setOnClickListener(v -> abrirEdicion());
    }

    private void terminarYRegresarInfo(){

        Intent resultIntent = new Intent();
        resultIntent.putExtra("titulo", this.titulo);
        resultIntent.putExtra("ubicacion",  this.ubicacion);
        resultIntent.putExtra("metros",this.metros);
        resultIntent.putExtra("precio", this.precio);
        resultIntent.putExtra("descripcion", this.descripcion);
        resultIntent.putParcelableArrayListExtra("imagenesUri", new ArrayList<>(imagenesUri));


        //TAGS
        resultIntent.putExtra("categoria", categoria);

        if (categoria.equalsIgnoreCase("Casa")) {

            resultIntent.putExtra("tipoCasa", tipoCasa);
            resultIntent.putExtra("habitaciones", habitaciones);
            resultIntent.putExtra("banos", banos);
            resultIntent.putExtra("exteriorInterior", exteriorInterior);
        } else if (categoria.equalsIgnoreCase("Habitación")) {

            resultIntent.putExtra("companeros", companeros);
            resultIntent.putExtra("genero", genero);
            resultIntent.putExtra("exteriorInterior", exteriorInterior);
            resultIntent.putExtra("tipoBano", tipoBano);
        }



        setResult(RESULT_OK, resultIntent);
        finish();

    }



    private void abrirEdicion() {
        Intent intent = new Intent(this, EditarAnuncioActivity.class);


        intent.putExtra("titulo", titulo);
        intent.putExtra("ubicacion", ubicacion);
        intent.putExtra("metros", metros);
        intent.putExtra("precio", precio);
        intent.putExtra("descripcion", descripcion);
        intent.putParcelableArrayListExtra("imagenesUri",  new ArrayList<>(imagenesUri));


        //TAGS
        intent.putExtra("categoria", categoria);

        if (categoria.equalsIgnoreCase("Casa")) {

            intent.putExtra("tipoCasa", tipoCasa);
            intent.putExtra("habitaciones", habitaciones);
            intent.putExtra("banos", banos);
            intent.putExtra("exteriorInterior", exteriorInterior);
        } else if (categoria.equalsIgnoreCase("Habitación")) {

            intent.putExtra("companeros", companeros);
            intent.putExtra("genero", genero);
            intent.putExtra("exteriorInterior", exteriorInterior);
            intent.putExtra("tipoBano", tipoBano);
        }


        editarAnuncioLauncher.launch(intent); // Usar el nuevo launcher
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