package es.ucm.fdi.v3findmyroommate.ui.chats;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.ucm.fdi.v3findmyroommate.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context;
    private ArrayList<Chat> chatList;

    public ChatAdapter(Context context, ArrayList<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(ChatViewHolder holder, int position) {
//        Chat chat = chatList.get(position);
//        holder.userNameTextView.setText(chat.getUserName());
//        holder.lastMessageTextView.setText(chat.getLastMessage());
//    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.chatName.setText(chat.getChatId());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatDetailActivity.class);
            intent.putExtra("chatId", chat.getChatId());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public Message chatName;
        TextView userNameTextView;
        TextView lastMessageTextView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.chatUserName);
            lastMessageTextView = itemView.findViewById(R.id.chatLastMessage);
        }
    }
}
