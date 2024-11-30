package es.ucm.fdi.v3findmyroommate.ui.home;

import android.app.Application;
import android.content.Intent;
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
import es.ucm.fdi.v3findmyroommate.ui.viviendas.Anuncio;
import es.ucm.fdi.v3findmyroommate.ui.viviendas.AnuncioDetalleActivity;

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
        adapter = new ViviendaAdapter(getActivity(), viviendas, this);
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
            FiltersFragment dialog = new FiltersFragment(this.getContext());
            dialog.setListenerFiltrosAplicados(((categoria, tipocasa, numHabs, numBanos, numComps, genero, orientacion, tipoBano) -> {
                // Llamada a viviendaViewModel para aplicar los filtros que se hanc reado
                homeViewModel.applyFiltersViewModel(categoria,tipocasa,numHabs, numBanos, numComps, genero, orientacion, tipoBano);
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





    //SAM-------------------------------------------------------------------------------------------------------

    public void lanzarVerAnuncio(Vivienda vivienda){


        Intent intent = new Intent(getContext(), ViviendaDetalleActivity.class);

        intent.putExtra(this.getString(R.string.key_id), vivienda.getId());
        intent.putExtra(this.getString(R.string.key_titulo), vivienda.getTitle());
        intent.putExtra(this.getString(R.string.key_ubicacion), vivienda.getLocation());
        intent.putExtra(this.getString(R.string.key_metros), vivienda.getMetr());
        intent.putExtra(this.getString(R.string.key_precio), vivienda.getPrice());
        intent.putExtra(this.getString(R.string.key_descripcion), vivienda.getDescription());
        intent.putParcelableArrayListExtra(this.getString(R.string.key_imagenes_uri), new ArrayList<>(vivienda.getImagenesUri()));


        //TAGS
        String categoria = vivienda.getCategoria();
        intent.putExtra(this.getString(R.string.key_categoria), categoria);


        if (categoria.equalsIgnoreCase(this.getString(R.string.category_casa))) {

            intent.putExtra(this.getString(R.string.key_tipo_casa), vivienda.getTipoCasa());
            intent.putExtra(this.getString(R.string.key_habitaciones), vivienda.getHabitaciones());
            intent.putExtra(this.getString(R.string.key_banos), vivienda.getBanos());
            intent.putExtra(this.getString(R.string.key_exterior_interior), vivienda.getExteriorInterior());
        } else if (categoria.equalsIgnoreCase(this.getString(R.string.category_habitacion))) {

            intent.putExtra(this.getString(R.string.key_companeros), vivienda.getCompaneros());
            intent.putExtra(this.getString(R.string.key_genero), vivienda.getGenero());
            intent.putExtra(this.getString(R.string.key_exterior_interior), vivienda.getExteriorInterior());
            intent.putExtra(this.getString(R.string.key_tipo_bano), vivienda.getTipoBano());
        }

        startActivity(intent);

    }


}