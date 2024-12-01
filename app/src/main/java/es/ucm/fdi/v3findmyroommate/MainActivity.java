package es.ucm.fdi.v3findmyroommate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;


import java.util.Locale;

import es.ucm.fdi.v3findmyroommate.ui.config.ConfigPreferencesModel;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;

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

        this.emailEditText = findViewById(R.id.emailEditText);
        this.passwordEditText = findViewById(R.id.passwordEditText);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        Button loginButton = findViewById(R.id.loginButton);

        // Adds both locales to the locale class' locale list.
        LocaleUtils.addLocale("es");
        LocaleUtils.addLocale("en_US");

        // Sets the spanish locale as the default locale.
        //LocaleUtils.setDefaultLocale(this, "es");

        Locale[] availableLocales = Locale.getAvailableLocales();
        for (Locale locale : availableLocales) {
            Log.d("Locales", "Locale: " + locale.toString());
        }



        // TEST TEST TEST
        String mess = "Welkcome back!";
        if (LocaleUtils.doesStringMatchAnyLanguage(this, mess, R.string.welcome_message)) {
            Log.d("buttonName", "MATCH");
        }
        else {
            Log.d("buttonName", "NO MATCH");
        }
        // END OF TEST


        loginButton.setOnClickListener(view -> {
            String userEmail = MainActivity.this.emailEditText.getText().toString();
            String userPassword = MainActivity.this.passwordEditText.getText().toString();

            // If the user has entered all the fields.
            if (!userEmail.isEmpty() && !userPassword.isEmpty()) {

                // Accesses database and searches for the user's email.
                mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(MainActivity.this, task -> {
                        if (task.isSuccessful()) {

                            requestPermissions();


                        }
                        else {
                            // If the sign in fails, displays a message to the user.
                            Toast signInFailedToast = Toast.makeText(MainActivity.this,
                                    R.string.sign_in_failed_toast_text, Toast.LENGTH_SHORT);
                            signInFailedToast.show();
                            Log.w("SignIn", "Sign in failed", task.getException());
                        }
                    });
            }

            else {
                Toast fillAlFieldsToast = Toast.makeText(MainActivity.this,
                        R.string.fill_all_fields_toast_text, Toast.LENGTH_SHORT);
                fillAlFieldsToast.show();
            }

        });

        TextView signUP = findViewById(R.id.signUP);
        signUP.setOnClickListener(view -> openSignUPView());
        signUP.setPaintFlags(signUP.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        signUP.setOnClickListener(view -> openSignUPView());

        signUP.setOnClickListener(view -> openSignUPView());
    }


    private void abrirTrasIniciar(){

        // If the sign in is successful, updates preferences signed-in user's information.
        ConfigPreferencesModel.setInitialPreferences(this.getApplication());
        openLoginView(); // Goes to the next screen.
        Log.d("SignIn", "Sign in successful");


    }


    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);

            } else {
                abrirTrasIniciar();
            }
        } else {
            abrirTrasIniciar();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Llamada a la implementación base
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirTrasIniciar();
            } else {
                Toast.makeText(this, this.getString(R.string.mensaje_permisos_requeridos_denegados), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void openLoginView(){
        Intent intent = new Intent(MainActivity.this, Lobby.class);
        startActivity(intent);
    }


    public void openSignUPView(){
        Intent intent = new Intent(MainActivity.this, SignUp.class);
        startActivity(intent);
    }


}