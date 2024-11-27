package es.ucm.fdi.v3findmyroommate.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;



import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ViviendaAdapter adapter;
    private HomeViewModel homeViewModel;
    private Spinner sCategoria, sTipoCasa, sNumHabs, sNumBanos, sOrientacion, sNumComps, sGenero, sTipoBano;
    private ChipGroup chipGroupComps, chipGroupGenero;
    private Button buttonApplyFilters;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewViviendas);
        sCategoria = root.findViewById(R.id.spinnerCategorias);
        sTipoCasa = root.findViewById(R.id.spinnerTipoCasas);
        sNumHabs = root.findViewById(R.id.spinnerNumHabs);
        sNumBanos = root.findViewById(R.id.spinnerNumBanos);
        sOrientacion = root.findViewById(R.id.spinnerOrientacion);
        sNumComps = root.findViewById(R.id.spinnerNumComps);
        sGenero = root.findViewById(R.id.spinnerGenero);
        sTipoBano = root.findViewById(R.id.spinnerTipoBano);

        buttonApplyFilters = root.findViewById(R.id.buttonApplyFilters);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        List<Vivienda> viviendas = new ArrayList<>();
        adapter = new ViviendaAdapter(viviendas);
        recyclerView.setAdapter(adapter);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getViviendas().observe(getViewLifecycleOwner(), new Observer<List<Vivienda>>() {
            @Override
            public void onChanged(List<Vivienda> lista) {
                viviendas.clear();
                viviendas.addAll(lista);
                adapter.notifyDataSetChanged();
            }
        });

        buttonApplyFilters.setOnClickListener(v -> applyFilters());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void applyFilters(){
        // APLICAR LOS FILTROS
        String fcategoria = sCategoria.getSelectedItem().toString();
        String ftipocasa = sTipoCasa.getSelectedItem().toString();
        String fnumhabitaciones = sNumHabs.getSelectedItem().toString();
        String fnumbanos = sNumBanos.getSelectedItem().toString();
        String fnumComps = sNumComps.getSelectedItem().toString();
        String fgenero = sGenero.getSelectedItem().toString();
        String forientacion = sOrientacion.getSelectedItem().toString();
        String fTipoBano = sTipoBano.getSelectedItem().toString();

        // Llamada a la construcción del ViewModel
        /*System.out.println("FILTROS");
        System.out.println("FCATEGORIA: " + fcategoria);
        System.out.println("FTIPOCASA: " + ftipocasa);*/
        //System.out.println("FCOMPS: " + fcomps);
        //System.out.println("FGENERO: " + fgenero);
        homeViewModel.applyFiltersViewModel(fcategoria, ftipocasa, fnumhabitaciones, fnumbanos, fnumComps, fgenero, forientacion, fTipoBano);
    }

    private String getSelectedChipText(ChipGroup cg) {
        int selected = cg.getCheckedChipId();
        if (selected != View.NO_ID){
            Chip chip = cg.findViewById(selected);
            return chip.getText().toString();
        }
        return null; // En caso de que no se haya seleccionado ningún chip
    }
}