package es.ucm.fdi.v3findmyroommate.ui.chats;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.ucm.fdi.v3findmyroommate.R;

public class ChatDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageList;
    private DatabaseReference messagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar lista de mensajes y adaptador
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);
        recyclerView.setAdapter(messageAdapter);

        // Referencia a Firebase para leer todos los mensajes

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://findmyroommate-86cbe-default-rtdb.europe-west1.firebasedatabase.app");
        messagesRef = database.getReference("chats");

        //messagesRef = FirebaseDatabase.getInstance().getReference("chats");

        // Leer mensajes de Firebase

        readAllMessagesFromFirebase();
    }

    private void readAllMessagesFromFirebase() {
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("FirebaseConnection", "Conexión establecida con Firebase.");

                messageList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                        DataSnapshot messagesSnapshot = chatSnapshot.child("messages");
                        for (DataSnapshot messageSnapshot : messagesSnapshot.getChildren()) {
                            Message message = messageSnapshot.getValue(Message.class);
                            if (message != null) {
                                messageList.add(message);
                                Log.d("FirebaseMessage", "Mensaje obtenido: " + message.getText());
                            }
                        }
                    }
                } else {
                    Log.d("FirebaseData", "No se encontraron mensajes en la base de datos.");
                    Toast.makeText(ChatDetailActivity.this, "No hay mensajes para mostrar", Toast.LENGTH_SHORT).show();
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error al leer los mensajes: " + databaseError.getMessage());
                Toast.makeText(ChatDetailActivity.this, "Error al cargar los mensajes", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
