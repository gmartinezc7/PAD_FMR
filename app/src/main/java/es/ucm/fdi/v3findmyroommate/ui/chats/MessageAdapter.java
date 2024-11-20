package es.ucm.fdi.v3findmyroommate.ui.chats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public MessageAdapter(Context context, ArrayList<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
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
        //TODO cuando se cambie al ver solo tus chat y no todos cambiarlo
        //Remitente
        holder.messageSender.setText(String.valueOf(message.getSenderId()));
        //Texto
        holder.messageText.setText(message.getText());
        //Fecha
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
