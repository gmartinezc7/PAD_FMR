package es.ucm.fdi.v3findmyroommate.ui.home;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import es.ucm.fdi.v3findmyroommate.R;


public class ViviendaAdapter extends RecyclerView.Adapter<ViviendaAdapter.ViviendaViewHolder> {

    private List<Vivienda> listViv;

    public ViviendaAdapter(List<Vivienda> lista){
        this.listViv = lista;
    }

    @NonNull
    @Override
    public ViviendaAdapter.ViviendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viv, parent, false);
        return new ViviendaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViviendaAdapter.ViviendaViewHolder holder, int position) {
        Vivienda vivienda = listViv.get(position);
        holder.name.setText(vivienda.getTitle());
        holder.address.setText(vivienda.getLocation());
        holder.description.setText(vivienda.getDescription());
        holder.price.setText(vivienda.getPrice().toString());
    }

    @Override
    public int getItemCount() {
        return listViv.size();
    }

    public static class ViviendaViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, description, price;

        public ViviendaViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            price = itemView.findViewById(R.id.price);
            description = itemView.findViewById(R.id.description);
        }
    }
    public void updateList (List<Vivienda> newVivs){
        this.listViv = newVivs;
        notifyDataSetChanged();
    }
}
