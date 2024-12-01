package es.ucm.fdi.v3findmyroommate.ui.chats;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;
public class Message {
    private String messageId;
    private String senderId;
    private String text;
    private long timestamp;
    private Map<String, Object> participantes;

    public Message() {}

    public Message(String messageId, String senderId, String text, long timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
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
            message.setSenderId((String) snapshot.child("sender").getValue());
        }
        return message;
    }
}
