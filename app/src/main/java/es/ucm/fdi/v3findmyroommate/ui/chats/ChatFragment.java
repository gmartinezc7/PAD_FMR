package es.ucm.fdi.v3findmyroommate.ui.chats;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import es.ucm.fdi.v3findmyroommate.R;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageList;
    private String chatId;
    private DatabaseReference chatMessagesRef;
    private EditText messageEditText;
    private ImageView sendMessageButton;

    private String username;
    private String otherUsername;
    private String currentUserId;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        // Obtener el chat pasado como argumento
        Bundle args = getArguments();
        if (args != null) {
            Chat chat = (Chat) args.getSerializable("chat");
            if (chat != null) {
                chatId = chat.getChatId();
                username = chat.getUsername();
                otherUsername = chat.getOtherUsername();
            }
        }

        // Si no se ha cargado el chat correctamente
        if (chatId == null) {
            Toast.makeText(getContext(), "No se pudo cargar el chat", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
            return root;
        }

        // Configuración de la vista
        recyclerView = root.findViewById(R.id.recyclerViewMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(getContext(), messageList, username, otherUsername, currentUserId);
        recyclerView.setAdapter(messageAdapter);

        messageEditText = root.findViewById(R.id.inputMessage);
        sendMessageButton = root.findViewById(R.id.buttonSend);

        // Referencia a los mensajes del chat
        chatMessagesRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId).child("messages");
        loadMessages();

        // Enviar mensaje cuando el botón es presionado
        sendMessageButton.setOnClickListener(v -> {
            String messageText = messageEditText.getText().toString().trim();
            if (!messageText.isEmpty()) {
                sendMessage(chatId, messageText);
                messageEditText.setText(""); // Limpiar el campo de mensaje
            } else {
                Toast.makeText(getContext(), "Escribe un mensaje", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    // Cargar los mensajes del chat
    private void loadMessages() {
        chatMessagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.clear();  // Limpiar los mensajes previos
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    if (message != null) {
                        messageList.add(message);  // Añadir los nuevos mensajes
                    }
                }
                messageAdapter.notifyDataSetChanged();  // Actualizar el adaptador
                recyclerView.scrollToPosition(messageList.size() - 1);  // Desplazar al último mensaje
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error cargando los mensajes: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(String chatId, String messageText) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        long timestamp = System.currentTimeMillis();

        // Crear el mensaje
        Message newMessage = new Message(String.valueOf(timestamp), currentUserId, messageText, timestamp);
        chatMessagesRef.child(String.valueOf(timestamp)).setValue(newMessage)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Mensaje enviado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Error al enviar el mensaje", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
