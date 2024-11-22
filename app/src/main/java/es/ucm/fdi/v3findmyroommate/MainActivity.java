package es.ucm.fdi.v3findmyroommate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //TODO: Quitar cuando roi tenga login
    private FirebaseAuth mAuth;
    ////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        FirebaseApp.initializeApp(this);

        //TODO: Quitar cuando roi tenga login
        mAuth = FirebaseAuth.getInstance();
        loginAutomatically();
        ////////////////////////////

        // Prueba GitHub
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openLoginView();
            }
        });
        TextView signUP = findViewById(R.id.signUP);
        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openSignUPView();}
        });
    }

    public void openLoginView(){
        Intent intent =  new Intent(MainActivity.this, Lobby.class);
        startActivity(intent);

    }
    public void openSignUPView(){
        Intent intent =  new Intent(MainActivity.this, SignUp.class);
        startActivity(intent);

    }


    //TODO QUITARLO CUANDO ROI SUBA LOGIN
    private void loginAutomatically() {
        String email = "roilop01@ucm.es";
        String password = "000000";

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("Mainactivity", "Inicio de sesión exitoso para UID: " + user.getUid());
                        openLobby();
                    } else {
                        // Error en el inicio de sesión
                        Log.e("Mainactivity", "Error al iniciar sesión", task.getException());
                    }
                });
    }

    private void openLobby() {
        Intent intent = new Intent(MainActivity.this, Lobby.class);
        startActivity(intent);
        finish(); // Evitar regresar a esta pantalla
    }
    ///////////////////////////////////////////////
}