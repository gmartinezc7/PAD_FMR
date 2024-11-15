package es.ucm.fdi.v3findmyroommate;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;

import es.ucm.fdi.v3findmyroommate.PreferencesFragment.PreferenceUser;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUp";
    private EditText userNameEditText;
    private EditText lastNameEditText;
    private EditText userEmailEditText;
    private EditText userPasswordTextEdit;
    private EditText userConfirmPasswordTextEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Obtén las referencias de los EditText
        userNameEditText = findViewById(R.id.userNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        userEmailEditText = findViewById(R.id.userEmailEditText);
        userPasswordTextEdit = findViewById(R.id.userPasswordTextEdit);
        userConfirmPasswordTextEdit = findViewById(R.id.confirmUserPasswordEditText);

        TextView txt = (TextView) findViewById(R.id.signInTextView);
        txt.setPaintFlags(txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    }

    public void createAccount(View view) {
        try {
            // Obtener los datos de los campos de texto
            String name = userNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String email = userEmailEditText.getText().toString().trim();
            String password = userPasswordTextEdit.getText().toString().trim();
            String confirmPassword = userConfirmPasswordTextEdit.getText().toString().trim();

            // Verificar que todos los campos estén completos
            if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                // Mostrar mensaje de error si algún campo está vacío
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar que las contraseñas coincidan
            if (!password.equals(confirmPassword)) {
                // Mostrar mensaje de error si las contraseñas no coinciden
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            // Iniciar la siguiente actividad
            Intent intent = new Intent(SignUp.this, PreferenceUser.class);
            intent.putExtra("userName", name);
            intent.putExtra("lastName", lastName);
            intent.putExtra("userEmail", email);
            intent.putExtra("userPassword", password);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void returnLogin(View view) {
        Intent intent = new Intent(SignUp.this, MainActivity.class);
        startActivity(intent);
    }
}