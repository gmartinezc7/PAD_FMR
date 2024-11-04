package es.ucm.fdi.v3findmyroommate.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.v3findmyroommate.VivCreateActivity;
import es.ucm.fdi.v3findmyroommate.databinding.FragmentHomeBinding;
import es.ucm.fdi.v3findmyroommate.Vivienda;
import es.ucm.fdi.v3findmyroommate.ViviendaAdapter;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private List<Vivienda> listViv;
    private ViviendaAdapter adapter;
    private ActivityResultLauncher<Intent> createViviendaLauncher;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listViv =  new ArrayList<>();
        adapter = new ViviendaAdapter(listViv);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        createViviendaLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    if (data.hasExtra("newViv")) {
                        Vivienda newViv = (Vivienda) data.getSerializableExtra("newViv");
                        if (newViv != null) {
                            listViv.add(newViv);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

        // botón psra lanzar el VivCreateActivity
        binding.btnAddVivienda.setOnClickListener(v -> {
            // AÑADIR VIVIENDA
            Intent intent = new Intent(getActivity(), VivCreateActivity.class);
            createViviendaLauncher.launch(intent);
        });

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}