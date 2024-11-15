package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.ArrayList;
import es.ucm.fdi.v3findmyroommate.R;


public class MisViviendasFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button btnCrearAnuncio;
    private MisViviendasViewModel misViviendasViewModel;
    private AnunciosAdapter adapter;

    private int posicionCambioActual;
    private ActivityResultLauncher<Intent> crearAnuncioLauncher;
    private ActivityResultLauncher<Intent> verAnuncioLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mis_viviendas, container, false);


        misViviendasViewModel = new ViewModelProvider(this).get(MisViviendasViewModel.class);

        // INICIO EL RECYCLERVIEW, DEFINIDO EN fragment_mis_viviendas, LO QUE ME VA A PERMITIR MANTENER MIS ELEMENTOS EN ORDEN VERTICAL
        recyclerView = view.findViewById(R.id.recycler_view_anuncios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // INICIAMOS EL ADAPTER Y LE ASIGNAMOS EL ADAPTADOR PARA MOSTRAR LOS DATOS, CUANDO SE VAYA A ACTUALIZAR,
        //USARÁ A ESTE ADPATADOR PARA MOSTRAR LOS DATOS
        adapter = new AnunciosAdapter(misViviendasViewModel, this);
        recyclerView.setAdapter(adapter);

        // CON ESTO PODREMOS OBSERVAR LOS DATOS ACTUALIZADOS DE LOS ANUNCIOS A TIEMPO REAL,
        // DE MANERA QUE SI SE PRODUCE ALGUN CAMBIO EN LA LISTA DE ANUNCIOS, SE NOTIFICARÁ
        //AUTOMATICAMENTE AQUÍ Y SE REALIZARÁ EL CAMBIO EN EL ADAPTADOR
        misViviendasViewModel.getAnuncios().observe(getViewLifecycleOwner(), anuncios -> {
            adapter.setAnuncios(anuncios);
        });


        // Inicializar el ActivityResultLauncher
        crearAnuncioLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        misViviendasViewModel.addAnuncio(data);
                    }
                }
        );


        // Inicializar el ActivityResultLauncher
        verAnuncioLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        misViviendasViewModel.actualizarAnuncio(posicionCambioActual, data);
                    }
                }
        );


        // Botón de crear anuncio
        btnCrearAnuncio = view.findViewById(R.id.btn_crear_anuncio);
        btnCrearAnuncio.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CrearAnuncioActivity.class);
           crearAnuncioLauncher.launch(intent); // Usar el nuevo launcher
        });



        return view;
    }



    public void lanzarVerAnuncio(int position){

        this.posicionCambioActual = position;


        Anuncio anuncio = misViviendasViewModel.getAnuncio(posicionCambioActual);

        Intent intent = new Intent(getContext(), AnuncioDetalleActivity.class);

        intent.putExtra("titulo", anuncio.getTitulo());
        intent.putExtra("ubicacion", anuncio.getUbicacion());
        intent.putExtra("metros", anuncio.getMetros());
        intent.putExtra("precio", anuncio.getPrecio());
        intent.putExtra("descripcion", anuncio.getDescripcion());
        intent.putParcelableArrayListExtra("imagenesUri", new ArrayList<>(anuncio.getImagenesUri()));


        verAnuncioLauncher.launch(intent);

    }
}
