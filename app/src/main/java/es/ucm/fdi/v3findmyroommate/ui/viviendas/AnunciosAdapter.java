package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.v3findmyroommate.R;

public class AnunciosAdapter extends RecyclerView.Adapter<AnunciosAdapter.AnuncioViewHolder> {

    private List<String> anuncios = new ArrayList<>();
    private MisViviendasViewModel viewModel;

    public AnunciosAdapter(MisViviendasViewModel viewModel) {
        this.viewModel = viewModel;
    }

    // Método para actualizar la lista de anuncios
    public void setAnuncios(List<String> nuevosAnuncios) {
        this.anuncios = nuevosAnuncios;
        notifyDataSetChanged(); // Notifica al RecyclerView que los datos han cambiado
    }

    @NonNull
    @Override
    public AnuncioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del item (aquí podrías inflar tu layout personalizado)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anuncio, parent, false);
        return new AnuncioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnuncioViewHolder holder, int position) {


        String anuncio = anuncios.get(position);
        String[] partes = anuncio.split(": \n", 2);
        if (partes.length > 1) {
            holder.textViewAnuncioId.setText(partes[0]); // Anuncio X:
            holder.textViewAnuncioDetalle.setText(partes[1]); // Detalles del anuncio
        }

        holder.previewRect.setVisibility(View.VISIBLE);

        // Añadir un listener al botón de eliminar en cada anuncio
        holder.btnEliminar.setOnClickListener(v -> {
            // Aquí llamamos al método de eliminar del ViewModel
            viewModel.eliminarAnuncio(position);
        });


    }

    @Override
    public int getItemCount() {
        return anuncios.size(); // Devuelve la cantidad de anuncios
    }

    // ViewHolder para manejar la vista de cada item
    static class AnuncioViewHolder extends RecyclerView.ViewHolder {

        TextView textViewAnuncioId;
        TextView textViewAnuncioDetalle;

        Button btnEliminar;
        View previewRect;

        public AnuncioViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAnuncioId = itemView.findViewById(R.id.text_view_anuncio_id);
            textViewAnuncioDetalle = itemView.findViewById(R.id.text_view_anuncio_detalle);

            btnEliminar = itemView.findViewById(R.id.btn_eliminar);
            previewRect = itemView.findViewById(R.id.preview_rect);
        }
    }
}
