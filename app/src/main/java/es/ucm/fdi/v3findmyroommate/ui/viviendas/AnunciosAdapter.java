package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.app.AlertDialog;
import android.content.Context;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.v3findmyroommate.R;

public class AnunciosAdapter extends RecyclerView.Adapter<AnunciosAdapter.AnuncioViewHolder> {

    private List<Anuncio> anuncios = new ArrayList<>();
    private MisViviendasViewModel viewModel;
    private MisViviendasFragment fragment;

private Context context;

    public AnunciosAdapter(MisViviendasViewModel viewModel, MisViviendasFragment fragment,
                           Context context) {
        this.fragment = fragment;
        this.viewModel = viewModel;
        this.context = context;
    }

    // METODO PARA ACTUALIZAR LA LISTA DE ANUNCIOS
    public void setAnuncios(List<Anuncio> nuevosAnuncios) {
        this.anuncios = nuevosAnuncios;
        notifyDataSetChanged(); // NOTIFICAMOS AL RECYCLERVIEW DE QUE LOS DATOS HAN CAMBIADO
    }

    @NonNull
    @Override
    public AnuncioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anuncio, parent, false);
        return new AnuncioViewHolder(view);

    }

    /*
    CONFIGURAMOS TODOS LOS "DETALLES" DEL ANUNCIO EN EL RECYCLERVIEW,
    COMO POR EJEMPLO LAS FLECHAS HACIA LOS LADOS PARA DESPLAZAR LA IMAGEN,
    LAS ETIQUETAS "CHIP" CON LA INFORMACIÓN, EL DIÁLOGO DE ELIMINADO AL DEJAR PULSADO,
    ETC.

     */
    @Override
    public void onBindViewHolder(@NonNull AnuncioViewHolder holder, int position) {
        Anuncio anuncio = anuncios.get(position);

        // CONFIGURAR LA INFORMACIÓN
        setAnuncioDetails(holder, anuncio);

        // CONFIGURAR LA IMAGEN (CLICK) Y LA NAVEGACIÓN ENTRE ELLAS
        setImageNavigation(holder, anuncio, position);


        // CLICK LARGO PARA ELIMIANR
        setLongClickListener(holder, position);

    }


    private void setAnuncioDetails(AnuncioViewHolder holder, Anuncio anuncio) {
        holder.chipTitulo.setText(this.context.getString(R.string.text_vivienda) + anuncio.getTitulo());
        holder.chipUbicacion.setText(this.context.getString(R.string.text_ubicacion) + anuncio.getUbicacion());
       holder.chipMetros.setText(anuncio.getMetros() + this.context.getString(R.string.text_metros_cuadrados));
       holder.chipPrecio.setText(anuncio.getPrecio() + this.context.getString(R.string.text_precio));
        holder.previewRect.setVisibility(View.VISIBLE);
    }


    private void setImageNavigation(AnuncioViewHolder holder, Anuncio anuncio, int position) {
        if (!anuncio.getImagenesUri().isEmpty()) {
            holder.imagenesUri = new ArrayList<>(anuncio.getImagenesUri());


            // Cargar la imagen actual usando Glide
            Glide.with(holder.imageViewAnuncio.getContext())
                    .load(holder.imagenesUri.get(holder.imagenActualIndex))
                    .into(holder.imageViewAnuncio);



            holder.btnPrev.setVisibility(holder.imagenActualIndex > 0 ? View.VISIBLE : View.INVISIBLE);
            holder.btnNext.setVisibility(holder.imagenActualIndex < holder.imagenesUri.size() - 1 ? View.VISIBLE : View.INVISIBLE);

            // NAVEGAR A LA IMAGEN ANTERIOR
            holder.btnPrev.setOnClickListener(v -> navigateImage(holder, -1));

            // NAVEGAR A LA SIGUIENTE
            holder.btnNext.setOnClickListener(v -> navigateImage(holder, 1));

            // CLICK EN LA IMAGEN PARA VER LOS DETALLES
            holder.imageViewAnuncio.setOnClickListener(v -> showAnuncioDetail(position));
        }
    }

    private void navigateImage(AnuncioViewHolder holder, int direction) {
        int newIndex = holder.imagenActualIndex + direction;
        if (newIndex >= 0 && newIndex < holder.imagenesUri.size()) {
            holder.imagenActualIndex = newIndex;
            // Cargar la imagen actual usando Glide
            Glide.with(holder.imageViewAnuncio.getContext())
                    .load(holder.imagenesUri.get(holder.imagenActualIndex))
                    .into(holder.imageViewAnuncio);

            holder.btnPrev.setVisibility(holder.imagenActualIndex > 0 ? View.VISIBLE : View.INVISIBLE);
            holder.btnNext.setVisibility(holder.imagenActualIndex < holder.imagenesUri.size() - 1 ? View.VISIBLE : View.INVISIBLE);
        }
    }




    //IMPORTANTE !!!!!
    private void showAnuncioDetail(int position) {

       // (LLAMA AL FRAGMENT PARA DESPUÉS PODER MANEJAR LOS POSIBLES CAMBIOS EN LA LISTA
        // ( "EDITAR" DENTRO DE "ANUNCIODETALLE"))
        this.fragment.lanzarVerAnuncio(position);


    }



    private void setLongClickListener(AnuncioViewHolder holder, int position) {
        holder.imageViewAnuncio.setOnLongClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle(this.context.getString(R.string.eliminar_anunio))
                    .setMessage(this.context.getString(R.string.pregunta_eliminar_anunio))
                    .setPositiveButton(this.context.getString(R.string.eliminar), (dialog, which) -> {
                        viewModel.eliminarAnuncio(position); //IMPORTANTE LA LLAMADA SIEMPRE A VIEWMODEL PARA MANEJAR LA LISTA
                        Toast.makeText(holder.itemView.getContext(), this.context.getString(R.string.mensaje_confirmacion_eliminado), Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(this.context.getString(R.string.cancelar), (dialog, which) -> dialog.dismiss())
                    .show();
            return true;
        });
    }




    @Override
    public int getItemCount() {
        return anuncios.size(); // DEVUELVE LA CANTIDAD DE ANUNCIOS
    }


    // VIEWHOLDER PARA MANEJAR LA VISTA DE CADA ITEM
    static class AnuncioViewHolder extends RecyclerView.ViewHolder {


        Chip chipTitulo;
        Chip chipUbicacion;
        Chip chipMetros;
        Chip chipPrecio;

        View previewRect;
        ImageView imageViewAnuncio;
        List<String> imagenesUri = new ArrayList<>();
        int imagenActualIndex = 0;
        ImageButton btnPrev, btnNext;

        public AnuncioViewHolder(@NonNull View itemView) {
            super(itemView);

            // BUSCAMOS CADA ELEMENTO POR SU ID
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
