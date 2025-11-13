package cassandra.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.UUID;

public class ChatMessageDTO {

    private UUID conversationId;
    private UUID messageId;
    private String senderId;
    private String senderName;
    private String messageText;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date createdAt;
    
    private Boolean isRead;

    public ChatMessageDTO() {
    }

    public ChatMessageDTO(UUID conversationId, UUID messageId, String senderId, String senderName,
                         String messageText, Date createdAt, Boolean isRead) {
        this.conversationId = conversationId;
        this.messageId = messageId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.messageText = messageText;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
}
