package es.ucm.fdi.v3findmyroommate.ui.home;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Vivienda>> viviendas;
    private List<Vivienda> viviendasini;

    public HomeViewModel() {
        viviendas = new MutableLiveData<>();
        cargarViviendas();
    }


    public LiveData<List<Vivienda>> getViviendas(){
        return viviendas;
    }

    private void cargarViviendas(){
        FirebaseDatabase databaser = FirebaseDatabase.getInstance("https://findmyroommate-86cbe-default-rtdb.europe-west1.firebasedatabase.app/");
        databaser.getReference("adds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Vivienda> lista = new ArrayList<>();
                for (DataSnapshot viviendas : snapshot.getChildren()){
                    Vivienda vivienda = new Vivienda();
                    String key = viviendas.getKey();
                    vivienda.setId(key);
                    vivienda.setTitle(viviendas.child("title").getValue(String.class));
                    vivienda.setLocation(viviendas.child("location").getValue(String.class));
                    vivienda.setMetr(viviendas.child("square_meters").getValue(String.class));
                    vivienda.setPrice(viviendas.child("price").getValue(String.class));
                    vivienda.setDescription(viviendas.child("description").getValue(String.class));
                    vivienda.setCategoria(viviendas.child("property_type").getValue(String.class));
                    vivienda.setTipoCasa(viviendas.child("house_type").getValue(String.class));
                    vivienda.setHabitaciones(viviendas.child("number_of_rooms").getValue(String.class));
                    vivienda.setBanos(viviendas.child("number_of_bathrooms").getValue(String.class));
                    vivienda.setExteriorInterior(viviendas.child("orientation").getValue(String.class));
                    vivienda.setCompaneros(viviendas.child("maximum_number_of_roomates").getValue(String.class));
                    vivienda.setGenero(viviendas.child("roommate_gender").getValue(String.class));
                    vivienda.setTipoBano(viviendas.child("bathroom_type").getValue(String.class));

                    //Obtener id y nombre dueño
                    String userId = viviendas.child("user_id").getValue(String.class);
                    vivienda.setUserId(userId);
                    if (userId != null) {
                        FirebaseDatabase.getInstance().getReference("users").child(userId).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String username = snapshot.getValue(String.class);
                                vivienda.setOwnerName(username);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                vivienda.setOwnerName("Desconocido");
                            }
                        });
                    }

                    //martineevivienda.printVivienda();


                    if (vivienda != null) {
                        lista.add(vivienda);
                        System.out.println("Vivienda carga: " + vivienda.getTitle());
                    }
                }
                viviendasini = new ArrayList<>(lista);
                viviendas.setValue(lista);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error en DATABASE");

            }
        });
    }

    public void applyFiltersViewModel (String categoria, String tipoCasa, String numhabs, String numbanos, String numComps, String genero, String orientation, String tipobano){
        List<Vivienda> filtered = new ArrayList<>();
        for (Vivienda vivienda : viviendasini){
            boolean filtrosOK = true;

            // La linea que compara cada filtro con el elemento de la vivienda es para comprobar si es
            // null, y en caso de serlo ser recibe una cadena vacía para poder compararlo

            // FILTRO CATEGORIA
            if (categoria != null && !categoria.isEmpty()) {
                if (!categoria.equals(vivienda.getCategoria() == null ? "" : vivienda.getCategoria())) {
                    filtrosOK = false;
                }
            }

            // FILTRO TIPO DE CASA
            if (tipoCasa != null && !tipoCasa.isEmpty()) {
                if (!tipoCasa.equals(vivienda.getTipoCasa() == null ? "" : vivienda.getTipoCasa())) {
                    filtrosOK = false;
                }
            }

            // FILTRO NUM HABITACIONES
            if (numhabs != null && !numhabs.isEmpty()){
                if (!numhabs.equals(vivienda.getHabitaciones() == null ? "" : vivienda.getHabitaciones())){
                    filtrosOK = false;
                }
            }

            // FILTRO NUM BANOS
            if (numbanos != null && !numbanos.isEmpty()){
                if (!numbanos.equals(vivienda.getBanos() == null ? "" : vivienda.getBanos())){
                    filtrosOK = false;
                }
            }

            // FILTRO NUM COMPS
            if (numComps != null && !numComps.isEmpty()){
                if (!numComps.equals(vivienda.getCompaneros() == null ? "" : vivienda.getCompaneros())){
                    filtrosOK = false;
                }
            }

            //FILTRO GENERO
            if(genero != null && !genero.isEmpty()){
                if (!genero.equals(vivienda.getGenero() == null ? "" : vivienda.getGenero())){
                    filtrosOK = false;
                }
            }

            // FILTRO ORIENTACION
            if (orientation != null && !orientation.isEmpty()){
                if (!orientation.equals(vivienda.getExteriorInterior() == null ? "" : vivienda.getExteriorInterior())){
                    filtrosOK = false;
                }
            }

            // FILTRO TIPOBANO
            if (tipobano != null && !tipobano.isEmpty()){
                if (!tipobano.equals(vivienda.getTipoBano() == null ? "" : vivienda.getTipoBano())){
                    filtrosOK = false;
                }
            }


            if (filtrosOK) filtered.add(vivienda);
        }

        // Ahora actualizamos el LiveData con las viviendas filtradas
        viviendas.setValue(filtered);

    }




}