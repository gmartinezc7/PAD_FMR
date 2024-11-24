package es.ucm.fdi.v3findmyroommate.ui.chats;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import es.ucm.fdi.v3findmyroommate.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context;
    private ArrayList<Chat> chatList;
    private OnChatClickListener chatClickListener;

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    public ChatAdapter(Context context, ArrayList<Chat> chatList, OnChatClickListener listener) {
        this.context = context;
        this.chatList = chatList;
        this.chatClickListener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        Log.d("ChatAdapter", "currentUserId: " + currentUserId);

        Map<String, Object> participants = chat.getParticipantes();
        String otherUserId = null;
        for (String participantId : participants.keySet()) {
            if (!participantId.equals(currentUserId)) {
                otherUserId = participantId;
                Log.d("ChatAdapter", "otherUserId: " + otherUserId);
                break;
            }
        }

        if (otherUserId != null) {
            userRef.child(otherUserId).child("username").get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    String otherUserName = task.getResult().getValue(String.class);
                    Log.d("ChatAdapter", "otherUserName: " + otherUserName);
                    holder.chatUserId.setText(otherUserName != null ? otherUserName : "Usuario desconocido");
                } else {
                    Log.e("ChatAdapter", "Error al cargar el nombre del usuario: " + task.getException());
                    holder.chatUserId.setText("Error al cargar usuario");
                }
            });
        } else {
            holder.chatUserId.setText("Sin participantes");
        }


        //Último mensaje
        Message lastMessage = getLastMessage(chat.getMessages());
        holder.chatLastMessage.setText(lastMessage != null ? lastMessage.getText() : "Sin mensajes");

        //Timestamp
        long timestamp = lastMessage != null ? lastMessage.getTimestamp() : 0;
        String formattedTimestamp = formatTimestamp(timestamp);
        holder.chatTimestamp.setText(formattedTimestamp);

        //Click sobre el chat lleva al chat
        holder.itemView.setOnClickListener(v -> chatClickListener.onChatClick(chat));
    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    private Message getLastMessage(Map<String, Object> messages) {
        long latestTimestamp = Long.MIN_VALUE;
        Message lastMessage = null;

        for (Map.Entry<String, Object> entry : messages.entrySet()) {
            try {
                Map<String, Object> messageData = (Map<String, Object>) entry.getValue();

                String text = (String) messageData.get("text");
                long timestamp = ((Number) messageData.get("timestamp")).longValue();
                String senderID = (String) messageData.get("sender");

                Message message = new Message();
                message.setText(text);
                message.setTimestamp(timestamp);
                message.setSender(senderID);

                if (timestamp > latestTimestamp) {
                    latestTimestamp = timestamp;
                    lastMessage = message;
                }
            } catch (ClassCastException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return lastMessage;
    }

    private String formatTimestamp(long timestamp) {
        if (timestamp == 0L) {
            return "No date";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return dateFormat.format(new Date(timestamp));
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView chatUserId;
        TextView chatLastMessage;
        TextView chatTimestamp;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatUserId = itemView.findViewById(R.id.chatUserId);
            chatLastMessage = itemView.findViewById(R.id.chatLastMessage);
            chatTimestamp = itemView.findViewById(R.id.chatTimestamp);
        }
    }
}
