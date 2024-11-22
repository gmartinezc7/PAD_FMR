package es.ucm.fdi.v3findmyroommate.ui.chats;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import es.ucm.fdi.v3findmyroommate.R;
import com.google.firebase.auth.FirebaseAuth;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context context;
    private ArrayList<Message> messageList;
    private String currentUserId;  // To store the logged-in user's ID

    public MessageAdapter(Context context, ArrayList<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        String senderId = message.getSender();  // Get the sender's ID from the message
        Log.d("MessageChatAdapter", "senderId: " + senderId);

        // Check if the sender is the logged-in user
        if (senderId.equals(currentUserId)) {
            // If the sender is the logged-in user, show "Tú"
            holder.messageSender.setText("Tú");
        } else {
            // Otherwise, get the username of the other user
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
            userRef.child(senderId).child("username").get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    String otherUserName = task.getResult().getValue(String.class);
                    Log.d("MessageChatAdapter", "otherUserName: " + otherUserName);
                    holder.messageSender.setText(otherUserName != null ? otherUserName : "Usuario desconocido");
                } else {
                    Log.e("MessageAdapter", "Error al cargar el nombre del usuario: " + task.getException());
                    holder.messageSender.setText("Error al cargar usuario");
                }
            });
        }

        // Set the message text
        holder.messageText.setText(message.getText());

        // Format and display the timestamp
        String formattedDate = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(message.getTimestamp()));
        holder.messageTimestamp.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageSender, messageText, messageTimestamp;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageSender = itemView.findViewById(R.id.messageSender);
            messageText = itemView.findViewById(R.id.messageText);
            messageTimestamp = itemView.findViewById(R.id.messageTimestamp);
        }
    }
}

