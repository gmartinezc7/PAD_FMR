package es.ucm.fdi.v3findmyroommate.PreferencesFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.SharedViewModel;
import es.ucm.fdi.v3findmyroommate.User;

public class PreferenceUser extends AppCompatActivity {
    private  SharedViewModel sharedViewModel;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_user);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String lastName = intent.getStringExtra("lastName");
        String email = intent.getStringExtra("userEmail");
        String password = intent.getStringExtra("userPassword");

        User user = new User();
        user.setName(userName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        sharedViewModel.setUser(user);


        Intent intent2 = getIntent();
        String nombreActivity = intent2.getStringExtra(getString(R.string.config_fragment_key));

        if (nombreActivity != null) {
            // Loads new PropertyTypeFragment (comes from the Preferences screen).
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new PropertyTypeFragment())
                        .commit();
            }
        }

        else {
            // Loads new PersonalInformationFragment.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new PersonalInformationFragment())
                        .commit();
            }
        }

    }
}
