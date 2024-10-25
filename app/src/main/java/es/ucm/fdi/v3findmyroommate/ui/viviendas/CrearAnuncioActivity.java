package es.ucm.fdi.v3findmyroommate.ui.viviendas;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import es.ucm.fdi.v3findmyroommate.R;

public class CrearAnuncioActivity extends AppCompatActivity {

    private EditText editTitulo, editUbicacion, editMetros, editPrecio;
    private Button btnGuardar;
    private Button btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_anuncio);

        editTitulo = findViewById(R.id.edit_titulo);
        editUbicacion = findViewById(R.id.edit_ubicacion);
        editMetros = findViewById(R.id.edit_metros);
        editPrecio = findViewById(R.id.edit_precio);
        btnGuardar = findViewById(R.id.btn_guardar_anuncio);

        btnGuardar.setOnClickListener(v -> {
            String titulo = editTitulo.getText().toString();
            String ubicacion = editUbicacion.getText().toString();
            String metros = editMetros.getText().toString();
            String precio = editPrecio.getText().toString();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("titulo", titulo);
            resultIntent.putExtra("ubicacion", ubicacion);
            resultIntent.putExtra("metros", metros);
            resultIntent.putExtra("precio", precio);

            setResult(RESULT_OK, resultIntent);
            finish();
        });

        btnCancelar = findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(v -> {
            // Termina la actividad y vuelve a la anterior
            finish();
        });

    }
}