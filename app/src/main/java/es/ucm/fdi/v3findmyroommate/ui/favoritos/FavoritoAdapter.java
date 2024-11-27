package es.ucm.fdi.v3findmyroommate.ui.favoritos;
import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.ui.home.HomeViewModel;

import android.widget.TextView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import es.ucm.fdi.v3findmyroommate.ui.home.Vivienda;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class FavoritoAdapter extends RecyclerView.Adapter <FavoritoAdapter.FavoritoViewHolder> {
    private List<Vivienda> listFav;

    public FavoritoAdapter (List<Vivienda> lista){ this.listFav = lista; }
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
    }

    @Override
    public int getItemCount() {
        return listFav.size();
    }

    public static class FavoritoViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, description, price, metr;

        public FavoritoViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            price = itemView.findViewById(R.id.price);
            description = itemView.findViewById(R.id.description);
            metr = itemView.findViewById(R.id.metr);
        }
    }

    public void updateList (List<Vivienda> newVivs){
        this.listFav = newVivs;
        notifyDataSetChanged();
    }
}
