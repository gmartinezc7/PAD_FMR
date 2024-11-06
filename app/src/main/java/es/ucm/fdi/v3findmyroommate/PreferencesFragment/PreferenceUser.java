package es.ucm.fdi.v3findmyroommate.PreferencesFragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import es.ucm.fdi.v3findmyroommate.R;

public class PreferenceUser extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_user);

        // Cargar el primer fragmento
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PersonalInformationFragment())
                    .commit();
        }

    }
}
