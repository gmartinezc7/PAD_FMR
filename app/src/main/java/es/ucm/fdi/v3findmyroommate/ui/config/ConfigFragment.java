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
            Intent intent = new Intent(ConfigFragment. this,PreferenceUser.class);
            //Intent intent = new Intent(getContext(), PropertyTypeFragment.class);
            //Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.preferences_fragment);

            //NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            //NavigationUI.setupActionBarWithNavController(this.getActivity(), navController);
            //navController.navigate(R.id.propertyTypeFragment);
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.preferences_fragment, new PropertyTypeFragment())
                    .commit();



            /*
            Fragment newFragment = new PropertyTypeFragment(); // Replace with your target fragment
            //FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

            Fragment currentFragment = requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (currentFragment != null) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.remove(currentFragment);
                transaction.commitNow(); // Commit immediately to ensure cleanup
                transaction.show(newFragment);
                transaction.commitNow(); // Commit immediately to ensure cleanup
            }

             */
            /*
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null); // Optional: Add to back stack for navigation
            transaction.commit();

             */


            if (getActivity() != null) {
                    /*

                FragmentActivity activity = getActivity(); // Obtén la actividad actual
                if (activity != null) {
                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

                    // Reemplaza el fragmento padre (ConfigFragment) por PropertyTypeFragment
                    transaction.replace(R.id.fragment_container, new PropertyTypeFragment());

                    // Opcional: Añadir a la pila de retroceso
                    transaction.addToBackStack(null);

                    // Finaliza la transacción
                    transaction.commit();
                    Log.d("ModifyPreferences", "Parent fragment replaced successfully");
                }
                if (isAdded()) {
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                    Fragment currentFragment = getChildFragmentManager().findFragmentById(R.id.preferences_frame);

                    if (currentFragment != null) {
                        transaction.remove(currentFragment);
                    }

                    transaction.replace(R.id.preferences_frame, new PropertyTypeFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

                     */

                /*

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.preferences_fragment);
                if (currentFragment != null) {
                    transaction.detach(currentFragment);
                }
                transaction.replace(R.id.preferences_fragment, new PropertyTypeFragment());
                transaction.addToBackStack(null);
                transaction.setReorderingAllowed(true);
                transaction.commit();
                 */

            }




            //Intent intent = new Intent(getContext().getApplicationContext(), Lobby.class);
            //startActivity(intent);
            /*
            FragmentActivity currentActivity = getActivity();
            if (currentActivity != null) {
                //currentActivity.finish();
                Log.d("ModifyPreferences", "Successfully initialized the modification fragment");
            }
            else throw new NullPointerException("Error loading the preference's modification fragment");

             */
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}