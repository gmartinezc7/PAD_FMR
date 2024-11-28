package es.ucm.fdi.v3findmyroommate.ui.favoritos;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import es.ucm.fdi.v3findmyroommate.ui.home.Vivienda;


public class MisFavoritosViewModel extends ViewModel {

    private MutableLiveData<List<Vivienda>> viviendasfavs;
    private String userId;

    public MisFavoritosViewModel() {
        viviendasfavs = new MutableLiveData<>();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        cargarViviendasFavs();
    }

    public MutableLiveData<List<Vivienda>> getViviendas () { return viviendasfavs; }

    private void cargarViviendasFavs (){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://findmyroommate-86cbe-default-rtdb.europe-west1.firebasedatabase.app/");

        // Lista de favs de usuario
        database.getReference("users").child(userId).child("listfavoritos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> favoritos = new ArrayList<>();
                for (DataSnapshot favoritosnap : snapshot.getChildren()){
                    String favorito = favoritosnap.getValue(String.class);
                    if (favorito != null){
                        System.out.println("FAVORITO: " + favorito);
                        favoritos.add(favorito);
                    }else System.out.println("FAVORITO NO ENCONTRADO");
                }

                // cargar las viviendas correspondientes
                cargarViviendasSegunFavoritos(favoritos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void cargarViviendasSegunFavoritos (List<String> favoritos){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://findmyroommate-86cbe-default-rtdb.europe-west1.firebasedatabase.app/");

        database.getReference("adds").addValueEventListener(new ValueEventListener() {
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
                    System.out.println("LLEGA A CARGA DE FAVORITOS");

                    if (vivienda != null && vivienda.getId() != null && favoritos.contains(vivienda.getId()) ){
                        lista.add(vivienda);
                        System.out.println("Favorita carga: " + vivienda.getTitle());
                    }
                }
                viviendasfavs.setValue(lista);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error en DATABASE");

            }
        });

    }
}