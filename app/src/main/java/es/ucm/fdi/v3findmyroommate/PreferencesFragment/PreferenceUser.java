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
        // Retrieves user data sent from another activity using an Intent.
        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String lastName = intent.getStringExtra("lastName");
        String email = intent.getStringExtra("userEmail");
        String password = intent.getStringExtra("userPassword");
        // Creates a User object and sets the retrieved data.
        User user = new User();
        user.setName(userName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);

        // Saves the User object in the shared ViewModel.
        sharedViewModel.setUser(user);

        // Retrieves another Intent to check if a specific fragment should be loaded.
        Intent intent2 = getIntent();
        String nombreActivity = intent2.getStringExtra(getString(R.string.config_fragment_key));

        if (nombreActivity != null) {
            // If a fragment is specified from the preferences screen, load the PropertyTypeFragment.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new PropertyTypeFragment())
                        .commit();
            }
        }

        else {
            // If no fragment is specified, load the PersonalInformationFragment by default.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new PersonalInformationFragment())
                        .commit();
            }
        }

    }
}
