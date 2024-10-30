package es.ucm.fdi.v3findmyroommate.ui.config;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.v3findmyroommate.databinding.FragmentConfigBinding;

public class ConfigFragment extends Fragment implements EditEmailDialog.DialogListener, EditUsernameDialog.DialogListener {

    private FragmentConfigBinding binding;
    private ConfigOptionChosen optionChosen;
    private EditUsernameDialog newUsernameDialog;
    private EditEmailDialog newEmailDialog;
    private DatabaseReference databaseUserReference;
    private String testUserID = "U-0001", testUsername = "Juan", testEmail = "juan@gmail.com";

    private static final String FMR_FIREBASE_DATABASE_URL =
            "https://findmyroommate-86cbe-default-rtdb.europe-west1.firebasedatabase.app/";
    private static final int NEW_USERNAME_OPTION_INDEX = 0;
    private static final int NEW_EMAIL_OPTION_INDEX = 1;
    private static final int NEW_PASSWORD_OPTION_INDEX = 2;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ConfigViewModel configViewModel =
                new ViewModelProvider(this).get(ConfigViewModel.class);

        binding = FragmentConfigBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textConfig;
        configViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        List<ConfigListItem> items = new ArrayList<>();
        items.add(new ConfigListItem("Nombre de usuario", "Default name..."));
        items.add(new ConfigListItem("E-mail", "Default email"));
        items.add(new ConfigListItem("Contraseña", "Default blabla"));

        ListView listView = binding.listaOpcionesConfiguracion; // Make sure this matches your ListView ID

        ConfigListAdapter adapter = new ConfigListAdapter(getContext(), items);
        listView.setAdapter(adapter);
        this.optionChosen = ConfigOptionChosen.NO_OPTION;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Obtains the database reference object used to modify and retrieve data from such database.
                FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(FMR_FIREBASE_DATABASE_URL);
                ConfigFragment.this.databaseUserReference = databaseInstance.getReference("users")
                        .child(ConfigFragment.this.testUserID);

                switch(position) {  // Decides what action takes place, depending on the item clicked.
                    case NEW_USERNAME_OPTION_INDEX:
                        Log.i("Configuration", "Option selected: edit username");
                        ConfigFragment.this.newUsernameDialog = EditUsernameDialog.newInstance(ConfigFragment.this.testUsername);
                        ConfigFragment.this.newUsernameDialog.show(getChildFragmentManager(), "EditUsernameDialogFragment");
                        ConfigFragment.this.optionChosen = ConfigOptionChosen.NEW_USERNAME_OPTION;
                        break;

                    case NEW_EMAIL_OPTION_INDEX:
                        Log.i("Configuration", "Option selected: edit email");
                        ConfigFragment.this.newEmailDialog = EditEmailDialog.newInstance(ConfigFragment.this.testEmail);
                        ConfigFragment.this.newEmailDialog.show(getChildFragmentManager(), "EditEmailDialogFragment");
                        ConfigFragment.this.optionChosen = ConfigOptionChosen.NEW_EMAIL_OPTION;
                        break;

                    case NEW_PASSWORD_OPTION_INDEX:
                        Log.i("Configuration", "Option selected: change password");
                        break;

                }
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPositiveButtonClick() {
        switch(this.optionChosen) {
            case NO_OPTION:
                break;

            case NEW_USERNAME_OPTION:
                String newUsername = this.newUsernameDialog.getNewUsername();
                DatabaseReference usernameReference = databaseUserReference.child("username");
                usernameReference.setValue(newUsername);

                if (!ConfigFragment.this.testUsername.equals(newUsername)) {
                    usernameReference.setValue(newUsername).addOnSuccessListener(aVoid -> {
                                // If the username gets succesfully updated in the database.
                                Log.d("FirebaseDatabase", "Username updated correctly.");
                            })
                            .addOnFailureListener(databaseError -> {
                                // If there was an error while trying to update the username.
                                Log.e("FirebaseDatabase", "Error updating username: "
                                        + databaseError.getMessage());
                            });
                }
                break;

            case NEW_EMAIL_OPTION:
                String newEmail = this.newEmailDialog.getNewEmail();
                DatabaseReference emailReference = databaseUserReference.child("email");
                emailReference.setValue(newEmail);

                if (!ConfigFragment.this.testEmail.equals(newEmail)) {
                    emailReference.setValue(newEmail).addOnSuccessListener(aVoid -> {
                                // If the email gets succesfully updated in the database.
                                Log.d("FirebaseDatabase", "Email updated correctly.");
                            })
                            .addOnFailureListener(databaseError -> {
                                // If there was an error while trying to update the email.
                                Log.e("FirebaseDatabase", "Error updating email: "
                                        + databaseError.getMessage());
                            });
                }
                break;

            case NEW_PASSWORD_OPTION:
                break;

            default:
                break;

        }

    }

    @Override
    public void onNegativeButtonClick() {}


}