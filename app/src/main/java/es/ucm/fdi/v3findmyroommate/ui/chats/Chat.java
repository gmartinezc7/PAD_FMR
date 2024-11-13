package es.ucm.fdi.v3findmyroommate.ui.chats;

public class Chat {
    private String chatId;
    private String userName;
    private String lastMessage;
    private long timestamp;

    public Chat() {    }

    public Chat(String chatId, String userName, String lastMessage, long timestamp) {
        this.chatId = chatId;
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
