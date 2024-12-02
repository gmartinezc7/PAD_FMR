package es.ucm.fdi.v3findmyroommate.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.TranslationUtils;
import es.ucm.fdi.v3findmyroommate.ui.viviendas.EditarAnuncioActivity;

public class ViviendaDetalleActivity extends AppCompatActivity {


    //TEXTVIEWS OBLIGATORIAS
    private TextView tituloText;
    private TextView ubicacionText;
    private TextView metrosText;
    private TextView precioText;
    private TextView descripcionText;
    //LISTA DE URIS PARA LAS IMAGENES, MINIMO OBLIGATORIO TENER 1
    private List<String> imagenesUri;

    //IMAGEN DEL ANUNCIO QUE SE MUESTRA
    private ImageView imagenAnuncio;
    //INDICE DE LA IMAGEN QUE SE MUESTRA
    private int imagenActualIndex = 0;

    //BOTONES NECESARIOS, EL DE VOLVER, EDITAR  Y EL DE LAS FLECHAS PARA PASAR LAS IMAGENES
    private Button btnVolver;
    private ImageButton btnPrev, btnNext;

    //STRINGS SIMPLEMENTE PARA MANEJAR LOS DATOS QUE VIENEN DE LOS INTENTS,
    // PONERLOS EN LOS TEXTVIEWS O VOLVER A METERLO EN INTENTS.
    private String idAnuncio;
    private String titulo;
    private String ubicacion;
    private String metros;
    private String precio;
    private String descripcion;


    //------------------------------------------------------------------------------------------------
    //PARTE DE LOS TAGS: (AL IGUAL QUE EN LO ANTERIOR, SE USAN TEXTVIEWS Y STRINGS PARA GUARDAR LA INFO Y MANEJARLA)
    private String categoria;
    private TextView categoriaText;

    //TAGS SI SE ELIGE LA OPCION DE CASA
    private String tipoCasa;
    private String habitaciones;
    private String banos;
    private String exteriorInterior;
    private TextView tipoCasaText;
    private TextView numHabitacionesText;
    private TextView numBanosText;
    private TextView orientacionText;

    //TAGS SI SE ESCOGE HABITACION
    private String companeros;
    private String genero;
    private String tipoBano;
    private TextView numCompanerosText;
    private TextView generoHabitacionesText;
    private TextView orientacionHabitacionText;
    private TextView tipoBanoText;

    //LINEARLAYOUTS QUE APARECEN O DESAPARECEN DEPENDIENDO DE SI LA OPCION ESCOGIDA ES UNA CASA O HABITACION
    LinearLayout opcionesCasa;
    LinearLayout opcionesHabitacion;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vivienda_detalle);


        enlazarIdsVista();

        obtenerDatosAnuncioInicial();


        establecerAccionesBotones();


    }


    private void enlazarIdsVista() {

        tituloText = findViewById(R.id.titulo_text);
        ubicacionText = findViewById(R.id.ubicacion_text);
        metrosText = findViewById(R.id.metros_text);
        precioText = findViewById(R.id.precio_text);
        descripcionText = findViewById(R.id.descripcion_text);
        imagenAnuncio = findViewById(R.id.imagen_anuncio);
        btnVolver = findViewById(R.id.btn_volver);
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


    }


    // OBTIENE EL ANUNCIO DEL INTENT Y RELLENA LOS TEXTVIEWS
    private void obtenerDatosAnuncioInicial() {

        Intent intent = getIntent();
        obtenerYEstablecerDatos(intent);

    }




    //ESTABLECEMOS LOS LISTENERS DE LOS BOTONES Y TAMBIEN LA ACCION TIPICA DE REGRESAR HACIA ATRAS CON LA FLECHA DEL PANEL DE
    //ANDROID, PARA EVITAR PROBLEMAS SI SE REGRESA CON DICHA FLECHA
    private void establecerAccionesBotones() {

        // Registrar callback para manejar la acción de retroceso
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish(); // Reutilizamos el método para guardar la información
            }
        });

        btnVolver.setOnClickListener(v ->  finish());

    }


