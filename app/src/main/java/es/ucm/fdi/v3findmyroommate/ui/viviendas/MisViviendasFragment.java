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
import androidx.recyclerview.widget.AsyncListUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.ui.config.ConfigPreferencesModel;


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

    // Define the callback interface
    public interface DataCallback<T> {
        void onDataLoaded(T data);
    }

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

        loadUserAdds(anuncios -> {
            adapter.setAnuncios(anuncios);
            adapter.notifyDataSetChanged(); // Refresh the adapter
        });

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


    // Función que
    private void loadUserAdds(DataCallback<List<Anuncio>> callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // Get current user

        if (user == null) {
            callback.onDataLoaded(new ArrayList<>()); // Return an empty list if the user is null
            return;
        }

        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(
                requireContext().getString(R.string.database_url));

        DatabaseReference databaseUserReference = databaseInstance.getReference("users").child(user.getUid());

        databaseUserReference.get().addOnCompleteListener(firstTask -> {
            if (firstTask.isSuccessful()) {
                DataSnapshot firstSnapshot = firstTask.getResult();

                List<String> userAddsIDListInDB = firstSnapshot.child(
                                getContext().getString(R.string.user_adds_list_db_label))
                        .getValue(new GenericTypeIndicator<List<String>>() {});

                if (userAddsIDListInDB != null) {
                    DatabaseReference databaseAddReference = databaseInstance.getReference("adds");

                    List<Anuncio> newAddList = new ArrayList<>();
                    for (String addID : userAddsIDListInDB) {
                        databaseAddReference.child(addID).get().addOnCompleteListener(secondTask -> {
                            if (secondTask.isSuccessful()) {
                                DataSnapshot secondSnapshot = secondTask.getResult();

                                String titulo = secondSnapshot.child(getActivity().getApplication()
                                        .getString(R.string.add_title_db_label)).getValue(String.class);
                                String ubicacion = secondSnapshot.child(getActivity().getApplication()
                                        .getString(R.string.add_location_db_label)).getValue(String.class);
                                String metros = secondSnapshot.child(getActivity().getApplication()
                                        .getString(R.string.add_square_meters_db_label)).getValue(String.class);
                                String precio = secondSnapshot.child(getActivity().getApplication()
                                        .getString(R.string.add_price_db_label)).getValue(String.class);
                                String descripcion = secondSnapshot.child(getActivity().getApplication()
                                        .getString(R.string.add_description_db_label)).getValue(String.class);
                                String categoria = secondSnapshot.child(getActivity().getApplication()
                                        .getString(R.string.property_type_db_label)).getValue(String.class);

                                // Obtiene la lista de imágenes del anuncio de la BD en formato String.
                                List<String> listaImagenesFormatoString = secondSnapshot.child(getActivity().getApplication()
                                        .getString(R.string.add_uri_list_db_label)).getValue
                                        (new GenericTypeIndicator<List<String>>() {});

                                // Convierte los valores de la lista recibida a formato Uri.
                                List<Uri> listaImagenesFormatoUri = convierteListaStringsAListaUris(listaImagenesFormatoString);

                                // Introduce cada uno de los campos en un intent.
                                Intent currentAddIntent = new Intent();
                                currentAddIntent.putExtra("titulo", titulo);
                                currentAddIntent.putExtra("ubicacion", ubicacion);
                                currentAddIntent.putExtra("metros", metros);
                                currentAddIntent.putExtra("precio", precio);
                                currentAddIntent.putExtra("descripcion", descripcion);
                                currentAddIntent.putExtra("categoria", categoria);
                                currentAddIntent.putParcelableArrayListExtra("imagenesUri", new ArrayList<>(listaImagenesFormatoUri));

                                if (categoria.equals(getContext().getApplicationContext().getString(
                                        R.string.house_property_type_label))) {  // Si el anuncio es de una casa.

                                    String tipo_casa = secondSnapshot.child(getActivity().getApplication()
                                            .getString(R.string.num_bathrooms_db_label)).getValue(String.class);
                                    String num_habitaciones = secondSnapshot.child(getActivity().getApplication()
                                            .getString(R.string.num_rooms_db_label)).getValue(String.class);
                                    String num_banos = secondSnapshot.child(getActivity().getApplication()
                                            .getString(R.string.add_house_type_db_label)).getValue(String.class);
                                    String orientacion = secondSnapshot.child(getActivity().getApplication()
                                            .getString(R.string.orientation_db_label)).getValue(String.class);

                                    // Guardamos los datos específicos, propios de una casa.
                                    currentAddIntent.putExtra("tipoCasa", tipo_casa);
                                    currentAddIntent.putExtra("habitaciones", num_habitaciones);
                                    currentAddIntent.putExtra("banos", num_banos);
                                    currentAddIntent.putExtra("exteriorInterior", orientacion);
                                }

                                // Añade el anuncio a la lista del modelo.
                                misViviendasViewModel.addAnuncio(currentAddIntent);

                                // Crea un nuevo anuncio a partir del intent y lo guarda en la lista.
                                Anuncio newAdd = new Anuncio(currentAddIntent);
                                newAddList.add(newAdd);

                                // Notify the callback when all ads are loaded
                                if (newAddList.size() == userAddsIDListInDB.size()) {
                                    callback.onDataLoaded(newAddList);
                                }
                            }
                        });
                    }
                }
                else {
                    callback.onDataLoaded(new ArrayList<>()); // No ads found
                }
            }
            else {
                callback.onDataLoaded(new ArrayList<>()); // Task failed
            }
        });
    }


    // Función auxiliar que convierte una lista de Uris a otra de Strings
    public static List<String> convierteListaUrisAListaStrings(List<Uri> urisList) {
        List<String> stringsList = new ArrayList<>();
        for (Uri uri : urisList) {
            stringsList.add(uri.toString());
        }
        return stringsList;
    }


    // Función auxiliar que convierte una lista de Strings a otra de Uris.
    public static List<Uri> convierteListaStringsAListaUris(List<String> stringsList) {
        List<Uri> urisList = new ArrayList<>();
        for (String str : stringsList) {
            Uri newUri = Uri.parse(str);
            urisList.add(newUri);
        }
        return urisList;
    }


}
