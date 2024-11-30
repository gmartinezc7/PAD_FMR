package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;


//CLASE QUE CONTIENE LA LISTA DE ANUNCIOS "MIS ANUNCIOS"
/*

TIENE DIFERENTES FUNCIONES QUE PERMITEN EL CORRECTO FUNCIONAMIENTO DE LA LISTA
ESTA LISTA SERÁ "OBSERVADA" A TRAVÉS DEL RECYCLERVIEW, POR ESO USAREMOS
MutableLiveData<List<Anuncio>> anuncios PARA LA LISTA DE ANUNCIOS

SIEMPRE QUE REALIZAMOS UN CAMBIO EN LA LISTA, AL FINAL USAMOS  anuncios.setValue(listaActual);
PARA NOTIFICAR DEL CAMBIO
 */
public class MisViviendasViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Anuncio>> anuncios;
    private boolean anunciosCargados = false;

    public MisViviendasViewModel(Application application) {
        super(application);
        anuncios = new MutableLiveData<>();
        List<Anuncio> listaInicial = new ArrayList<>();
        anuncios.setValue(listaInicial);
    }

    public boolean isAnunciosCargados() {
        return anunciosCargados;
    }

    public void setAnunciosCargados(boolean cargados) {
        this.anunciosCargados = cargados;
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
            Anuncio nuevoAnuncio = new Anuncio(this.getApplication(), data);
            listaActual.add(nuevoAnuncio);
            anuncios.setValue(listaActual);
        }
    }


    public void eliminarAnuncio(int position) {
        List<Anuncio> listaActual = anuncios.getValue();
        if (listaActual != null && position >= 0 && position < listaActual.size()) {

            Application application = getApplication();
            MisViviendasFragment.eliminarAnuncioEnBD(listaActual.get(position), this.getApplication());
            listaActual.remove(position);
            anuncios.setValue(listaActual);
        }
    }


    public void actualizarAnuncio(int position, Intent data) {
        List<Anuncio> listaActual = anuncios.getValue();
        if (listaActual != null && position >= 0 && position < listaActual.size()) {
            Anuncio anuncioActualizado = new Anuncio(this.getApplication(), data);
            listaActual.set(position, anuncioActualizado);
            anuncios.setValue(listaActual);
        }
    }
}