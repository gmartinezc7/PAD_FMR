package es.ucm.fdi.v3findmyroommate.ui.config;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}