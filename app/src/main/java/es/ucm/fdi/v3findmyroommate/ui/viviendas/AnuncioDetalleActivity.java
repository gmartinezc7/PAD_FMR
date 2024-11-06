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
    private TextView idText;
    private TextView detalleText;
    private ImageView imagenAnuncio;
    private Button btnVolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio_detalle);

        // Inicializar vistas
        idText = findViewById(R.id.id_text);
        detalleText = findViewById(R.id.detalle_text);
        imagenAnuncio = findViewById(R.id.imagen_anuncio);
        btnVolver = findViewById(R.id.btn_volver);
        // Obtener el anuncio del intent
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        String detalle = intent.getStringExtra("detalle");
        Uri imagenUri = intent.getParcelableExtra("imagenUri");

        // Configurar los datos en las vistas
        idText.setText("Tu Anuncio: " + id);
        detalleText.setText(detalle);
        imagenAnuncio.setImageURI(imagenUri);


        btnVolver.setOnClickListener(v -> finish());
    }
}