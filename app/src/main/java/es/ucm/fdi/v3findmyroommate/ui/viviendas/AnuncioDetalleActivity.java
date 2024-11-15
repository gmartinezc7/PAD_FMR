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


    private ActivityResultLauncher<Intent> editarAnuncioLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio_detalle);

        // Inicializar vistas
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

        // Obtener el anuncio del intent
        Intent intent = getIntent();
        this.titulo = intent.getStringExtra("titulo");
        this.ubicacion = intent.getStringExtra("ubicacion");
        this.metros = intent.getStringExtra("metros");
        this.precio = intent.getStringExtra("precio");
        this.descripcion = intent.getStringExtra("descripcion");
        this.imagenesUri = new ArrayList<>(intent.getParcelableArrayListExtra("imagenesUri"));


        tituloText.setText(titulo);
        ubicacionText.setText("Ubicación " + ubicacion);
        metrosText.setText("Metros Cuadrados " + metros);
        precioText.setText( "Precio " +precio);
        descripcionText.setText("Descripción:\n" + descripcion);


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
                        ubicacionText.setText("Ubicación " + ubicacion);
                        metrosText.setText("Metros Cuadrados " + metros);
                        precioText.setText( "Precio " +precio);
                        descripcionText.setText("Descripción:\n" + descripcion);
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