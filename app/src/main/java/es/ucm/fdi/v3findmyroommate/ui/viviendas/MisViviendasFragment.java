package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.app.Activity;
import android.app.Application;
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

        // Cargar anuncios solo si es la primera vez
        if (!misViviendasViewModel.isAnunciosCargados()) {
        loadUserAdds(anuncios -> {
            misViviendasViewModel.setAnunciosCargados(true);
        });
        }

        // CON ESTO PODREMOS OBSERVAR LOS DATOS ACTUALIZADOS DE LOS ANUNCIOS A TIEMPO REAL,
        // DE MANERA QUE SI SE PRODUCE ALGUN CAMBIO EN LA LISTA DE ANUNCIOS, SE NOTIFICARÁ
        //AUTOMATICAMENTE AQUÍ Y SE REALIZARÁ EL CAMBIO EN EL ADAPTADOR
        misViviendasViewModel.getAnuncios().observe(getViewLifecycleOwner(), anuncios -> {
            adapter.setAnuncios(anuncios);
            adapter.notifyDataSetChanged();
        });


        //INICIAMOS LOS RESULT LAUNCHERS
        crearAnuncioLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String id = data.getStringExtra("id");
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
            //Intent intent = new Intent(getContext(), CrearAnuncioActivity.class);
            //crearAnuncioLauncher.launch(intent); // Usar el nuevo launcher
            CrearAnuncioActivity.startForResult(crearAnuncioLauncher, getContext());
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

        intent.putExtra("id", anuncio.getId());
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
                        if (addID != null) {
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
                                    currentAddIntent.putExtra("id", addID);
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

                                    else if (categoria.equals(getContext().getApplicationContext().getString(
                                            R.string.room_property_type_label))) {  // Si el anuncio es de una habitación.

                                        String max_companeros = secondSnapshot.child(getActivity().getApplication()
                                                .getString(R.string.max_num_roommates_db_label)).getValue(String.class);
                                        String genero_companeros = secondSnapshot.child(getActivity().getApplication()
                                                .getString(R.string.roommate_gender_db_label)).getValue(String.class);
                                        String orientacion = secondSnapshot.child(getActivity().getApplication()
                                                .getString(R.string.orientation_db_label)).getValue(String.class);
                                        String tipo_bano = secondSnapshot.child(getActivity().getApplication()
                                                .getString(R.string.bathroom_type_db_label)).getValue(String.class);

                                        // Guardamos los datos específicos, propios de una casa.
                                        currentAddIntent.putExtra("companeros", max_companeros);
                                        currentAddIntent.putExtra("genero", genero_companeros);
                                        currentAddIntent.putExtra("exteriorInterior", orientacion);
                                        currentAddIntent.putExtra("tipoBano", tipo_bano);
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


    // Función que guarda un anuncio de nueva creación en la base de datos.
    public static void guardarAnuncioEnBD(Anuncio nuevoAnuncio, Application application) {



        // Guarda el ID del anuncio en la lista de IDs de anuncios de este usuario.
        MisViviendasFragment.guardarAnuncioListaAnunciosUsuario(nuevoAnuncio, application);

        // Obtiene una instancia de la BD.
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(application.
                getApplicationContext().getString(R.string.database_url));

        // Obtiene la referencia a la BD de anuncios.
        DatabaseReference databaseAddReference = databaseInstance.getReference("adds")
                .child(nuevoAnuncio.getId());

        databaseAddReference.child(application.getString(R.string.add_title_db_label))
                .setValue(nuevoAnuncio.getTitulo());
        databaseAddReference.child(application.getString((R.string.add_location_db_label)))
                .setValue(nuevoAnuncio.getUbicacion());
        databaseAddReference.child(application.getString((R.string.add_square_meters_db_label)))
                .setValue(nuevoAnuncio.getMetros());
        databaseAddReference.child(application.getString((R.string.add_price_db_label)))
                .setValue(nuevoAnuncio.getPrecio());
        databaseAddReference.child(application.getString((R.string.add_description_db_label)))
                .setValue(nuevoAnuncio.getDescripcion());

        List<String> listaImagenesAnuncioFormatoString = MisViviendasFragment.convierteListaUrisAListaStrings(nuevoAnuncio.getImagenesUri());
        databaseAddReference.child(application.getString((R.string.add_uri_list_db_label)))
                .setValue(listaImagenesAnuncioFormatoString);

        String categoria = nuevoAnuncio.getCategoria();
        databaseAddReference.child(application.getString((R.string.property_type_db_label)))
                .setValue(categoria);

        if (categoria.equals(application.getString(R.string.house_property_type_label))) {  // Si selecciona una casa.
            databaseAddReference.child(application.getString(R.string.add_house_type_db_label))
                    .setValue(nuevoAnuncio.getTipoCasa());
            databaseAddReference.child(application.getString(R.string.num_rooms_db_label))
                    .setValue(nuevoAnuncio.getHabitaciones());
            databaseAddReference.child(application.getString(R.string.num_bathrooms_db_label))
                    .setValue(nuevoAnuncio.getBanos());
            databaseAddReference.child(application.getString(R.string.orientation_db_label))
                    .setValue(nuevoAnuncio.getExteriorInterior());
        }
        else if (categoria.equals(application.getString(R.string.room_property_type_label))) {  // Si selecciona una habitación
            databaseAddReference.child(application.getString(R.string.max_num_roommates_db_label))
                    .setValue(nuevoAnuncio.getCompaneros());
            databaseAddReference.child(application.getString(R.string.roommate_gender_db_label))
                    .setValue(nuevoAnuncio.getGenero());
            databaseAddReference.child(application.getString(R.string.orientation_db_label))
                    .setValue(nuevoAnuncio.getExteriorInterior());
            databaseAddReference.child(application.getString(R.string.bathroom_type_db_label))
                    .setValue(nuevoAnuncio.getTipoBano());
        }


    }


    // Función que actualiza un anuncio ya existente en la base de datos.
    public static void actualizarAnuncioEnBD(Anuncio anuncioActualizado, Application application) {

        // Obtiene una instancia de la BD.
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(application.
                getApplicationContext().getString(R.string.database_url));

        // Obtiene la referencia a la BD de anuncios.
        DatabaseReference databaseAddReference = databaseInstance.getReference("adds")
                .child(anuncioActualizado.getId());

        databaseAddReference.child(application.getString(R.string.add_title_db_label))
                .setValue(anuncioActualizado.getTitulo());
        databaseAddReference.child(application.getString((R.string.add_location_db_label)))
                .setValue(anuncioActualizado.getUbicacion());
        databaseAddReference.child(application.getString((R.string.add_square_meters_db_label)))
                .setValue(anuncioActualizado.getMetros());
        databaseAddReference.child(application.getString((R.string.add_price_db_label)))
                .setValue(anuncioActualizado.getPrecio());
        databaseAddReference.child(application.getString((R.string.add_description_db_label)))
                .setValue(anuncioActualizado.getDescripcion());

        List<String> listaImagenesAnuncioFormatoString = MisViviendasFragment.convierteListaUrisAListaStrings(anuncioActualizado.getImagenesUri());
        databaseAddReference.child(application.getString((R.string.add_uri_list_db_label)))
                .setValue(listaImagenesAnuncioFormatoString);

        String categoria = anuncioActualizado.getCategoria();
        databaseAddReference.child(application.getString((R.string.property_type_db_label)))
                .setValue(categoria);

        if (categoria.equals(application.getString(R.string.house_property_type_label))) {  // Si selecciona una casa.

            // Elimina los campos que no sean de una casa (en el caso de que antes fuese un anuncio de una habitación).
            databaseAddReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    // Elimina el campo de máximo número de compañeros de piso.
                    String max_num_roomates = snapshot.child(application
                            .getString(R.string.max_num_roommates_db_label)).getValue(String.class);
                    if (max_num_roomates != null)
                        databaseAddReference.child(application.getString(R.string.max_num_roommates_db_label))
                                .removeValue();

                    // Elimina el campo de preferencia de sexo de los compañeros.
                    String roomate_gender = snapshot.child(application
                            .getString(R.string.roommate_gender_db_label)).getValue(String.class);
                    if (roomate_gender != null)
                        databaseAddReference.child(application.getString(R.string.roommate_gender_db_label))
                                .removeValue();

                    // Elimina el campo de tipo de baño.
                    String bathroom_type = snapshot.child(application
                            .getString(R.string.bathroom_type_db_label)).getValue(String.class);
                    if (bathroom_type != null)
                        databaseAddReference.child(application.getString(R.string.bathroom_type_db_label))
                                .removeValue();

                }
            });

            // Añade los campos propios del alquiler de una casa.
            databaseAddReference.child(application.getString(R.string.add_house_type_db_label))
                    .setValue(anuncioActualizado.getTipoCasa());
            databaseAddReference.child(application.getString(R.string.num_rooms_db_label))
                    .setValue(anuncioActualizado.getHabitaciones());
            databaseAddReference.child(application.getString(R.string.num_bathrooms_db_label))
                    .setValue(anuncioActualizado.getBanos());
            databaseAddReference.child(application.getString(R.string.orientation_db_label))
                    .setValue(anuncioActualizado.getExteriorInterior());

        }

        else if (categoria.equals(application.getString(R.string.room_property_type_label))) {  // Si selecciona una habitación

            // Elminina los campos que no sean de una habitación (en el casod e que antes fuese un anuncio de una casa).
            databaseAddReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    // Elimina el campo de tipo de casa.
                    String house_type = snapshot.child(application
                            .getString(R.string.add_house_type_db_label)).getValue(String.class);
                    if (house_type != null)
                        databaseAddReference.child(application.getString(R.string.add_house_type_db_label))
                                .removeValue();

                    // Elimina el campo de número de habitaciones.
                    String num_rooms = snapshot.child(application
                            .getString(R.string.num_rooms_db_label)).getValue(String.class);
                    if (num_rooms != null)
                        databaseAddReference.child(application.getString(R.string.num_rooms_db_label))
                                .removeValue();

                    // Elimina el campo de número de baños.
                    String num_bathroom = snapshot.child(application
                            .getString(R.string.num_bathrooms_db_label)).getValue(String.class);
                    if (num_bathroom != null)
                        databaseAddReference.child(application.getString(R.string.num_bathrooms_db_label))
                                .removeValue();

                }
            });

            // Añade los campos propios del alquiler de una habitación.
            databaseAddReference.child(application.getString(R.string.max_num_roommates_db_label))
                    .setValue(anuncioActualizado.getCompaneros());
            databaseAddReference.child(application.getString(R.string.roommate_gender_db_label))
                    .setValue(anuncioActualizado.getGenero());
            databaseAddReference.child(application.getString(R.string.orientation_db_label))
                    .setValue(anuncioActualizado.getExteriorInterior());
            databaseAddReference.child(application.getString(R.string.bathroom_type_db_label))
                    .setValue(anuncioActualizado.getTipoBano());

        }
    }


    // Función que elimina un anuncio de la base de datos.
    public static void eliminiarAnuncioEnBD(Anuncio nuevoAnuncio, Application application) {

        // Obtiene una instancia de la BD.
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(application.
                getApplicationContext().getString(R.string.database_url));

        // Obtiene la referencia a la BD de anuncios.
        DatabaseReference databaseAddReference = databaseInstance.getReference("adds")
                .child(nuevoAnuncio.getId());

        // Elmina el ID del anuncio de la lista de IDs de anuncios de este usuario.
        MisViviendasFragment.eliminarAnuncioListaAnunciosUsuario(nuevoAnuncio, application);

        // Eliminar la entrada del anuncio de la BD de anuncios.
        databaseAddReference.removeValue();
    }


    // Función que guarda el ID del anuncio actual en la lista con los IDs de los anuncios del usuario.
    private static void guardarAnuncioListaAnunciosUsuario(Anuncio nuevoAnuncio, Application application) {
        // Obtiene el usuario actual.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            // Obtiene una instancia de la BD.
            FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(application.
                    getApplicationContext().getString(R.string.database_url));

            // Obtiene la referencia a la BD de usuarios.
            DatabaseReference databaseUserReference = databaseInstance.getReference("users")
                    .child(user.getUid());

            databaseUserReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    // Obtiene la lista actual de anuncios de ese usuario
                    List<String> userAddsIdsList = new ArrayList<String>();

                    List<String> userAddsInDB = snapshot.child(application
                            .getString(R.string.user_adds_list_db_label)).getValue(
                            new GenericTypeIndicator<List<String>>() {
                            });

                    if (userAddsInDB != null) {
                        // Añade el ID del anuncio a la lista y la actualiza en la BD.
                        userAddsInDB.add(nuevoAnuncio.getId());
                        databaseUserReference.child(application.getString(R.string.user_adds_list_db_label))
                                .setValue(userAddsInDB);
                    }
                    else {
                        userAddsIdsList.add(nuevoAnuncio.getId());
                        databaseUserReference.child(application.getString(R.string.user_adds_list_db_label))
                                .setValue(userAddsIdsList);
                    }
                }
            });


        }
    }


    // Función que elimina el ID del anuncio a borrar de la lista con los IDs de los anuncios del usuario.
    private static void eliminarAnuncioListaAnunciosUsuario(Anuncio nuevoAnuncio, Application application) {
        // Obtiene el usuario actual.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            // Obtiene una instancia de la BD.
            FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(application.
                    getApplicationContext().getString(R.string.database_url));

            // Obtiene la referencia a la BD de usuarios.
            DatabaseReference databaseUserReference = databaseInstance.getReference("users")
                    .child(user.getUid());


            // Escucha primero el estado de la base de datos para asegurar que se actualizó correctamente.
            DatabaseReference anuncioReference = databaseInstance.getReference("adds")
                    .child(nuevoAnuncio.getId());

            databaseUserReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    // Obtiene la lista actual de anuncios de ese usuario
                    List<String> userAddsIdsList = new ArrayList<String>();

                    List<String> userAddsInDB = snapshot.child(application
                            .getString(R.string.user_adds_list_db_label)).getValue(
                            new GenericTypeIndicator<List<String>>() {
                            });

                    if (userAddsInDB != null) { // Como hay al menos un anuncio en la lista, esta nunca será vacía en este momento.
                        // Añade el ID del anuncio a la lista y la actualiza en la BD.
                        userAddsInDB.remove(nuevoAnuncio.getId());
                        databaseUserReference.child(application.getString(R.string.user_adds_list_db_label))
                                .setValue(userAddsInDB);
                    }
                }
            });
        }
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
