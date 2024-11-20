package es.ucm.fdi.v3findmyroommate.ui.config;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;

import es.ucm.fdi.v3findmyroommate.Lobby;
import es.ucm.fdi.v3findmyroommate.MainActivity;
import es.ucm.fdi.v3findmyroommate.PreferencesFragment.BaseFragment;
import es.ucm.fdi.v3findmyroommate.PreferencesFragment.PersonalInformationFragment;
import es.ucm.fdi.v3findmyroommate.PreferencesFragment.PreferenceUser;
import es.ucm.fdi.v3findmyroommate.PreferencesFragment.PropertyTypeFragment;
import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.SignUp;
import es.ucm.fdi.v3findmyroommate.databinding.FragmentConfigBinding;

public class ConfigFragment extends Fragment {

    private FragmentConfigBinding binding;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ConfigPreferencesModel ConfigPreferencesModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()))
                .get(ConfigPreferencesModel.class);

        binding = FragmentConfigBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.mAuth = FirebaseAuth.getInstance();

        // Adds preferences fragment here instead of on item click.
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.preferences_frame, new ConfigEditTextPreferencesFragment(ConfigPreferencesModel))
                .commit();

        Button logoutButton = root.findViewById(R.id.log_out_button);
        logoutButton.setOnClickListener(view -> {
            mAuth.signOut();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            FragmentActivity currentActivity = getActivity();
            if (currentActivity != null) {
                currentActivity.finish();
                Toast logoutToast = Toast.makeText(getContext(), R.string.logout_toast_text,
                        Toast.LENGTH_SHORT);
                logoutToast.show();
                Log.d("Logout", "Logout successful");
            }
            else throw new NullPointerException("Error finishing activity");
        });


        Button mofifyPreferencesButton = root.findViewById(R.id.modify_preferences_button);
        mofifyPreferencesButton.setOnClickListener(view -> {
            Intent intent = new Intent(this.getContext(), PreferenceUser.class);
            intent.putExtra(getString(R.string.config_fragment_key), getString(R.string.property_type_fragment_key));
            startActivity(intent);

        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}