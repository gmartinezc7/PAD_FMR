package es.ucm.fdi.v3findmyroommate.ui.favoritos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.ucm.fdi.v3findmyroommate.databinding.FragmentMisFavoritosBinding;
import es.ucm.fdi.v3findmyroommate.ui.home.Vivienda;
import es.ucm.fdi.v3findmyroommate.ui.home.ViviendaAdapter;
import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.ui.home.ViviendaDetalleActivity;

public class MisFavoritosFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoritoAdapter adapter;
    private MisFavoritosViewModel misfavsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mis_favoritos, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewFavoritos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Vivienda> viviendasfavs = new ArrayList<>();
        adapter = new FavoritoAdapter(viviendasfavs, this);
        recyclerView.setAdapter(adapter);

        misfavsViewModel = new ViewModelProvider(this).get(MisFavoritosViewModel.class);
        misfavsViewModel.getViviendas().observe(getViewLifecycleOwner(), new Observer<List<Vivienda>>() {
            @Override
            public void onChanged(List<Vivienda> lista) {
                viviendasfavs.clear();
                viviendasfavs.addAll(lista);
                adapter.notifyDataSetChanged();
            }

        });

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
        intent.putStringArrayListExtra(this.getString(R.string.key_imagenes_uri), new ArrayList<>(vivienda.getImagenesUri()));


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