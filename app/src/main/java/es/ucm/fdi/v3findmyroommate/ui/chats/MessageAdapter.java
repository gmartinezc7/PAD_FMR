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

    public MessageAdapter(Context context, ArrayList<Message> messageList, String currentUserName, String otherUserName, String currentUserId) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserName = currentUserName;
        this.otherUserName = otherUserName;
        this.currentUserId = currentUserId;
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
        String senderId = message.getSenderId();

        if (senderId != null && senderId.equals(currentUserId)) {
            holder.messageSender.setVisibility(View.VISIBLE);
            holder.messageContainerOther.setVisibility(View.GONE);

            holder.messageSenderName.setText(currentUserName != null ? currentUserName : context.getString(R.string.you));
            holder.messageText.setText(message.getText() != null ? message.getText() : context.getString(R.string.message_no_message));

            String formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault()).format(new Date(message.getTimestamp()));
            holder.messageTimestamp.setText(formattedDate);
            holder.messageTimestamp.setGravity(Gravity.END);
        } else {
            holder.messageSender.setVisibility(View.GONE);
            holder.messageContainerOther.setVisibility(View.VISIBLE);

            holder.messageOtherName.setText(otherUserName != null ? otherUserName : context.getString(R.string.unknown_user));
            holder.messageTextOther.setText(message.getText() != null ? message.getText() : context.getString(R.string.message_no_message));

            String formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault()).format(new Date(message.getTimestamp()));
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
