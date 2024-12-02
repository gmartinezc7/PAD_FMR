package es.ucm.fdi.v3findmyroommate.ui.favoritos;
import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.ui.home.HomeFragment;
import es.ucm.fdi.v3findmyroommate.ui.home.HomeViewModel;

import android.net.Uri;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import es.ucm.fdi.v3findmyroommate.ui.home.Vivienda;
import es.ucm.fdi.v3findmyroommate.ui.home.ViviendaAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class FavoritoAdapter extends RecyclerView.Adapter <FavoritoAdapter.FavoritoViewHolder> {
    private List<Vivienda> listFav;
    private MisFavoritosFragment fragment;

    public FavoritoAdapter (List<Vivienda> lista, MisFavoritosFragment fragment){

        this.fragment = fragment;
        this.listFav = lista;

    }
    @NonNull
    @Override
    public FavoritoAdapter.FavoritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fav, parent, false);
        return new FavoritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritoAdapter.FavoritoViewHolder holder, int position) {
        Vivienda vivienda = listFav.get(position);
        holder.name.setText(vivienda.getTitle());
        holder.address.setText(vivienda.getLocation());
        holder.description.setText(vivienda.getDescription());
        holder.price.setText(vivienda.getPrice().toString());
        holder.metr.setText(vivienda.getMetr().toString());

        //SAM-----------------------------------------------------------------------------------------------------
        holder.previewRect.setVisibility(View.VISIBLE);
        //SAM--------------------------------------------------------------------------------------------------------------------------
        setImageNavigation(holder, vivienda);
//-------------------------------------------------------------------------------------------------------------------



    }


    //SAM---------------------------------------------------------------------------------------------------------------------------------------
    private void setImageNavigation(FavoritoAdapter.FavoritoViewHolder holder, Vivienda vivienda) {
        if (!vivienda.getImagenesUri().isEmpty()) { // Usamos URLs en lugar de URIs
            holder.imagenesUri = new ArrayList<>(vivienda.getImagenesUri());

            // Cargar la imagen actual usando Glide
            Glide.with(holder.imageViewAnuncio.getContext())
                    .load(holder.imagenesUri.get(holder.imagenActualIndex))
                    .into(holder.imageViewAnuncio);

            // Actualizar visibilidad de los botones
            holder.btnPrev.setVisibility(holder.imagenActualIndex > 0 ? View.VISIBLE : View.INVISIBLE);
            holder.btnNext.setVisibility(holder.imagenActualIndex < holder.imagenesUri.size() - 1 ? View.VISIBLE : View.INVISIBLE);

            // Navegar a la imagen anterior
            holder.btnPrev.setOnClickListener(v -> navigateImage(holder, -1));

            // Navegar a la siguiente imagen
            holder.btnNext.setOnClickListener(v -> navigateImage(holder, 1));

            // Click en la imagen para ver los detalles
            holder.imageViewAnuncio.setOnClickListener(v -> showAnuncioDetail(vivienda));
        }
    }


    private void navigateImage(FavoritoAdapter.FavoritoViewHolder holder, int direction) {
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
    private void showAnuncioDetail(Vivienda vivienda) {

        this.fragment.lanzarVerAnuncio(vivienda);

    }
    //---------------------------------------------------------------------------------------------------------------------------------------





    @Override
    public int getItemCount() {
        return listFav.size();
    }

    public static class FavoritoViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, description, price, metr;


        //SAM-------------------------------------------------------------
        View previewRect;
        ImageView imageViewAnuncio;
        List<String> imagenesUri = new ArrayList<>();
        int imagenActualIndex = 0;
        ImageButton btnPrev, btnNext;
        //---------------------------------------------------------------------------


        public FavoritoViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            price = itemView.findViewById(R.id.price);
            description = itemView.findViewById(R.id.description);
            metr = itemView.findViewById(R.id.metr);


            //SAM------------------------------------------------------------------------------------------
            previewRect = itemView.findViewById(R.id.preview_rect);
            imageViewAnuncio = itemView.findViewById(R.id.image_view_anuncio);
            btnPrev = itemView.findViewById(R.id.btn_prev);
            btnNext = itemView.findViewById(R.id.btn_next);
            btnPrev.setVisibility(View.INVISIBLE);
            btnNext.setVisibility(View.INVISIBLE);
            //------------------------------------------------------------------------------------------

        }
    }

    public void updateList (List<Vivienda> newVivs){
        this.listFav = newVivs;
        notifyDataSetChanged();
    }
}
