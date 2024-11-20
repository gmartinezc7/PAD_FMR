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

    public HomeViewModel() {
        viviendas = new MutableLiveData<>();
        cargarViviendas();
    }


    public LiveData<List<Vivienda>> getViviendas(){
        return viviendas;
    }

    private void cargarViviendas(){
        FirebaseDatabase databaser = FirebaseDatabase.getInstance("https://findmyroommate-86cbe-default-rtdb.europe-west1.firebasedatabase.app/");
        databaser.getReference("viviendas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Vivienda> lista = new ArrayList<>();
                for (DataSnapshot viviendas : snapshot.getChildren()){
                    Vivienda vivienda = viviendas.getValue(Vivienda.class);
                    if (vivienda != null) {
                        lista.add(vivienda);
                    }
                }
                viviendas.setValue(lista);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error en DATABASE");

            }
        });
    }


}