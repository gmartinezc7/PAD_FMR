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

                // Con switch evalúas la posición del item seleccionado y dependiendo
                // de la posicion seleccionada lanzas la actividad.
                switch(position) {

                    case 0:
                        Log.d("Firebase", "Option 0 selected");
                        System.out.println("DSOFSIDOFHOIASDHFOISHDFIOHDSIOFSD");
                        EditUsernameDialog newDialog = new EditUsernameDialog();
                        newDialog.show(getParentFragmentManager(), "EditUsernameDialogFragment");
                        break;
                    case 1:
                        // Lanzas la actividad 2
                        Log.d("Firebase", "Option 1 selected");
                        System.out.println("DSOFSIDOFHOIASDHFOISHDFIOHDSIOFSD");
                        break;
                    case 2:
                        // Lanzas la actividad 3
                        Log.d("Firebase", "Option 2 selected");
                        System.out.println("DSOFSIDOFHOIASDHFOISHDFIOHDSIOFSD");
                        break;
                }
            }
        });

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://findmyroommate-86cbe-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();


        // Upload the user to the "users" node in the database
        myRef.child("users").push().setValue("NnewF")
                .addOnCompleteListener(task -> {
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