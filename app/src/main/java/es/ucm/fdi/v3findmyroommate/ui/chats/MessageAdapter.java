package es.ucm.fdi.v3findmyroommate.ui.chats;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

import es.ucm.fdi.v3findmyroommate.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context context;
    private ArrayList<Message> messageList;
    private String currentUserId;
    private String currentUserName;
    private String otherUserName;

    public MessageAdapter(Context context, ArrayList<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        fetchCurrentUserInfo();
    }

    private void fetchCurrentUserInfo() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    currentUserName = task.getResult().child("username").getValue(String.class);
                }
            }
        });
    }

    //TODO TEMPORAL
    private void fetchOtherUserInfo(String id) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(id);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    otherUserName = task.getResult().child("username").getValue(String.class);
                }
            }
        });
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
        String senderId = message.getSender();
        fetchOtherUserInfo(message.getSenderId());


        //Usuario Actual
        if (senderId.equals(currentUserId)) {
            holder.messageSender.setVisibility(View.VISIBLE);
            holder.messageContainerOther.setVisibility(View.GONE);

            holder.messageSenderName.setText(currentUserName != null ? currentUserName : "You");
            holder.messageText.setText(message.getText());

            String formattedDate = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(message.getTimestamp()));
            holder.messageTimestamp.setText(formattedDate);
            holder.messageTimestamp.setGravity(Gravity.END);
        } else {
            //Otro usuario
            holder.messageSender.setVisibility(View.GONE);
            holder.messageContainerOther.setVisibility(View.VISIBLE);

            holder.messageOtherName.setText(otherUserName != null ? message.getSenderId() : "Unknown");
            holder.messageTextOther.setText(message.getText());

            String formattedDate = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(message.getTimestamp()));
            holder.messageTimestampOther.setText(formattedDate);
            holder.messageTimestampOther.setGravity(Gravity.START);
        }
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout messageSender, messageContainerOther;
        TextView messageText, messageTextOther, messageTimestamp, messageTimestampOther, messageSenderName, messageOtherName;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageSender = itemView.findViewById(R.id.messageSender);
            messageSenderName = itemView.findViewById(R.id.messageSenderName);
            messageText = itemView.findViewById(R.id.messageText);
            messageTimestamp = itemView.findViewById(R.id.messageTimestamp);

            messageContainerOther = itemView.findViewById(R.id.messageContainerOther);
            messageOtherName = itemView.findViewById(R.id.messageOtherName);
            messageTextOther = itemView.findViewById(R.id.messageTextOther);
            messageTimestampOther = itemView.findViewById(R.id.messageTimestampOther);
        }
    }
}
