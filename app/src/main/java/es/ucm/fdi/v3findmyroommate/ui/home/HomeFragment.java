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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import es.ucm.fdi.v3findmyroommate.LocaleUtils;
import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.TranslationUtils;
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
        //Button openFilters = root.findViewById(R.id.buttonOpenFilters);

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

        DrawerLayout drawerLayout = root.findViewById(R.id.drawerLayoutFilters); // ID del DrawerLayout
        Button openFiltersButton = root.findViewById(R.id.buttonOpenFilters);

        openFiltersButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START); // Cierra el menú si está abierto
            } else {
                drawerLayout.openDrawer(GravityCompat.START); // Abre el menú lateral
            }
        });

        View filtersMenu = root.findViewById(R.id.filtersDialog); // Este debe ser el layout que contiene los filtros en el drawer
        Spinner sCategoria = filtersMenu.findViewById(R.id.spinnerCategorias);
        Spinner sTipoCasa = filtersMenu.findViewById(R.id.spinnerTipoCasas);
        Spinner sNumHabs = filtersMenu.findViewById(R.id.spinnerNumHabs);
        Spinner sNumBanos = filtersMenu.findViewById(R.id.spinnerNumBanos);
        Spinner sOrientacion = filtersMenu.findViewById(R.id.spinnerOrientacion);
        Spinner sGenero = filtersMenu.findViewById(R.id.spinnerGenero);
        Spinner sNumComps = filtersMenu.findViewById(R.id.spinnerNumComps);
        Spinner sTipoBano = filtersMenu.findViewById(R.id.spinnerTipoBano);

        View filtersCasa = filtersMenu.findViewById(R.id.filtrosCasa);
        View filtersHabitacion = filtersMenu.findViewById(R.id.filtrosHabitacion);

        sCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoriaSel = parent.getItemAtPosition(position).toString();
                String tipoCasaSel = getString(R.string.house_property_type_label);
                if (categoriaSel.equals(getString(R.string.house_property_type_label))) {
                    filtersCasa.setVisibility(View.VISIBLE);
                    filtersHabitacion.setVisibility(View.GONE);
                } else if (categoriaSel.equals(getString(R.string.room_property_type_label))) {
                    filtersCasa.setVisibility(View.GONE);
                    filtersHabitacion.setVisibility(View.VISIBLE);
                } else {
                    filtersCasa.setVisibility(View.GONE);
                    filtersHabitacion.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Button applyFiltersButton = filtersMenu.findViewById(R.id.buttonApplyFilters);
        applyFiltersButton.setOnClickListener(v -> {
            String fCategoria = sCategoria.getSelectedItem() != null ? sCategoria.getSelectedItem().toString() : "";
            String fTipoCasa = sTipoCasa.getSelectedItem() != null ? sTipoCasa.getSelectedItem().toString() : "";
            String fNumHabs = sNumHabs.getSelectedItem() != null ? sNumHabs.getSelectedItem().toString() : "";
            String fNumBanos = sNumBanos.getSelectedItem() != null ? sNumBanos.getSelectedItem().toString() : "";
            String fOrientacion = sOrientacion.getSelectedItem() != null ? sOrientacion.getSelectedItem().toString() : "";
            String fGenero = sGenero.getSelectedItem() != null ? sGenero.getSelectedItem().toString() : "";
            String fNumComps = sNumComps.getSelectedItem() != null ? sNumComps.getSelectedItem().toString() : "";
            String fTipoBano = sTipoBano.getSelectedItem() != null ? sTipoBano.getSelectedItem().toString() : "";

            // Lógica para aplicar filtros
            // Llama a tu método o listener con los valores seleccionados
            applyFilters(fCategoria, fTipoCasa, fNumHabs, fNumBanos, fOrientacion, fGenero, fNumComps, fTipoBano);

            drawerLayout.closeDrawer(GravityCompat.START); // Cierra el menú lateral tras aplicar los filtros
        });

        Button cancelFiltersButton = filtersMenu.findViewById(R.id.buttonCancelFilters);
        cancelFiltersButton.setOnClickListener(v -> {
            // Opcional: Restaura valores por defecto si es necesario
            drawerLayout.closeDrawer(GravityCompat.START); // Cierra el menú lateral
        });

        // Método para manejar la aplicación de filtros


        /*
        openFilters.setOnClickListener(v -> {
            FiltersFragment dialog = new FiltersFragment(this.getContext());
            dialog.setListenerFiltrosAplicados(((categoria, tipocasa, numHabs, numBanos, numComps, genero, orientacion, tipoBano) -> {
                // Llamada a viviendaViewModel para aplicar los filtros que se hanc reado
                homeViewModel.applyFiltersViewModel(categoria,tipocasa,numHabs, numBanos, numComps, genero, orientacion, tipoBano);
            }));
            dialog.show(getParentFragmentManager(),"Filters");
        });
        */

        //buttonApplyFilters.setOnClickListener(v -> applyFilters());

        return root;
    }


    private void applyFilters(String categoria, String tipoCasa, String numHabs, String numBanos,
                              String orientacion, String genero, String numComps, String tipoBano) {
        categoria = translateIfNeeded(categoria);
        tipoCasa = translateIfNeeded(tipoCasa);
        numHabs = translateIfNeeded(numHabs);
        numBanos = translateIfNeeded(numBanos);
        orientacion = translateIfNeeded(orientacion);
        genero = translateIfNeeded(genero);
        numComps = translateIfNeeded(numComps);
        tipoBano = translateIfNeeded(tipoBano);
        homeViewModel.applyFiltersViewModel(categoria,tipoCasa,numHabs, numBanos, numComps, genero, orientacion, tipoBano);
        System.out.println("Filtros aplicados: " + categoria + ", " + tipoCasa + ", " + numHabs + ", " + numBanos);
    }
    private String translateIfNeeded(String value) {
        Locale currentLocale = Locale.getDefault();
        boolean isSpanish = currentLocale.getLanguage().equals("es");

        if (!isSpanish && value != null) {
            return TranslationUtils.translateToBaseLanguage(value);
        }
        return value;
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