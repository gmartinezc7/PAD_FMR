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


//ESTE FRAGMENT SIRVE COMO "CONECTOR" DE TODA LA PARTE DE "MISVIVIENDAS".
/*
SE VA A ENCARGAR DE MANEJAR DIRECTAMENTE PARTE DE LA LISTA DE ANUNCIOS CONTENIDA EN
EL VIEW MODEL "MisViviendasViewModel", DE CREAR EL ADAPTER (RECYCLERVIEW) PRINCIPAL, EL CUAL
CONTIENE TODA LA LISTA DE "MIS ANUNCIOS".

ES MUY IMPORTANTE EL PAPEL QUE TIENE ESTE FRAGMENT A LA HORA DE MANEJAR TODOS LOS DATOS,
TODAS LAS MODIFICACIONES REALIZADAS SOBRE ALGÚN ANUNCIO, LAS CUALES AFECTAN DIRECTAMENTE A LA LISTA DE ANUNCIOS,
DEBEN PASAR POR AQUÍ, ES POR ESO QUE RECIBE VARIOS RESULTADOS DE ACTIVIDADES QUE MANEJAN CAMBIOS EN LOS ANUNCIOS
(crearAnuncioLauncher Y verAnuncioLauncher), verAnuncioLauncher SE USA YA QUE DEVUELVE LOS CAMBIOS DE EDITAR UN
ANUNCIO, ACTIVIDAD QUE SE LANZA DESDE verAnuncio (AnuncioDetalleActivity).
 */
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


        //INICIAMOS LOS RESULT LAUNCHERS
        crearAnuncioLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        misViviendasViewModel.addAnuncio(data);
                    }
                }
        );

        verAnuncioLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        misViviendasViewModel.actualizarAnuncio(posicionCambioActual, data);
                    }
                }
        );


        // TAMBIÉN CONTIENE EL BOTÓN PRINCIPAL DE CREAR ANUNCIO
        btnCrearAnuncio = view.findViewById(R.id.btn_crear_anuncio);
        btnCrearAnuncio.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CrearAnuncioActivity.class);
           crearAnuncioLauncher.launch(intent); // Usar el nuevo launcher
        });



        return view;
    }


   //FUNCIÓN LLAMADA DESDE EL ADAPTER, PARA PERMITIRNOS LANZAR verAnuncioLauncher DESDE AQUÍ Y ASÍ
    //PODER "RECOGER" LOS RESULTADOS

    /*
    IMPORTANTE, EN ESTA FUNCIÓN OBTENDREMOS EL ANUNCIO DE LA LISTA DEL ViewModel
    Y "ENVIAREMOS" LOS DATOS MEDIANTE EL LAUNCH EN EL INTENT, EXTRAYÉNDOLOS ANTES DEL ANUNCIO
     */
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


        //TAGS
        String categoria = anuncio.getCategoria();
        intent.putExtra("categoria", categoria);


        if (categoria.equalsIgnoreCase("Casa")) {

            intent.putExtra("tipoCasa", anuncio.getTipoCasa());
            intent.putExtra("habitaciones", anuncio.getHabitaciones());
            intent.putExtra("banos", anuncio.getBanos());
            intent.putExtra("exteriorInterior", anuncio.getExteriorInterior());
        } else if (categoria.equalsIgnoreCase("Habitación")) {

            intent.putExtra("companeros", anuncio.getCompaneros());
            intent.putExtra("genero", anuncio.getGenero());
            intent.putExtra("exteriorInterior", anuncio.getExteriorInterior());
            intent.putExtra("tipoBano", anuncio.getTipoBano());
        }

        verAnuncioLauncher.launch(intent);

    }
}
