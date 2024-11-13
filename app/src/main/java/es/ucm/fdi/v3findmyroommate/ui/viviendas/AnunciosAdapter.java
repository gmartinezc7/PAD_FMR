package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

        // Configurar detalles del anuncio
        setAnuncioDetails(holder, anuncio);

        // Configurar la imagen y la navegación entre imágenes
        setImageNavigation(holder, anuncio);


        // Configurar el clic largo para eliminar
        setLongClickListener(holder, position, anuncio);

    }


    private void setAnuncioDetails(AnuncioViewHolder holder, Anuncio anuncio) {
        holder.chipTitulo.setText("Vivienda: " + anuncio.getTitulo());
        holder.chipUbicacion.setText("Ubicación: " + anuncio.getUbicacion());
        holder.chipMetros.setText("Metros Cuadrados: " + anuncio.getMetros());
        holder.chipPrecio.setText("Precio: " + anuncio.getPrecio());
        holder.previewRect.setVisibility(View.VISIBLE);
    }


    private void setImageNavigation(AnuncioViewHolder holder, Anuncio anuncio) {
        if (!anuncio.getImagenesUri().isEmpty()) {
            holder.imagenesUri = new ArrayList<>(anuncio.getImagenesUri());
            holder.imageViewAnuncio.setImageURI(holder.imagenesUri.get(holder.imagenActualIndex));
            holder.btnPrev.setVisibility(holder.imagenActualIndex > 0 ? View.VISIBLE : View.INVISIBLE);
            holder.btnNext.setVisibility(holder.imagenActualIndex < holder.imagenesUri.size() - 1 ? View.VISIBLE : View.INVISIBLE);

            // Navegar hacia la imagen anterior
            holder.btnPrev.setOnClickListener(v -> navigateImage(holder, -1));

            // Navegar hacia la imagen siguiente
            holder.btnNext.setOnClickListener(v -> navigateImage(holder, 1));

            // Clic en la imagen para ver detalles
            holder.imageViewAnuncio.setOnClickListener(v -> showAnuncioDetail(holder, anuncio));
        }
    }

    private void navigateImage(AnuncioViewHolder holder, int direction) {
        int newIndex = holder.imagenActualIndex + direction;
        if (newIndex >= 0 && newIndex < holder.imagenesUri.size()) {
            holder.imagenActualIndex = newIndex;
            holder.imageViewAnuncio.setImageURI(holder.imagenesUri.get(holder.imagenActualIndex));
            holder.btnPrev.setVisibility(holder.imagenActualIndex > 0 ? View.VISIBLE : View.INVISIBLE);
            holder.btnNext.setVisibility(holder.imagenActualIndex < holder.imagenesUri.size() - 1 ? View.VISIBLE : View.INVISIBLE);
        }
    }


    private void showAnuncioDetail(AnuncioViewHolder holder, Anuncio anuncio) {
        Intent intent = new Intent(holder.itemView.getContext(), AnuncioDetalleActivity.class);
        intent.putExtra("titulo", anuncio.getTitulo());
        intent.putExtra("detalle", anuncio.getDetalle());
        intent.putParcelableArrayListExtra("imagenesUri", new ArrayList<>(anuncio.getImagenesUri()));
        holder.itemView.getContext().startActivity(intent);
    }



    private void setLongClickListener(AnuncioViewHolder holder, int position, Anuncio anuncio) {
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
        List<Uri> imagenesUri = new ArrayList<>();
        int imagenActualIndex = 0;
        ImageButton btnPrev, btnNext;

        public AnuncioViewHolder(@NonNull View itemView) {
            super(itemView);

            // Encuentra cada Chip por su ID
             chipTitulo = itemView.findViewById(R.id.chipTitulo);
             chipUbicacion = itemView.findViewById(R.id.chipUbicacion);
             chipMetros = itemView.findViewById(R.id.chipMetros);
             chipPrecio = itemView.findViewById(R.id.chipPrecio);


            previewRect = itemView.findViewById(R.id.preview_rect);
            imageViewAnuncio = itemView.findViewById(R.id.image_view_anuncio);
            btnPrev = itemView.findViewById(R.id.btn_prev);
            btnNext = itemView.findViewById(R.id.btn_next);
            btnPrev.setVisibility(View.INVISIBLE);
            btnNext.setVisibility(View.INVISIBLE);

        }
    }



}
