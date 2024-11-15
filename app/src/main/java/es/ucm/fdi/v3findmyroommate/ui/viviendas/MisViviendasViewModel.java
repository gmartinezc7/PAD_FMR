package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MisViviendasViewModel extends ViewModel {

    private final MutableLiveData<List<Anuncio>> anuncios;
    private final List<Integer> availableIds; // Para almacenar IDs eliminados

    public MisViviendasViewModel() {
        anuncios = new MutableLiveData<>();
        availableIds = new ArrayList<>(); // Inicializa la lista de IDs disponibles

        List<Anuncio> listaInicial = new ArrayList<>();
        anuncios.setValue(listaInicial);
    }

    public LiveData<List<Anuncio>> getAnuncios() {

        return anuncios;
    }

    public Anuncio getAnuncio(int position) {

        List<Anuncio> listaActual = anuncios.getValue();
        if (listaActual != null && position >= 0 && position < listaActual.size()) {
            return listaActual.get(position);
        }
        else return null;
    }


    public void addAnuncio(Intent data) {

        List<Anuncio> listaActual = anuncios.getValue();
        if (listaActual != null) {
            Anuncio nuevoAnuncio = new Anuncio(data);
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

            // Elimina el anuncio
            listaActual.remove(position);

            // Actualiza la lista de anuncios en el MutableLiveData
            anuncios.setValue(listaActual); // Notifica el cambio
        }
    }


    public void actualizarAnuncio(int position, Intent data) {
        List<Anuncio> listaActual = anuncios.getValue();
        if (listaActual != null && position >= 0 && position < listaActual.size()) {
            Anuncio anuncioActualizado = new Anuncio(data);
            listaActual.set(position, anuncioActualizado);
            anuncios.setValue(listaActual); // Notifico el cambio
        }
    }
}