package es.ucm.fdi.v3findmyroommate.ui.config;

import android.os.Bundle;
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

public class ConfigFragment extends Fragment {

    private FragmentConfigBinding binding;

    private static final int NEW_USERNAME_OPTION = 0;
    private static final int NEW_EMAIL_OPTION = 1;
    private static final int NEW_PASSWORD_OPTION = 2;


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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch(position) {  // Decides what action takes place, depending on the item clicked.
                    case NEW_USERNAME_OPTION:
                        Log.i("Configuration", "Set new username");
                        EditUsernameDialog newUsernameDialog = new EditUsernameDialog();
                        newUsernameDialog.show(getParentFragmentManager(), "EditUsernameDialogFragment");
                        break;
                    case NEW_EMAIL_OPTION:
                        Log.i("Configuration", "Set new email");
                        EditEmailDialog newEmailDialog = new EditEmailDialog();
                        newEmailDialog.show(getParentFragmentManager(), "EditEmailDialogFragment");
                        break;
                    case NEW_PASSWORD_OPTION:
                        Log.i("Configuration", "Set new password");
                        break;
                }
            }
        });

        // Obtains the database reference object used to modify and retrieve data from such database.
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://findmyroommate-86cbe-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();


        // Uploads the user "Juan" to the "users" node in the database
        ConfigListItem it = new ConfigListItem("NN", "mm");
        myRef.child("Nuevo item").push().setValue(it);
        myRef.child("users").push().setValue("Juan").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Data uploaded successfully
                Log.d("Firebase", "User uploaded successfully.");
            } else {
                // Data upload failed
                Log.e("Firebase", "User upload failed.", task.getException());
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}