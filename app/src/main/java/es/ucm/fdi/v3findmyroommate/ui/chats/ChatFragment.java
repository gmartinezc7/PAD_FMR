package es.ucm.fdi.v3findmyroommate.ui.chats;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
                messageEditText.setText("");
            } else {
                Toast.makeText(getContext(), "Escribe un mensaje", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void loadMessages() {
        chatMessagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    if (message != null) {
                        messageList.add(message);
                    }
                }
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messageList.size() - 1);
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

        Message newMessage = new Message(String.valueOf(timestamp), currentUserId, messageText, timestamp);

        chatMessagesRef.child(String.valueOf(timestamp)).setValue(newMessage)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        updateChatLastMessage(chatId, messageText, timestamp);
                    } else {
                        Log.d("SendMessage", "Error al enviar el mensaje.");
                    }
                });
    }

    private void updateChatLastMessage(String chatId, String messageText, long timestamp) {
        Map<String, Object> chatUpdates = new HashMap<>();
        chatUpdates.put("lastMessage", messageText);
        chatUpdates.put("timestamp", timestamp);

        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId);
        chatRef.updateChildren(chatUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notifyRecipient(chatId, messageText);
                    }
                });
    }

    private void notifyRecipient(String chatId, String messageText) {
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId);
        chatRef.child("participants").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot participant : snapshot.getChildren()) {
                    String participantId = participant.getKey();
                    if (!participantId.equals(currentUserId)) {
                        sendLocalNotification(messageText);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("NotifyRecipient", "Error: " + error.getMessage());
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void sendLocalNotification(String message) {
        // Crear un canal de notificación para Android 8.0 y superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Chat Notifications";
            String description = "Notifications for new messages in chat.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("chat_channel", name, importance);
            channel.setDescription(description);

            // Obtener el NotificationManager y crear el canal
            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        // Crear la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "chat_channel")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp) // Usar un ícono apropiado
                .setContentTitle("Nuevo mensaje")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // Se cancela cuando se toca la notificación

        // Mostrar la notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(0, builder.build());
    }

}
