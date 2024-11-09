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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import es.ucm.fdi.v3findmyroommate.MainActivity;
import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.SignUp;
import es.ucm.fdi.v3findmyroommate.databinding.FragmentConfigBinding;

public class ConfigFragment extends Fragment {

    private FragmentConfigBinding binding;
    private Button logoutButton;
    private FirebaseDatabase databaseInstance;
    private FirebaseAuth authenticationService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ConfigViewModel configViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()))
                .get(ConfigViewModel.class);

        binding = FragmentConfigBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.databaseInstance = FirebaseDatabase.getInstance(this.getResources().
                getString(R.string.database_url));
        this.authenticationService = FirebaseAuth.getInstance();

        this.logoutButton = root.findViewById(R.id.log_out_button);
        this.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticationService.signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                try {
                    getActivity().finish();
                }
                catch (NullPointerException nPE) {
                    Log.e("Logout", "Error finishing activity", nPE);
                }
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Toast logout_toast = Toast.makeText(getContext(), R.string.logout_toast_text, Toast.LENGTH_SHORT);
                logout_toast.show();
            }
        });

        // Add preferences fragment here instead of on item click
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.preferences_frame, new ConfigEditTextPreferencesFragment(configViewModel))
                .commit();



        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}