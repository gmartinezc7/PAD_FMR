package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.v3findmyroommate.R;

public class AnunciosAdapter extends RecyclerView.Adapter<AnunciosAdapter.AnuncioViewHolder> {

    private List<Anuncio> anuncios = new ArrayList<>();
    private MisViviendasViewModel viewModel;

    public AnunciosAdapter(MisViviendasViewModel viewModel) {
        this.viewModel = viewModel;
    }

    // Método para actualizar la lista de anuncios
    public void setAnuncios(List<Anuncio> nuevosAnuncios) {
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
        Anuncio anuncio = anuncios.get(position);

        // Mostrar el ID y los detalles del anuncio
        holder.textViewAnuncioId.setText(anuncio.getId() + ":");
        holder.textViewAnuncioDetalle.setText(anuncio.getDetalle());

        // Establecer la visibilidad de previewRect si es necesario
        holder.previewRect.setVisibility(View.VISIBLE);


        // Asignar la imagen usando el URI de la imagen del anuncio
        if (anuncio.getImagenUri() != null) {
            holder.imageViewAnuncio.setImageURI(anuncio.getImagenUri());
            // Alternativamente, puedes usar Glide
            // Glide.with(holder.itemView.getContext()).load(anuncio.getImageUri()).into(holder.imageViewAnuncio);
        }

        // Listener para el botón de eliminar
        holder.btnEliminar.setOnClickListener(v -> {
            viewModel.eliminarAnuncio(position); // Llama al método de eliminar en el ViewModel
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
        ImageView imageViewAnuncio;

        public AnuncioViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAnuncioId = itemView.findViewById(R.id.text_view_anuncio_id);
            textViewAnuncioDetalle = itemView.findViewById(R.id.text_view_anuncio_detalle);
            btnEliminar = itemView.findViewById(R.id.btn_eliminar);
            previewRect = itemView.findViewById(R.id.preview_rect);
            imageViewAnuncio = itemView.findViewById(R.id.image_view_anuncio);
        }
    }
}
