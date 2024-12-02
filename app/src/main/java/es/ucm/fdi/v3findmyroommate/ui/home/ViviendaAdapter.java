package es.ucm.fdi.v3findmyroommate.ui.home;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.TranslationUtils;
import es.ucm.fdi.v3findmyroommate.ui.chats.Chat;
import es.ucm.fdi.v3findmyroommate.ui.chats.ChatActivity;
import es.ucm.fdi.v3findmyroommate.ui.chats.ChatFragment;
import es.ucm.fdi.v3findmyroommate.ui.viviendas.Anuncio;
import es.ucm.fdi.v3findmyroommate.ui.viviendas.AnuncioDetalleActivity;
import es.ucm.fdi.v3findmyroommate.ui.viviendas.AnunciosAdapter;
import es.ucm.fdi.v3findmyroommate.ui.viviendas.MisViviendasFragment;


public class ViviendaAdapter extends RecyclerView.Adapter<ViviendaAdapter.ViviendaViewHolder> {

    private List<Vivienda> listViv;
    private String userId;
    private FirebaseDatabase database;
    private Context context;
    private HomeFragment fragment;

    public ViviendaAdapter(Context context, List<Vivienda> lista, HomeFragment fragment){
        this.context = context;
        this.listViv = lista;
        this.fragment = fragment;
        this.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.database = FirebaseDatabase.getInstance(context.getString(R.string.database_url));
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

        //SAM-----------------------------------------------------------------------------------------------------
        holder.previewRect.setVisibility(View.VISIBLE);
//-------------------------------------------------------------------------------------------------------------------


        //Usuario
        String ownerName = vivienda.getOwnerName();
        if (ownerName != null) {
            holder.ownerName.setText("Contanctar dueño: " + ownerName);
            //Llamar al ChatActivity
            holder.ownerName.setOnClickListener(v -> {
                String ownerId = vivienda.getOwnerId();
                if (ownerId != null) {
                    openChatWithOwner(ownerId);
                } else {
                    Log.e("vivienda adapter", "No se puede contactar con el propietario");
                }
            });

        } else {
            holder.ownerName.setText("Dueño: Desconocido");
        }


        // El resto de atributos, como son chips que quiero mostrar y no mostrar dependiendo de la
        // categoría, pues los cargamos dependiendo de si son atributos de esa categoría o no, con
        // el siguiente método

        setChipVisibility(holder.categoria, TranslationUtils.reverseTranslateIfNeeded(vivienda.getCategoria()),
                context.getString(R.string.item_category_label));
        setChipVisibility(holder.tipoCasa, TranslationUtils.reverseTranslateIfNeeded(vivienda.getTipoCasa()),
                context.getString(R.string.item_house_type_label));
        setChipVisibility(holder.habitaciones, vivienda.getHabitaciones(),
                context.getString(R.string.item_num_rooms_label));
        setChipVisibility(holder.banos, vivienda.getBanos(),
                context.getString(R.string.item_num_bathrooms_label));
        setChipVisibility(holder.exteriorInterior, TranslationUtils.reverseTranslateIfNeeded(vivienda.getExteriorInterior()),
                context.getString(R.string.item_orientation_label));
        setChipVisibility(holder.companeros, vivienda.getCompaneros(),
                context.getString(R.string.item_num_roommates_label));
        setChipVisibility(holder.genero, TranslationUtils.reverseTranslateIfNeeded(vivienda.getGenero()),
                context.getString(R.string.item_roommates_gender_label));
        setChipVisibility(holder.tipoBano, TranslationUtils.reverseTranslateIfNeeded(vivienda.getTipoBano()),
                context.getString(R.string.item_bathroom_type_label));


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


        //SAM--------------------------------------------------------------------------------------------------------------------------
        // CONFIGURAR LA IMAGEN (CLICK) Y LA NAVEGACIÓN ENTRE ELLAS
        setImageNavigation(holder, vivienda);



    }




    //SAM---------------------------------------------------------------------------------------------------------------------------------------
    private void setImageNavigation(ViviendaAdapter.ViviendaViewHolder holder, Vivienda vivienda) {
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


    private void navigateImage(ViviendaAdapter.ViviendaViewHolder holder, int direction) {
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
        return listViv.size();
    }

    public static class ViviendaViewHolder extends RecyclerView.ViewHolder {
        private TextView name, address, description, price, ownerName;
        private TextView metr;
        private ToggleButton toggleFavorito;
        private Chip categoria, tipoCasa, habitaciones, banos, exteriorInterior, companeros, genero, tipoBano;


        //SAM-------------------------------------------------------------
        View previewRect;
        ImageView imageViewAnuncio;
         List<String> imagenesUri = new ArrayList<>();
         int imagenActualIndex = 0;
         ImageButton btnPrev, btnNext;
        //---------------------------------------------------------------------------


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
            ownerName = itemView.findViewById(R.id.owner_id);



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

    private void openChatWithOwner(String ownerId) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference("chats");

        // Buscar chat entre los usuarios
        chatsRef.orderByChild("participants/" + currentUserId).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String chatId = null;

                // Verificar si ya existe un chat con ambos usuarios
                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    if (chatSnapshot.child("participants").hasChild(ownerId)) {
                        chatId = chatSnapshot.getKey();
                        break;
                    }
                }

                if (chatId == null) {
                    // Crear un nuevo chat si no existe
                    chatId = chatsRef.push().getKey();
                    if (chatId != null) {
                        Map<String, Object> chatData = new HashMap<>();
                        chatData.put("lastMessage", "");
                        chatData.put("timestamp", System.currentTimeMillis());

                        Map<String, Boolean> participants = new HashMap<>();
                        participants.put(currentUserId, true);
                        participants.put(ownerId, true);

                        chatData.put("participants", participants);

                        chatsRef.child(chatId).setValue(chatData);

                        // Agregar el chat a la lista de chats de ambos usuarios
                        DatabaseReference userChatsRef = FirebaseDatabase.getInstance().getReference("users");
                        userChatsRef.child(currentUserId).child("chats").child(chatId).setValue(true);
                        userChatsRef.child(ownerId).child("chats").child(chatId).setValue(true);
                    }
                }

                // Abrir el fragmento de chat
                openChatFragment(chatId, ownerId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("viviendaAdapter ", "Error al abrir el chat");
            }
        });
    }


    private void openChatFragment(String chatId, String ownerId) {
        Chat chat = new Chat(chatId, null, null, "", System.currentTimeMillis());
        chat.setOtherUsername(ownerId);

        // Crea el ChatFragment y pasa los argumentos
        ChatFragment chatFragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("chat", chat);
        chatFragment.setArguments(bundle);

        // Usa el NavController para navegar
        if (context instanceof FragmentActivity) {
            NavController navController = Navigation.findNavController((FragmentActivity) context, R.id.nav_host_fragment_activity_lobby);
            navController.navigate(R.id.chatFragment, bundle);
        } else {
            Log.e("ViviendaAdapter", "Contexto no válido para abrir el fragmento");
        }
    }




}
