package es.ucm.fdi.v3findmyroommate.ui.home;


import androidx.annotation.NonNull;
import androidx.annotation.StyleableRes;
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
                    vivienda.setOwnerId(userId);
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
        // DEBUG
        System.out.println("SE HA LLAMADO A LA FUNCIÓN applyFiltersViewModel con los valores: ");
        System.out.println("Filtros: " + categoria + tipoCasa + numhabs + numbanos + numComps + genero + orientation + tipobano);
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

            // FILTROS CATEGORIA CASA
            if (categoria != null && categoria.equals("Home")){
                System.out.println("ES UNA CASAAAAAA");
                System.out.println("Vivienda: " + vivienda.getId());

                if (tipoCasa != null && !tipoCasa.isEmpty() && !tipoCasa.equals(vivienda.getTipoCasa() == null ? "" : vivienda.getTipoCasa())){
                    System.out.println("tipoCasa no Coincide. TipoCasaFiltro: " + tipoCasa + " TipoCasaVivienda" + vivienda.getTipoCasa());
                    filtrosOK = false;
                }else System.out.println("tipoCasa Coincide");

                if (numhabs != null && !numhabs.isEmpty() && !numhabs.equals(vivienda.getHabitaciones() == null ? "" : vivienda.getHabitaciones())){
                    System.out.println("numhabs no Coincide. NumHabsFiltro: " + numhabs + " NumHabsVivienda" + vivienda.getHabitaciones());
                    filtrosOK = false;
                }else System.out.println("numhabs Coincide");

                if (numbanos != null && !numbanos.isEmpty() && !numbanos.equals(vivienda.getBanos() == null ? "" : vivienda.getBanos())){
                    System.out.println("numbanos no Coincide. NumBanosFiltro: " + numbanos + " NumBanosVivienda: " + vivienda.getBanos());
                    filtrosOK = false;
                }else System.out.println("numbanos Coincide");

                if (orientation != null && !orientation.isEmpty() && !orientation.equals(vivienda.getExteriorInterior() == null ? "" : vivienda.getExteriorInterior())){
                    System.out.println("orientation no Coincide");
                    filtrosOK = false;
                }else System.out.println("orientation Coincide");
            }

            // Filtros Categoría Habitación


            if (categoria != null && categoria.equals("Habitación")){
                System.out.println("ES UNA HABITACIOOOOON");
                System.out.println("Vivienda: " + vivienda.getId());
                if (numComps != null && !numComps.isEmpty() && !numComps.equals(vivienda.getCompaneros() == null ? "" : vivienda.getCompaneros())){
                    System.out.println("numcomps no Coincide. Numcompsfiltro: " + numComps + " numCompsVivienda: " + vivienda.getCompaneros());
                    filtrosOK = false;
                }else System.out.println("numcomps Coincide");

                if (genero != null && !genero.isEmpty() && !genero.equals(vivienda.getGenero() == null ? "" : vivienda.getGenero())){
                    System.out.println("genero no Coincide. Generofiltro: " + genero + " Generovivienda: " + vivienda.getGenero());
                    filtrosOK = false;
                }System.out.println("genero Coincide");

                if (tipobano != null && !tipobano.isEmpty() && !tipobano.equals(vivienda.getTipoBano() == null ? "" : vivienda.getTipoBano())){
                    System.out.println("tipobano no Coincide. TipoBanoFiltro: " + tipobano + " TipoBanoVivienda: " + vivienda.getTipoBano());
                    filtrosOK = false;
                }else System.out.println("tipobano Coincide");

                if (orientation != null && !orientation.isEmpty() && !orientation.equals(vivienda.getExteriorInterior() == null ? "" : vivienda.getExteriorInterior())){
                    System.out.println("orientation no Coincide");
                    filtrosOK = false;
                }else System.out.println("orientation Coincide");
            }



            if (filtrosOK){
                filtered.add(vivienda);
                System.out.println("FILTRADO");
                vivienda.printVivienda();
            }
        }

        // Ahora actualizamos el LiveData con las viviendas filtradas
        viviendas.setValue(filtered);

    }




}