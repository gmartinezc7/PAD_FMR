package es.ucm.fdi.v3findmyroommate;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cloudinary.android.MediaManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.v3findmyroommate.databinding.ActivityMainBinding;
import es.ucm.fdi.v3findmyroommate.ui.config.ConfigPreferencesModel;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;
    public static ActivityMainBinding binding;

    // Cloudinary DB necessary constants.
    public static final String CLOUD_REPOS_NAME = "dhvyxnpau";
    public static final String UPLOAD_PRESET = "fmr_upload_preset";

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
        LocaleUtils.addLocale("en");


        // Inflates view binding (necessary for the Cloudinary API).
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());
        //binding.toolbar.setTitle("Cloudinary Quickstart");
        //setSupportActionBar(binding.toolbar);

        // Start the Cloudinary API.
        initCloudinary();



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



        //Pedir permiso para Notificaciones
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    101
            );
        }

        //Token para inapp-messaging pero no se usa
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        //Guardar token del usuario
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
                        userRef.child("fcmToken").setValue(token);
                    }
                });


    }


    // Init the DB connection.
    private void initCloudinary() {
        Map config = new HashMap();
        config.put("cloud_name", MainActivity.CLOUD_REPOS_NAME);
        config.put("api_key", getString(R.string.cloudinary_api_key));
        config.put("api_secret", getString(R.string.cloudinary_api_secret_key));
        MediaManager.init(this, config);
    }


    private void abrirTrasIniciar(){

        // If the sign in is successful, updates preferences signed-in user's information.
        ConfigPreferencesModel.setInitialPreferences(this.getApplication());
        openLoginView(); // Goes to the next screen.
        Log.d("SignIn", "Sign in successful");


    }


    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Lista de permisos según la versión de Android
            List<String> permisosNecesarios = new ArrayList<>();

            // Permiso de cámara (siempre requerido)
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permisosNecesarios.add(android.Manifest.permission.CAMERA);
            }

            // Permiso de imágenes según la versión de Android
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    permisosNecesarios.add(android.Manifest.permission.READ_MEDIA_IMAGES);
                }
            } else { // Android 12 y anteriores
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    permisosNecesarios.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }

            // Solicitar los permisos necesarios
            if (!permisosNecesarios.isEmpty()) {
                requestPermissions(
                        permisosNecesarios.toArray(new String[0]),
                        100
                );
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
            boolean todosPermisosConcedidos = true;

            for (int resultado : grantResults) {
                if (resultado != PackageManager.PERMISSION_GRANTED) {
                    todosPermisosConcedidos = false;
                    break;
                }
            }

            if (todosPermisosConcedidos) {
                abrirTrasIniciar();
            } else {
                Toast.makeText(this, getString(R.string.mensaje_permisos_requeridos_denegados), Toast.LENGTH_SHORT).show();
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