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

    //TODO cuando funcione refactorizar para guardar en una varible globar el id del usuario
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        Bundle args = getArguments();
        if (args != null) {
            chatId = args.getString("chatId");
        }

        recyclerView = root.findViewById(R.id.recyclerViewMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(getContext(), messageList);
        recyclerView.setAdapter(messageAdapter);

        messageEditText = root.findViewById(R.id.inputMessage);
        sendMessageButton = root.findViewById(R.id.buttonSend);

        if (chatId != null) {
            chatMessagesRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId).child("messages");
            loadMessages();
        } else {
            Toast.makeText(getContext(), "No se pudo cargar el chat", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        }

        sendMessageButton.setOnClickListener(v -> sendMessage());

        return root;
    }

    private void loadMessages() {
        chatMessagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    Log.e("ChatFragment", "senderId: " + message.getSender());
                    Log.e("ChatFragment", "Timestamp: " + message.getTimestamp());
                    Log.e("ChatFragment", "text: " + message.getText());
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

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (!messageText.isEmpty()) {
            long timestamp = System.currentTimeMillis();

            mAuth = FirebaseAuth.getInstance();
            String senderId = mAuth.getCurrentUser().getUid();

            chatMessagesRef.getParent().child("participants").get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    Map<String, Boolean> participants = (Map<String, Boolean>) task.getResult().getValue();
                    if (participants != null) {
                        String receiverId = null;
                        for (String participant : participants.keySet()) {
                            if (!participant.equals(senderId)) {
                                receiverId = participant;
                                break;
                            }
                        }

                        if (receiverId != null) {
                            Message newMessage = new Message(String.valueOf(timestamp), senderId, messageText, timestamp);

                            //Guardar el mensaje en firebase
                            chatMessagesRef.child(String.valueOf(timestamp))
                                    .setValue(newMessage)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            messageEditText.setText("");

                                            chatMessagesRef.getParent().updateChildren(Map.of(
                                                    "lastMessage", messageText,
                                                    "timestamp", timestamp
                                            ));
                                        } else {
                                            Toast.makeText(getContext(), "Error enviando el mensaje", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "No se pudo identificar el receptor", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Error obteniendo los participantes", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "El mensaje no puede estar vacío", Toast.LENGTH_SHORT).show();
        }
    }



}