//FUNCION AUXILIAR SIMPLEMENTE PARA MANEJAR LOS DATOS Y MOSTRARLOS, SE USA TANTO PARA EL INTENT AL ABRIR LA ACTIVIDAD
    //COMO EN EL RESULT CUANDO REGRESAN LOS DATOS DE LA ACTIVIDAD DE EDITAR.

    private void obtenerYEstablecerDatos(Intent data) {

        this.idAnuncio = data.getStringExtra(this.getString(R.string.key_id));
        this.titulo = data.getStringExtra(this.getString(R.string.key_titulo));
        this.ubicacion = data.getStringExtra(this.getString(R.string.key_ubicacion));
        this.precio = data.getStringExtra(this.getString(R.string.key_precio));
        this.descripcion = data.getStringExtra(this.getString(R.string.key_descripcion));
        this.imagenesUri = data.getStringArrayListExtra(this.getString(R.string.key_imagenes_uri));
        iniciarNavImagenes();


        tituloText.setText(titulo);
        ubicacionText.setText(ubicacion);
        precioText.setText(precio);
        descripcionText.setText(descripcion);


        //TAGS
        this.categoria = data.getStringExtra(this.getString(R.string.key_categoria));
        categoriaText.setText(TranslationUtils.reverseTranslateIfNeeded(this.categoria));

        if (this.categoria.equalsIgnoreCase(this.getString(R.string.category_casa))) {

            opcionesCasa.setVisibility(View.VISIBLE);
            opcionesHabitacion.setVisibility(View.GONE);
            this.metros = data.getStringExtra(this.getString(R.string.key_metros));
            this.tipoCasa = data.getStringExtra(this.getString(R.string.key_tipo_casa));
            this.habitaciones = data.getStringExtra(this.getString(R.string.key_habitaciones));
            this.banos = data.getStringExtra(this.getString(R.string.key_banos));
            this.exteriorInterior = data.getStringExtra(this.getString(R.string.key_exterior_interior));

            metrosText.setText(metros);
            tipoCasaText.setText(TranslationUtils.reverseTranslateIfNeeded(tipoCasa));
            numHabitacionesText.setText(habitaciones);
            numBanosText.setText(banos);
            orientacionText.setText(TranslationUtils.reverseTranslateIfNeeded(exteriorInterior));

        } else if (categoria.equalsIgnoreCase(this.getString(R.string.category_habitacion))) {

            opcionesCasa.setVisibility(View.GONE);
            opcionesHabitacion.setVisibility(View.VISIBLE);

            this.companeros = data.getStringExtra(this.getString(R.string.key_companeros));
            this.genero = data.getStringExtra(this.getString(R.string.key_genero));
            this.exteriorInterior = data.getStringExtra(this.getString(R.string.key_exterior_interior));
            this.tipoBano = data.getStringExtra(this.getString(R.string.key_tipo_bano));

            numCompanerosText.setText(companeros);
            generoHabitacionesText.setText(TranslationUtils.reverseTranslateIfNeeded(genero));
            orientacionHabitacionText.setText(TranslationUtils.reverseTranslateIfNeeded(exteriorInterior));
            tipoBanoText.setText(TranslationUtils.reverseTranslateIfNeeded(tipoBano));

        }


    }






    //DETERMINA LA VISIBILIDAD DE LAS FLECHAS DEPENDIENDO DEL NUMERO DE IMAGENES
    //QUE HAYA EN LA LISTA Y DE LA POSICION DE LA IMAGEN QUE SE OBSERVA
    //TAMBIEN DETERMINA LA IMAGEN QUE SE VE EN EL CUADRO SEGUN EL INDICE ACTUAL
    private void iniciarNavImagenes() {

        // Cargar la imagen actual usando Glide
        Glide.with(imagenAnuncio.getContext())
                .load(imagenesUri.get(imagenActualIndex))
                .into(imagenAnuncio);
        btnPrev.setVisibility(imagenActualIndex > 0 ? View.VISIBLE : View.INVISIBLE);
        btnNext.setVisibility(imagenActualIndex < imagenesUri.size() - 1 ? View.VISIBLE : View.INVISIBLE);


        // Navegar hacia la imagen anterior
        btnPrev.setOnClickListener(v -> navigateImage(-1));
        // Navegar hacia la imagen siguiente
        btnNext.setOnClickListener(v -> navigateImage(1));


    }


    private void navigateImage(int direction) {
        int newIndex = imagenActualIndex + direction;
        if (newIndex >= 0 && newIndex < imagenesUri.size()) {
            imagenActualIndex = newIndex;
            // Cargar la imagen actual usando Glide
            Glide.with(imagenAnuncio.getContext())
                    .load(imagenesUri.get(imagenActualIndex))
                    .into(imagenAnuncio);

            btnPrev.setVisibility(imagenActualIndex > 0 ? View.VISIBLE : View.INVISIBLE);
            btnNext.setVisibility(imagenActualIndex < imagenesUri.size() - 1 ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
