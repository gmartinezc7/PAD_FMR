package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MisViviendasViewModel extends ViewModel {

    private final MutableLiveData<List<Anuncio>> anuncios;
    private final List<Integer> availableIds; // Para almacenar IDs eliminados
    private int contadorAnuncios; // Para seguir el último ID usado

    public MisViviendasViewModel() {
        anuncios = new MutableLiveData<>();
        availableIds = new ArrayList<>(); // Inicializa la lista de IDs disponibles
        contadorAnuncios = 1; // Comenzar desde 1

        List<Anuncio> listaInicial = new ArrayList<>();
        anuncios.setValue(listaInicial);
    }

    public LiveData<List<Anuncio>> getAnuncios() {
        return anuncios;
    }

    public void addAnuncio(String nuevo, Uri imagenUri) {
        List<Anuncio> listaActual = anuncios.getValue();
        if (listaActual != null) {
            int nuevoId = !availableIds.isEmpty() ? availableIds.remove(availableIds.size() - 1) : contadorAnuncios++;
            Anuncio nuevoAnuncio = new Anuncio(nuevoId, nuevo, imagenUri);
            listaActual.add(nuevoAnuncio);
            anuncios.setValue(listaActual);
        }
    }


    public void eliminarAnuncio(int position) {
        List<Anuncio> listaActual = anuncios.getValue();
        if (listaActual != null && position >= 0 && position < listaActual.size()) {
            // Extraer el anuncio que se va a eliminar
            Anuncio anuncioEliminado = listaActual.get(position);

            // Extraer el ID del anuncio que se va a eliminar
            int idEliminado = anuncioEliminado.getId();

            // Elimina el anuncio
            listaActual.remove(position);

            // Actualiza los números de los anuncios restantes
            for (int i = position; i < listaActual.size(); i++) {
                Anuncio anuncioActual = listaActual.get(i);
                // Actualiza el ID
                listaActual.set(i, new Anuncio(anuncioActual.getId() - 1, anuncioActual.getDetalle(), anuncioActual.getImagenUri()));
            }

            // Actualiza el contador si es necesario
            if (listaActual.size() < contadorAnuncios) {
                contadorAnuncios--; // Actualiza el contador si hay anuncios eliminados
            }

            // Actualiza la lista de anuncios en el MutableLiveData
            anuncios.setValue(listaActual); // Notifica el cambio
        }
    }
}