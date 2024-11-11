package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

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

        // Mostrar  detalles del anuncio
        holder.chipTitulo.setText("Vivienda: " + anuncio.getTitulo());
        holder.chipUbicacion.setText("Ubicación: " + anuncio.getUbicacion());
        holder.chipMetros.setText("Metros Cuadrados: " + anuncio.getMetros());
        holder.chipPrecio.setText("Precio: " + anuncio.getPrecio());



        // Establecer la visibilidad de previewRect POR SI ACASO ESTA OCULTO Y EVITAR PROBLEMAS
        holder.previewRect.setVisibility(View.VISIBLE);


        // Asignar la imagen usando el URI de la imagen del anuncio
        if (anuncio.getImagenUri() != null) {
            holder.imageViewAnuncio.setImageURI(anuncio.getImagenUri());

            // "Ver"
            holder.imageViewAnuncio.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), AnuncioDetalleActivity.class);
                intent.putExtra("titulo", anuncio.getTitulo());
                intent.putExtra("detalle", anuncio.getDetalle());
                intent.putExtra("imagenUri", anuncio.getImagenUri());

                holder.itemView.getContext().startActivity(intent);
            });

            // Clic largo: muestra la opción de eliminar
            holder.imageViewAnuncio.setOnLongClickListener(v -> {
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Eliminar anuncio")
                        .setMessage("¿Estás seguro de que deseas eliminar este anuncio?")
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            viewModel.eliminarAnuncio(position);
                            Toast.makeText(holder.itemView.getContext(), "Anuncio eliminado", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                        .show();
                return true;
            });
        }



    }

    @Override
    public int getItemCount() {
        return anuncios.size(); // Devuelve la cantidad de anuncios
    }

    // ViewHolder para manejar la vista de cada item
    static class AnuncioViewHolder extends RecyclerView.ViewHolder {



        Chip chipTitulo;
        Chip chipUbicacion;
        Chip chipMetros;
        Chip chipPrecio;

        View previewRect;
        ImageView imageViewAnuncio;

        public AnuncioViewHolder(@NonNull View itemView) {
            super(itemView);

            // Encuentra cada Chip por su ID
             chipTitulo = itemView.findViewById(R.id.chipTitulo);
             chipUbicacion = itemView.findViewById(R.id.chipUbicacion);
             chipMetros = itemView.findViewById(R.id.chipMetros);
             chipPrecio = itemView.findViewById(R.id.chipPrecio);


            previewRect = itemView.findViewById(R.id.preview_rect);
            imageViewAnuncio = itemView.findViewById(R.id.image_view_anuncio);
        }
    }
}
