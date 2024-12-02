package es.ucm.fdi.v3findmyroommate.ui.chats;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.ucm.fdi.v3findmyroommate.R;

public class ChatActivity extends AppCompatActivity {

    private String chatId;
    private String ownerUserId;
    private Chat chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (getIntent() != null) {
            chatId = getIntent().getStringExtra("chatId");
            ownerUserId = getIntent().getStringExtra("ownerUserId");
        }

        if (chatId == null) {
            Toast.makeText(this, "No se pudo cargar el chat", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadChatDetails(chatId);
    }

    private void loadChatDetails(String chatId) {
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    openChatFragment(chat);
                } else {
                    Toast.makeText(ChatActivity.this, "Chat no encontrado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, "Error al cargar el chat", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void openChatFragment(Chat chat) {
        ChatFragment chatFragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("chatId", chatId);
        bundle.putSerializable("chat", chat);
        chatFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.chatFragmentContainer, chatFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}


