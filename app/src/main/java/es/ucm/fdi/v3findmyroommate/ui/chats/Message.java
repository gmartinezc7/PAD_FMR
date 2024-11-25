package es.ucm.fdi.v3findmyroommate.ui.chats;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;
public class Message {
    private String messageId;
    private String sender;
    private String text;
    private long timestamp;
    private Map<String, Object> participantes;

    public Message() {}

    public Message(String messageId, String sender, String text, long timestamp) {
        this.messageId = messageId;
        this.sender = sender;
        this.text = text;
        this.timestamp = timestamp;
        this.participantes = new HashMap<>();
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String senderId) {
        this.sender = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(Map<String, Object> participantes) {
        this.participantes = participantes;
    }

    public static Message fromDataSnapshot(DataSnapshot snapshot) {
        Message message = snapshot.getValue(Message.class);
        if (message != null) {
            message.setSender((String) snapshot.child("sender").getValue());
        }
        return message;
    }

    public String getSenderId() {return sender;}
}
