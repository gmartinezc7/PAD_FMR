package es.ucm.fdi.v3findmyroommate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;

import es.ucm.fdi.v3findmyroommate.PreferencesFragment.PreferenceUser;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUp";
    private SharedViewModel sharedViewModel;
    private EditText userNameEditText;
    private EditText lastNameEditText;
    private EditText userEmailEditText;
    private EditText userPasswordTextEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Obtén las referencias de los EditText
        userNameEditText = findViewById(R.id.userNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        userEmailEditText = findViewById(R.id.userEmailEditText);
        userPasswordTextEdit = findViewById(R.id.userPasswordTextEdit);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
    }

    public void createAccount(View view) {
        try {
            // Crea una instancia de User y asigna los datos
            User user = new User();
            user.setName(userNameEditText.getText().toString().trim());
            user.setLastName(lastNameEditText.getText().toString().trim());
            user.setEmail(userEmailEditText.getText().toString().trim());
            user.setPassword(userPasswordTextEdit.getText().toString().trim());

            // Guarda el objeto User en el ViewModel
            sharedViewModel.setUser(user);

            // Inicia la siguiente actividad
            Intent intent = new Intent(SignUp.this, PreferenceUser.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
