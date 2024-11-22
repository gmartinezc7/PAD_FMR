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

import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.ui.config.ConfigEditTextPreferencesFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private ArrayList<Chat> chatList;
    private DatabaseReference chatRef;

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
                bundle.putString("chatId", chat.getChatId());
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
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                        try {
                            //Chat
                            String chatId = chatSnapshot.getKey();
                            Map<String, Object> chatData = (Map<String, Object>) chatSnapshot.getValue();

                            //Participantes y mensajes
                            Map<String, Object> participants = (Map<String, Object>) chatData.get("participants");
                            Map<String, Object> messagesData = (Map<String, Object>) chatData.get("messages");
                            String lastMessage = (String) chatData.get("lastMessage");

                            Long timestampLong = (Long) chatData.get("timestamp");
                            long timestamp = timestampLong != null ? timestampLong : System.currentTimeMillis();

                            //Chat
                            Chat chat = new Chat(chatId, messagesData, participants, lastMessage, timestamp);

                            //Ultimo mensaje
                            if (messagesData != null) {
                                for (Map.Entry<String, Object> entry : messagesData.entrySet()) {
                                    Map<String, Object> messageMap = (Map<String, Object>) entry.getValue();
                                    Message message = convertToMessage(messageMap);
                                    if (message != null && (lastMessage == null || message.getTimestamp() > chat.getTimestamp())) {
                                        chat.setLastMessage(message.getText());
                                        chat.setTimestamp(message.getTimestamp());
                                    }
                                }
                            }

                            chatList.add(chat);
                        } catch (Exception e) {
                            Log.e("FirebaseError", "Error al convertir el chat: " + e.getMessage());
                        }
                    }
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error al cargar los chats", Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", "Error al obtener los chats: " + databaseError.getMessage());
            }
        });
    }


    private Message convertToMessage(Map<String, Object> messageData) {
        try {
            String text = (String) messageData.get("text");
            Long timestampLong = (Long) messageData.get("timestamp");
            String senderId = String.valueOf(messageData.get("sender"));

            long timestamp = timestampLong != null ? timestampLong : System.currentTimeMillis();

            if (text == null || senderId == null) {
                return null;
            }

            return new Message("generated_message_id", senderId, text, timestamp);
        } catch (Exception e) {
            Log.e("FirebaseError", "Error al convertir el mensaje: " + e.getMessage());
            return null;
        }
    }

    //TODO Ya no hace falta este metodo
    private Message getLastMessage(Map<String, Object> messages) {
        long latestTimestamp = Long.MIN_VALUE;
        Message lastMessage = null;

        for (Map.Entry<String, Object> entry : messages.entrySet()) {
            Message message = (Message) entry.getValue();
            if (message != null && message.getTimestamp() > latestTimestamp) {
                latestTimestamp = message.getTimestamp();
                lastMessage = message;
            }
        }
        return lastMessage;
    }
}
