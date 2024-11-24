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

    public LiveData<List<Vivienda>> getViviendas () { return viviendasfavs; }

    private void cargarViviendasFavs (){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://findmyroommate-86cbe-default-rtdb.europe-west1.firebasedatabase.app/");

        // Lista de favs de usuario
        database.getReference("users").child(userId).child("listfavoritos").addListenerForSingleValueEvent(new ValueEventListener() {
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

        database.getReference("viviendas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Vivienda> lista = new ArrayList<>();
                for (DataSnapshot viviendas : snapshot.getChildren()){
                    Vivienda vivienda = viviendas.getValue(Vivienda.class);
                    String vivID = viviendas.getKey(); // Obtiene key de la vivineda
                    System.out.println("LLEGA A CARGA DE FAVORITOS");
                    if (vivienda != null && vivID != null && favoritos.contains(vivID) ){
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