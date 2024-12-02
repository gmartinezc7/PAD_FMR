package es.ucm.fdi.v3findmyroommate.ui.chats;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.ucm.fdi.v3findmyroommate.R;
//import es.ucm.fdi.v3findmyroommate.ui.config.ConfigEditTextPreferencesFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private ArrayList<Chat> chatList;
    private DatabaseReference chatRef;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewChats);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        chatList = new ArrayList<>();
        chatAdapter = new ChatAdapter(getContext(), chatList, new ChatAdapter.OnChatClickListener() {
            @Override
            public void onChatClick(Chat chat) {
                ChatFragment chatFragment = new ChatFragment();
                Bundle bundle = new Bundle();

                //Detalles Chat
                bundle.putString("chatId", chat.getChatId());
                bundle.putSerializable("chat", chat);
                chatFragment.setArguments(bundle);

                // Ocultar RecyclerView y mostrar el contenedor del fragmento
                recyclerView.setVisibility(View.GONE);
                View container = getView().findViewById(R.id.chatFragmentContainer);
                if (container != null) {
                    container.setVisibility(View.VISIBLE);
                }

                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.chatFragmentContainer, chatFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        recyclerView.setAdapter(chatAdapter);

        chatRef = FirebaseDatabase.getInstance().getReference("chats");
        loadChatsFromFirebase();

        return root;
    }

    private void loadChatsFromFirebase() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("chats");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                        String chatId = chatSnapshot.getKey();
                        loadChatDetails(chatId);
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

    private void loadChatDetails(String chatId) {
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Procesar los datos del chat
                    Map<String, Object> chatData = (Map<String, Object>) dataSnapshot.getValue();
                    String lastMessage = (String) chatData.get("lastMessage");
                    Long timestamp = (Long) chatData.get("timestamp");
                    Map<String, Object> participants = (Map<String, Object>) chatData.get("participants");
                    Map<String, Object> messagesData = (Map<String, Object>) chatData.get("messages");

                    boolean hasUnreadMessages = false;

                    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    // Verificar si hay mensajes no leídos
                    for (Map.Entry<String, Object> entry : messagesData.entrySet()) {
                        String messageId = entry.getKey();
                        Map<String, Object> message = (Map<String, Object>) entry.getValue();

                        Boolean isSeen = (Boolean) message.get("visto");

                        String senderId = (String) message.get("senderId");

                        if (senderId != null && senderId.equals(currentUserId)) {
                            continue;
                        }

                        if (isSeen == null) {
                            // Si el campo "visto" es nulo, agregarlo a la base de datos como "true"
                            message.put("visto", true);
                            chatRef.child("messages").child(messageId).child("visto").setValue(true);
                            isSeen = true;
                        }

                        // Verificar si el mensaje no ha sido visto por otros
                        if (!isSeen) {
                            hasUnreadMessages = true;
                            break;
                        }
                    }

                    // Crear y añadir el chat a la lista
                    Chat chat = new Chat(chatId, messagesData, participants, lastMessage, timestamp);
                    assignUsernames(participants, chat);


                    // Enviar notificación si hay mensajes no leídos
                    if (hasUnreadMessages) {
                        for (String participantId : participants.keySet()) {
                            if (!participantId.equals(currentUserId)) {

                                sendUnreadMessagesNotification(chat);
                            }
                        }
                    }

                    chatList.add(chat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error al obtener detalles del chat: " + databaseError.getMessage());
            }
        });
    }


    private void assignUsernames(Map<String, Object> participants, Chat chat) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        AtomicInteger participantsLoaded = new AtomicInteger(0); // Usamos AtomicInteger para contar

        for (String participantId : participants.keySet()) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(participantId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String username = snapshot.child("username").getValue(String.class);
                        if (participantId.equals(currentUserId)) {
                            chat.setUsername(username);
                        } else {
                            chat.setOtherUsername(username);
                        }

                        // Incrementar el contador de participantes cargados
                        if (participantsLoaded.incrementAndGet() == participants.size()) {
                            // Solo enviamos la notificación cuando todos los participantes hayan sido cargados
                            chatAdapter.notifyDataSetChanged();
                            // Ahora podemos enviar la notificación
                            sendUnreadMessagesNotification(chat);  // Pasamos el chat completo
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseError", "Error al obtener el usuario: " + error.getMessage());
                }
            });
        }
    }

    private void sendUnreadMessagesNotification(Chat chat) {
        boolean hasUnreadMessages = false;
        for (Map.Entry<String, Object> entry : chat.getMessagesData().entrySet()) {
            Map<String, Object> message = (Map<String, Object>) entry.getValue();
            Boolean isSeen = (Boolean) message.get("visto");

            if (isSeen == null || !isSeen) {
                hasUnreadMessages = true;
                break;
            }
        }

        // Enviar notificación si hay mensajes no leídos
        if (hasUnreadMessages) {
            String chatId = chat.getChatId();
            String otherUsername = chat.getOtherUsername();

            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelId = "chat_notifications";
                    if (notificationManager.getNotificationChannel(channelId) == null) {
                        NotificationChannel channel = new NotificationChannel(
                                channelId,
                                "Mensajes no leídos",
                                NotificationManager.IMPORTANCE_HIGH
                        );
                        notificationManager.createNotificationChannel(channel);
                    }
                }
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "chat_notifications")
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentTitle("Nuevo mensaje de " + otherUsername)
                    .setContentText("Tienes mensajes no leídos en el chat con " + otherUsername)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            notificationManager.notify(chatId.hashCode(), builder.build());
        }
    }


    private void createNewChat(List<String> participantIds, String messageText) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String chatId = FirebaseDatabase.getInstance().getReference("chats").push().getKey();

        Map<String, Object> chatData = new HashMap<>();
        chatData.put("lastMessage", messageText);
        chatData.put("timestamp", System.currentTimeMillis());

        //Guardar el chat en Firebase
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId);
        chatRef.setValue(chatData);

        //Añadir el chat a cada usuario
        for (String participantId : participantIds) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(participantId).child("chats");
            userRef.child(chatId).setValue(true);
        }
    }


}
