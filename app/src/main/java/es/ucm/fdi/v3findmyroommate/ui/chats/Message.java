package es.ucm.fdi.v3findmyroommate.ui.chats;

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
        this.participantes = participantes;
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
}
