package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class MisViviendasViewModel extends ViewModel {

    private final MutableLiveData<List<String>> anuncios;
    private final List<Integer> availableIds; // Para almacenar IDs eliminados
    private int contadorAnuncios; // Para seguir el último ID usado

    public MisViviendasViewModel() {
        anuncios = new MutableLiveData<>();
        availableIds = new ArrayList<>(); // Inicializa la lista de IDs disponibles
        contadorAnuncios = 1; // Comenzar desde 1

        List<String> listaInicial = new ArrayList<>();
        anuncios.setValue(listaInicial);
    }

    public LiveData<List<String>> getAnuncios() {
        return anuncios;
    }

    public void addAnuncio(String nuevo) {
        List<String> listaActual = anuncios.getValue();
        if (listaActual != null) {
            int nuevoId;
            if (!availableIds.isEmpty()) {
                nuevoId = availableIds.remove(availableIds.size() - 1); // Reutiliza un ID disponible
            } else {
                nuevoId = contadorAnuncios++; // Usa el contador actual y lo incrementa
            }
            String nuevoAnuncio = "Anuncio " + nuevoId + ": \n" + nuevo;
            listaActual.add(nuevoAnuncio);
            anuncios.setValue(listaActual);
        }
    }

    public void eliminarAnuncio(int position) {
        List<String> listaActual = anuncios.getValue();
        if (listaActual != null && position >= 0 && position < listaActual.size()) {
            // Extraer el anuncio que se va a eliminar
            String anuncioEliminado = listaActual.get(position);

            // Extraer el ID del anuncio que se va a eliminar
            // El ID es la parte después de "Anuncio " y antes de ":"
            int idEliminado = Integer.parseInt(anuncioEliminado.split(" ")[1].replace(":", ""));

            // Elimina el anuncio
            listaActual.remove(position);

            // Actualiza los números de los anuncios restantes
            for (int i = position; i < listaActual.size(); i++) {
                String anuncioActual = listaActual.get(i);
                // Extraer el número actual del anuncio
                int numeroActual = Integer.parseInt(anuncioActual.split(" ")[1].replace(":", ""));
                // Reemplazar el número con el decrementar el ID
                listaActual.set(i, anuncioActual.replace("Anuncio " + numeroActual, "Anuncio " + (numeroActual - 1)));
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