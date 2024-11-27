package es.ucm.fdi.v3findmyroommate.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.os.Bundle;
import android.content.Intent;

import es.ucm.fdi.v3findmyroommate.R;

public class VivCreateActivity extends AppCompatActivity {

    private EditText nameIn, addressIn, priceIn, descriptionIn;
    private Button createButton;

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_viv);

        nameIn = findViewById(R.id.nameIN);
        addressIn = findViewById(R.id.addressIN);
        priceIn = findViewById(R.id.priceIN);
        descriptionIn = findViewById(R.id.descriptionIN);
        createButton = findViewById(R.id.buttonCreate);


        createButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String n = nameIn.getText().toString();
                String a = addressIn.getText().toString();
                double p = Double.parseDouble(priceIn.getText().toString());
                String d = descriptionIn.getText().toString();


                // ESTO ESTÁ COMENTADO PORQUE YA NO HACE FALTA CREAR VIVIENDA DESDE AQUÍ
                //Vivienda newViv = new Vivienda (n,p,d,a,0);

                Intent resultIntent = new Intent();
                //resultIntent.putExtra("newViv", newViv);
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });
    }


}
