package es.ucm.fdi.v3findmyroommate.ui.chats;

import android.os.Bundle;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_chat, container, false);

        //TODO hacer que el id venga de la sesion
        chatId = getArguments() != null ? getArguments().getString("chatId") : null;

        recyclerView = root.findViewById(R.id.recyclerViewMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(getContext(), messageList);
        recyclerView.setAdapter(messageAdapter);

        messageEditText = root.findViewById(R.id.editTextMessage);
        sendMessageButton = root.findViewById(R.id.buttonSendMessage);

        //Obtener los mensajes
        if (chatId != null) {
            chatMessagesRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId).child("messages");
            loadMessages();
        }else{
            Toast.makeText(getContext(), "No se pudo cargar el chat", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
            return root;
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
//
//        String messageText = messageEditText.getText().toString().trim();
//        if (!messageText.isEmpty()) {
//            long timestamp = System.currentTimeMillis();
//            //TODO poner el id del usuario que esta usando la aplicacion
//            //String senderId = "user_id";
//            int senderId = 99999;
//            int reciverID = 100000;
//            Message newMessage = new Message(senderId, reciverID, messageText, timestamp);
//
//            Map<String, Object> messageValues = new HashMap<>();
//            messageValues.put(String.valueOf(timestamp), newMessage);
//            chatMessagesRef.updateChildren(messageValues).addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    messageEditText.setText("");
//                } else {
//                    Toast.makeText(getContext(), "Error enviando el mensaje", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            Toast.makeText(getContext(), "El mensaje no puede estar vacío", Toast.LENGTH_SHORT).show();
//        }
    }
}
