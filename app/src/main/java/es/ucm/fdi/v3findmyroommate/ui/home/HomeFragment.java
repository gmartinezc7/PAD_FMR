package es.ucm.fdi.v3findmyroommate.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private Button buttonApplyFilters;


    // variables para poder visualizar o no los filtros
    private View filterscasa, filtershabitacion;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        recyclerView = root.findViewById(R.id.recyclerViewViviendas);
        Button openFilters = root.findViewById(R.id.buttonOpenFilters);

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


        openFilters.setOnClickListener(v -> {
            FiltersFragment dialog = new FiltersFragment();
            dialog.setListenerFiltrosAplicados(((categoria, tipocasa, numHabs, numBanos, orientacion, genero, numComps, tipoBano) -> {
                // Llamada a viviendaViewModel para aplicar los filtros que se hanc reado
                homeViewModel.applyFiltersViewModel(categoria,tipocasa,numHabs, numBanos, orientacion, genero, numComps, tipoBano);
            }));
            dialog.show(getParentFragmentManager(),"Filters");
        });
        //buttonApplyFilters.setOnClickListener(v -> applyFilters());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void applyFilters(){
        // APLICAR LOS FILTROS
        String fcategoria = sCategoria.getSelectedItem().toString();
        System.out.println("CATEGORIA FILTRO: " + fcategoria);

        String ftipocasa = null;
        if (filterscasa.getVisibility() == View.VISIBLE){
            sTipoCasa.getSelectedItem().toString();
        }

        String fnumhabitaciones = null;
        if (filterscasa.getVisibility() == View.VISIBLE){
            sNumHabs.getSelectedItem().toString();
        }

        String fnumbanos = null;
        if (filterscasa.getVisibility() == View.VISIBLE){
            sNumBanos.getSelectedItem().toString();
        }

        String fnumComps = null;
        if (filterscasa.getVisibility() == View.VISIBLE){
            sNumComps.getSelectedItem().toString();
        }

        String fgenero = null;
        if (filterscasa.getVisibility() == View.VISIBLE){
            sGenero.getSelectedItem().toString();
        }

        String forientacion = null;
        if (filterscasa.getVisibility() == View.VISIBLE){
            sOrientacion.getSelectedItem().toString();
        }

        String fTipoBano = null;
        if (filterscasa.getVisibility() == View.VISIBLE){
            sTipoBano.getSelectedItem().toString();
        }

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