package es.ucm.fdi.v3findmyroommate.ui.favoritos;

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
import es.ucm.fdi.v3findmyroommate.ui.home.Vivienda;


public class MisFavoritosViewModel extends ViewModel {

    private MutableLiveData<List<Vivienda>> viviendasfavs;

    public MisFavoritosViewModel() {
        viviendasfavs = new MutableLiveData<>();
        cargarViviendasFavs();
    }

    public LiveData<List<Vivienda>> getViviendas () { return viviendasfavs; }

    private void cargarViviendasFavs (){
        FirebaseDatabase databaser = FirebaseDatabase.getInstance("https://findmyroommate-86cbe-default-rtdb.europe-west1.firebasedatabase.app/");
        databaser.getReference("viviendas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Vivienda> lista = new ArrayList<>();
                for (DataSnapshot viviendas : snapshot.getChildren()){
                    Vivienda vivienda = viviendas.getValue(Vivienda.class);
                    if (vivienda != null) {
                        lista.add(vivienda);
                        System.out.println("Vivienda carga: " + vivienda.getTitle());
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