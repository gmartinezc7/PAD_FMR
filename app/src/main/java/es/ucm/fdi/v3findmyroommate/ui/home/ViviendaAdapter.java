package es.ucm.fdi.v3findmyroommate.ui.home;

import com.google.android.material.chip.Chip;

import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.v3findmyroommate.R;


public class ViviendaAdapter extends RecyclerView.Adapter<ViviendaAdapter.ViviendaViewHolder> {

    private List<Vivienda> listViv;
    private String userId;
    private FirebaseDatabase database;

    public ViviendaAdapter(List<Vivienda> lista){

        this.listViv = lista;
        this.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.database = FirebaseDatabase.getInstance("https://findmyroommate-86cbe-default-rtdb.europe-west1.firebasedatabase.app/");
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
        holder.price.setText(vivienda.getPrice());
        holder.metr.setText(vivienda.getMetr());
        setImageNavigationViv(holder,vivienda,position);

        // El resto de atributos, como son chips que quiero mostrar y no mostrar dependiendo de la
        // categoría, pues los cargamos dependiendo de si son atributos de esa categoría o no, con
        // el siguiente método

        setChipVisibility(holder.categoria, vivienda.getCategoria(),"Categoria - ");
        setChipVisibility(holder.tipoCasa, vivienda.getTipoCasa(), "Tipo Casa - ");
        setChipVisibility(holder.habitaciones, vivienda.getHabitaciones(), "Habitaciones - ");
        setChipVisibility(holder.banos, vivienda.getBanos(), "Baños - ");
        setChipVisibility(holder.exteriorInterior, vivienda.getExteriorInterior(), "Orientacion - ");
        setChipVisibility(holder.companeros, vivienda.getCompaneros(), "Compañeros - ");
        setChipVisibility(holder.genero, vivienda.getGenero(), "Género - ");
        setChipVisibility(holder.tipoBano, vivienda.getTipoBano(), "Tipo Baño - ");

        String key = vivienda.getId();
        String vivKey = database.getReference("viviendas").child(String.valueOf(position)).getKey();
        System.out.println("VALOR DE KEY: " + vivKey);

        if (vivKey != null){ // se ha conseguido encontrar esa vivienda en la lista
            database.getReference("users").child(userId).child("listfavoritos").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // aquí se verifica si está en la lista de favs
                    boolean isFav = snapshot.hasChild(vivKey);

                    // esto es para descoenctar y poder actualizar el toggle button
                    holder.toggleFavorito.setOnCheckedChangeListener(null);
                    holder.toggleFavorito.setChecked(isFav);

                    holder.toggleFavorito.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked){// Agregar favoritos
                            database.getReference("users").child(userId).child("listfavoritos").child(vivKey).setValue(key);
                        }else{// Quitar de favs
                            database.getReference("users").child(userId).child("listfavoritos").child(vivKey).removeValue();
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    System.out.println("Error al comprobar favs");
                }
            });

        }


    }

    private void setImageNavigationViv (ViviendaViewHolder holder, Vivienda vivienda, int position){
        if (!vivienda.getImagenesUri().isEmpty()){
            holder.imagenesUri = new ArrayList<>(vivienda.getImagenesUri());
            holder.imagenViewVivienda.setImageURI(holder.imagenesUri.get(holder.imagenActualIndex));
            holder.left.setVisibility(holder.imagenActualIndex > 0 ? View.VISIBLE : View.INVISIBLE);
            holder.right.setVisibility(holder.imagenActualIndex < holder.imagenesUri.size()-1 ? View.VISIBLE : View.INVISIBLE);

            // imagen anterior y siguiente
            holder.left.setOnClickListener(v -> navigateImage(holder, -1));
            holder.right.setOnClickListener(v -> navigateImage(holder, 1));


        }
    }

    private void navigateImage (ViviendaViewHolder holder, int direction){
        int newIndex = holder.imagenActualIndex + direction;
        if (newIndex >= 0 && newIndex < holder.imagenesUri.size()){
            holder.imagenActualIndex = newIndex;
            holder.imagenViewVivienda.setImageURI(holder.imagenesUri.get(holder.imagenActualIndex));
            holder.left.setVisibility(holder.imagenActualIndex > 0 ? View.VISIBLE : View.INVISIBLE);
            holder.right.setVisibility(holder.imagenActualIndex < holder.imagenesUri.size() -1 ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return listViv.size();
    }

    public static class ViviendaViewHolder extends RecyclerView.ViewHolder {
        private TextView name, address, description, price;
        private TextView metr;
        private ToggleButton toggleFavorito;
        private Chip categoria, tipoCasa, habitaciones, banos, exteriorInterior, companeros, genero, tipoBano;
        private ImageView imagenViewVivienda;
        private List<Uri> imagenesUri = new ArrayList<>();
        private ImageButton left, right;
        private int imagenActualIndex = 0;

        public ViviendaViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            price = itemView.findViewById(R.id.price);
            description = itemView.findViewById(R.id.description);
            metr = itemView.findViewById(R.id.metr);
            toggleFavorito = itemView.findViewById(R.id.toggleButton);
            categoria = itemView.findViewById(R.id.chipCategoria);
            tipoCasa = itemView.findViewById(R.id.chipTipoCasa);
            habitaciones = itemView.findViewById(R.id.chipHabitaciones);
            banos = itemView.findViewById(R.id.chipBanos);
            exteriorInterior = itemView.findViewById(R.id.chipExtInt);
            companeros = itemView.findViewById(R.id.chipCompaneros);
            genero = itemView.findViewById(R.id.chipGenero);
            tipoBano = itemView.findViewById(R.id.chipTipoBano);
            imagenViewVivienda = itemView.findViewById(R.id.image_view_vivienda);
            left = itemView.findViewById(R.id.btn_prev_viv);
            right = itemView.findViewById(R.id.btn_next_viv);
            left.setVisibility(View.INVISIBLE);
            right.setVisibility(View.INVISIBLE);
        }
    }


    public void updateList (List<Vivienda> newVivs){
        this.listViv = newVivs;
        notifyDataSetChanged();
    }

    // Método auxiliar para cargar vista
    private void setChipVisibility (com.google.android.material.chip.Chip chip, String value, String prefix){
        if (value == null || value.trim().isEmpty()){
            chip.setVisibility(View.GONE);
        }else{
            chip.setVisibility(View.VISIBLE);
            chip.setText(prefix + value);
        }

    }
}
