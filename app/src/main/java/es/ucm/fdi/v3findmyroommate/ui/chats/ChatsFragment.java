package es.ucm.fdi.v3findmyroommate.ui.chats;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.ucm.fdi.v3findmyroommate.R;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private ArrayList<Chat> chatList;
    private DatabaseReference chatRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chats, container, false);

        // Inicializar RecyclerView y configurar LayoutManager
        recyclerView = root.findViewById(R.id.recyclerViewChats);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializar lista de chats y el adaptador
        chatList = new ArrayList<>();
        chatAdapter = new ChatAdapter(getContext(), chatList);
        recyclerView.setAdapter(chatAdapter);

        // Inicializar referencia de Firebase
        chatRef = FirebaseDatabase.getInstance().getReference("chats");

        // Cargar datos de Firebase
        loadChatsFromFirebase();

        return root;
    }

    private void loadChatsFromFirebase() {
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                    Chat chat = chatSnapshot.getValue(Chat.class);
                    if (chat != null) {
                        chatList.add(chat);

                        // Iterar por mensajes del chat
                        for (DataSnapshot messageSnapshot : chatSnapshot.child("messages").getChildren()) {
                            Message message = messageSnapshot.getValue(Message.class);
                            if (message != null) {
                                Log.d("FirebaseMessage", "Mensaje cargado: " + message.getText());
                            }else{
                                Log.d("FirebaseMessage","No hay mensaje");
                            }
                        }
                    }
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error al cargar los chats", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
