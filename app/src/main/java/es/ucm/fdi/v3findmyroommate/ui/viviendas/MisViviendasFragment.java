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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import es.ucm.fdi.v3findmyroommate.LocaleUtils;
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
        adapter = new AnunciosAdapter(misViviendasViewModel, this, this.getContext());
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

        intent.putExtra(this.getString(R.string.key_id), anuncio.getId());
        intent.putExtra(this.getString(R.string.key_titulo), anuncio.getTitulo());
        intent.putExtra(this.getString(R.string.key_ubicacion), anuncio.getUbicacion());
        intent.putExtra(this.getString(R.string.key_metros), anuncio.getMetros());
        intent.putExtra(this.getString(R.string.key_precio), anuncio.getPrecio());
        intent.putExtra(this.getString(R.string.key_descripcion), anuncio.getDescripcion());
        intent.putParcelableArrayListExtra(this.getString(R.string.key_imagenes_uri), new ArrayList<>(anuncio.getImagenesUri()));


        //TAGS
        String categoria = anuncio.getCategoria();
        intent.putExtra(this.getString(R.string.key_categoria), categoria);


        if (categoria.equalsIgnoreCase(this.getString(R.string.house_property_type_label))) {

            intent.putExtra(this.getString(R.string.key_tipo_casa), anuncio.getTipoCasa());
            intent.putExtra(this.getString(R.string.key_habitaciones), anuncio.getHabitaciones());
            intent.putExtra(this.getString(R.string.key_banos), anuncio.getBanos());
            intent.putExtra(this.getString(R.string.key_exterior_interior), anuncio.getExteriorInterior());
        } else if (categoria.equalsIgnoreCase(this.getString(R.string.room_property_type_label))) {

            intent.putExtra(this.getString(R.string.key_companeros), anuncio.getCompaneros());
            intent.putExtra(this.getString(R.string.key_genero), anuncio.getGenero());
            intent.putExtra(this.getString(R.string.key_exterior_interior), anuncio.getExteriorInterior());
            intent.putExtra(this.getString(R.string.key_tipo_bano), anuncio.getTipoBano());
        }

        verAnuncioLauncher.launch(intent);

    }






   //---------------------------------BASE DE DATOS:--------------------------------------------------------------------

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
                                    currentAddIntent.putExtra(this.getString(R.string.key_id), addID);
                                    currentAddIntent.putExtra(this.getString(R.string.key_titulo), titulo);
                                    currentAddIntent.putExtra(this.getString(R.string.key_ubicacion), ubicacion);
                                    currentAddIntent.putExtra(this.getString(R.string.key_metros), metros);
                                    currentAddIntent.putExtra(this.getString(R.string.key_precio), precio);
                                    currentAddIntent.putExtra(this.getString(R.string.key_descripcion), descripcion);
                                    currentAddIntent.putExtra(this.getString(R.string.key_categoria), categoria);
                                    currentAddIntent.putParcelableArrayListExtra(this.getString(R.string.key_imagenes_uri), new ArrayList<>(listaImagenesFormatoUri));

                                    if (categoria != null) {

                                        if (categoria.equals(getContext().getApplicationContext().getString(
                                                R.string.house_property_type_label))) {  // Si el anuncio es de una casa.

                                            String tipo_casa = secondSnapshot.child(getActivity().getApplication()
                                                    .getString(R.string.add_house_type_db_label)).getValue(String.class);
                                            String num_habitaciones = secondSnapshot.child(getActivity().getApplication()
                                                    .getString(R.string.num_rooms_db_label)).getValue(String.class);
                                            String num_banos = secondSnapshot.child(getActivity().getApplication()
                                                    .getString(R.string.num_bathrooms_db_label)).getValue(String.class);
                                            String orientacion = secondSnapshot.child(getActivity().getApplication()
                                                    .getString(R.string.orientation_db_label)).getValue(String.class);

                                            // Guardamos los datos específicos, propios de una casa.
                                            currentAddIntent.putExtra(this.getString(R.string.key_tipo_casa), tipo_casa);
                                            currentAddIntent.putExtra(this.getString(R.string.key_habitaciones), num_habitaciones);
                                            currentAddIntent.putExtra(this.getString(R.string.key_banos), num_banos);
                                            currentAddIntent.putExtra(this.getString(R.string.key_exterior_interior), orientacion);
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
                                            currentAddIntent.putExtra(this.getString(R.string.key_companeros), max_companeros);
                                            currentAddIntent.putExtra(this.getString(R.string.key_genero), genero_companeros);
                                            currentAddIntent.putExtra(this.getString(R.string.key_exterior_interior), orientacion);
                                            currentAddIntent.putExtra(this.getString(R.string.key_tipo_bano), tipo_bano);
                                        }

                                    }

                                    // Añade el anuncio a la lista del modelo.
                                    misViviendasViewModel.addAnuncio(currentAddIntent);

                                    // Crea un nuevo anuncio a partir del intent y lo guarda en la lista.
                                    Anuncio newAdd = new Anuncio(this.getContext(), currentAddIntent);
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


    // Método que actualiza la BD de anuncios, bien introduciendo el nuevo anuncio a crear, bien
    // actualizando el anuncio dado.
    public static void guardarOActualizarAnuncioEnBD(Anuncio nuevoAnuncio, Application application) {

        // Guarda el ID del anuncio en la lista de IDs de anuncios de este usuario.
        MisViviendasFragment.actualizarListaAnunciosUsuario(nuevoAnuncio, application);

        // Obtiene una instancia de la BD.
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(application.
                getApplicationContext().getString(R.string.database_url));

        // Obtiene la referencia a la BD de anuncios.
        DatabaseReference databaseAddReference = databaseInstance.getReference("adds")
                .child(nuevoAnuncio.getId());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();    // Obtiene el usuario actual.
        if (user != null) {
            databaseAddReference.child(application.getString(R.string.add_user_id_db_label))
                    .setValue(user.getUid());
        }
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

        if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                nuevoAnuncio.getCategoria(), R.string.house_property_type_label)) {  // Si selecciona una casa.
            String housePropertyTypeLabelDefaultLanguage = LocaleUtils.getValueInDBLocale(application
                .getApplicationContext(), R.string.house_property_type_label);
            databaseAddReference.child(application.getString(R.string.property_type_db_label))
                    .setValue(housePropertyTypeLabelDefaultLanguage);

            MisViviendasFragment.eliminarCamposPreviosTipoPropiedadCasa(databaseAddReference, application);

            MisViviendasFragment.guardarTipoDeCasa(nuevoAnuncio, application, databaseAddReference);

            // Como es un valor numérico, no es necesario compararlo con otros idiomas.
            databaseAddReference.child(application.getString(R.string.num_rooms_db_label))
                    .setValue(nuevoAnuncio.getHabitaciones());

            // Como es un valor numérico, no es necesario compararlo con otros idiomas.
            databaseAddReference.child(application.getString(R.string.num_bathrooms_db_label))
                    .setValue(nuevoAnuncio.getBanos());

            MisViviendasFragment.guardarOrientacion(nuevoAnuncio, application, databaseAddReference);

            // Como es un valor numérico, no es necesario compararlo con otros idiomas.
            databaseAddReference.child(application.getString(R.string.square_meters_db_label))
                    .setValue(nuevoAnuncio.getMetros());

        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                nuevoAnuncio.getCategoria(), R.string.room_property_type_label)) {  // Si selecciona una habitación.
            String roomPropertyTypeLabelDefaultLanguage = LocaleUtils.getValueInDBLocale(application
                    .getApplicationContext(), R.string.room_property_type_label);
            databaseAddReference.child(application.getString(R.string.property_type_db_label))
                    .setValue(roomPropertyTypeLabelDefaultLanguage);

            MisViviendasFragment.eliminarCamposPreviosTipoPropiedadHabitacion(databaseAddReference, application);

            // Como es un valor numérico, no es necesario compararlo con otros idiomas.
            databaseAddReference.child(application.getString(R.string.max_num_roommates_db_label))
                    .setValue(nuevoAnuncio.getCompaneros());

            MisViviendasFragment.guardarGenero(nuevoAnuncio, application, databaseAddReference);

            MisViviendasFragment.guardarOrientacion(nuevoAnuncio, application, databaseAddReference);

            MisViviendasFragment.guardarTipoDeBano(nuevoAnuncio, application, databaseAddReference);

        }

    }


    // Función que elimina un anuncio de la base de datos.
    public static void eliminarAnuncioEnBD(Anuncio nuevoAnuncio, Application application) {

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
    

    // Método que guarda en la BD el valor seleccionado para el campo de tipo de casa en el lenguaje por defecto.
    private static void guardarTipoDeCasa(Anuncio nuevoAnuncio, Application application, DatabaseReference
            databaseAddReference) {

        String tipoCasaSeleccionado = nuevoAnuncio.getTipoCasa();
        String tipoCasaLenguajePredeterminado = "";

        if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                tipoCasaSeleccionado, R.string.house_house_type)) {
            tipoCasaLenguajePredeterminado = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.house_house_type);
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                tipoCasaSeleccionado, R.string.apartment_house_type)) {
            tipoCasaLenguajePredeterminado = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.apartment_house_type);
        }

        databaseAddReference.child(application.getString(R.string.add_house_type_db_label))
                .setValue(tipoCasaLenguajePredeterminado);

    }


    // Método que guarda en la BD el valor seleccionado para el campo de orientación en el lenguaje por defecto.
    private static void guardarOrientacion(Anuncio nuevoAnuncio, Application application, DatabaseReference
            databaseAddReference) {

        String orientacionSeleccionada = nuevoAnuncio.getExteriorInterior();
        String orientacionLenguajePredeterminado = "";

        if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                orientacionSeleccionada, R.string.interior_orientation)) {
            orientacionLenguajePredeterminado = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.interior_orientation);
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                orientacionSeleccionada, R.string.exterior_orientation)) {
            orientacionLenguajePredeterminado = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.exterior_orientation);
        }

        databaseAddReference.child(application.getString(R.string.orientation_db_label))
                .setValue(orientacionLenguajePredeterminado);

    }


    // Método que guarda en la BD el valor seleccionado para el campo de género de los compañeros en el lenguaje por defecto.
    private static void guardarGenero(Anuncio nuevoAnuncio, Application application, DatabaseReference
            databaseAddReference) {

        String generoSeleccionado = nuevoAnuncio.getGenero();
        String generoLenguajePredeterminado = "";

        if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                generoSeleccionado, R.string.man)) {
            generoLenguajePredeterminado = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.man);
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                generoSeleccionado, R.string.woman)) {
            generoLenguajePredeterminado = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.woman);
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                generoSeleccionado, R.string.both_label)) {
            generoLenguajePredeterminado = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.both_label);
        }

        databaseAddReference.child(application.getString(R.string.roommate_gender_db_label))
                .setValue(generoLenguajePredeterminado);

    }


    // Método que guarda en la BD el valor seleccionado para el campo de tipo de baño en el lenguaje por defecto.
    private static void guardarTipoDeBano(Anuncio nuevoAnuncio, Application application, DatabaseReference
            databaseAddReference) {

        String tipoBanoSeleccionado = nuevoAnuncio.getTipoBano();
        String tipoBanoLenguajePredeterminado = "";

        if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                tipoBanoSeleccionado, R.string.private_bathroom_type)) {
            tipoBanoLenguajePredeterminado = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.private_bathroom_type);
        }

        else if (LocaleUtils.doesStringMatchAnyLanguage(application.getApplicationContext(),
                tipoBanoSeleccionado, R.string.shared_bathroom_type)) {
            tipoBanoLenguajePredeterminado = LocaleUtils.getValueInDBLocale(application.getApplicationContext(),
                    R.string.shared_bathroom_type);
        }

        databaseAddReference.child(application.getString(R.string.bathroom_type_db_label))
                .setValue(tipoBanoLenguajePredeterminado);

    }


    // Método que elimina los campos que no sean los propios de una casa.
    private static void eliminarCamposPreviosTipoPropiedadCasa(DatabaseReference databaseAddReference,
                                                               Application application) {

        databaseAddReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();

                // Elimina el campo de máximo número de compañeros de piso.
                String max_num_roomates = snapshot.child(application
                        .getString(R.string.max_num_roommates_db_label)).getValue(String.class);
                if (max_num_roomates != null)
                    databaseAddReference.child(application.getString(R.string.max_num_roommates_db_label))
                            .removeValue();

                // Elimina el campo de preferencia de género de los compañeros.
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
    }


    // Método que elimina los campos que no sean los propios de una habitación.
    private static void eliminarCamposPreviosTipoPropiedadHabitacion(DatabaseReference databaseAddReference,
                                                                     Application application) {

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

                // Elimina el campo de metros cuadrados.
                String square_meters = snapshot.child(application
                        .getString(R.string.square_meters_db_label)).getValue(String.class);
                if (square_meters != null)
                    databaseAddReference.child(application.getString(R.string.square_meters_db_label))
                            .removeValue();

            }
        });
    }


    // Función que guarda el ID del anuncio actual en la lista con los IDs de los anuncios del usuario,
    // si tal anuncio no ha sido guardado ya.
    private static void actualizarListaAnunciosUsuario(Anuncio nuevoAnuncio, Application application) {
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
                        // Si el ID del anuncio no está en la lista, lo añade (caso de crear anuncio).
                        if (!userAddsInDB.contains(nuevoAnuncio.getId())) {
                            userAddsInDB.add(nuevoAnuncio.getId());
                            databaseUserReference.child(application.getString(R.string.user_adds_list_db_label))
                                    .setValue(userAddsInDB);

                        }
                    }
                    else {
                        userAddsIdsList.add(nuevoAnuncio.getId());
                        databaseUserReference.child(application.getString(R.string.user_adds_list_db_label)).setValue(userAddsIdsList);
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
                    List<String> userAddsInDB = snapshot.child(application
                            .getString(R.string.user_adds_list_db_label)).getValue(
                        new GenericTypeIndicator<List<String>>() {
                        });

                    if (userAddsInDB != null) { // Como hay al menos un anuncio en la lista, esta nunca será vacía en este momento.
                        // Añade el ID del anuncio a la lista y la actualiza en la BD.
                        userAddsInDB.remove(nuevoAnuncio.getId());
                        databaseUserReference.child(application.getString(R.string.user_adds_list_db_label)).setValue(userAddsInDB);
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
