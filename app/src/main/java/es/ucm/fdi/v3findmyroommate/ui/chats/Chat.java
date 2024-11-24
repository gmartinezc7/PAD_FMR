package es.ucm.fdi.v3findmyroommate.ui.chats;

import java.io.Serializable;
import java.util.Map;

public class Chat {
    private String chatId;
    private Map<String, Object> messages;
    private Map<String, Object> participantes;
    private String lastMessage;
    private long timestamp;

    public Chat() {}

    public Chat(String chatId, Map<String, Object> messages, Map<String, Object> participantes, String lastMessage, long timestamp) {
        this.chatId = chatId;
        this.messages = messages;
        this.participantes = participantes;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Map<String, Object> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, Object> messages) {
        this.messages = messages;
    }

    public Map<String, Object> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(Map<String, Object> participantes) {
        this.participantes = participantes;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Serializable getParticipants() { return (Serializable) participantes; }
}
