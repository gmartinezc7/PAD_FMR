package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import es.ucm.fdi.v3findmyroommate.R;


public class AnuncioDetalleActivity extends AppCompatActivity {
    private TextView tituloText;
    private TextView detalleText;
    private ImageView imagenAnuncio;
    private Button btnVolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio_detalle);

        // Inicializar vistas
        tituloText = findViewById(R.id.titulo_text);
        detalleText = findViewById(R.id.detalle_text);
        imagenAnuncio = findViewById(R.id.imagen_anuncio);
        btnVolver = findViewById(R.id.btn_volver);
        // Obtener el anuncio del intent
        Intent intent = getIntent();
        String titulo = intent.getStringExtra("titulo");
        String detalle = intent.getStringExtra("detalle");
        Uri imagenUri = intent.getParcelableExtra("imagenUri");

        tituloText.setText(titulo);
        detalleText.setText(detalle);
        imagenAnuncio.setImageURI(imagenUri);


        btnVolver.setOnClickListener(v -> finish());
    }
}