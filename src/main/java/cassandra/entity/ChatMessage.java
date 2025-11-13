package cassandra.entity;

import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Table("chat_messages")
public class ChatMessage implements Serializable {

    @PrimaryKeyColumn(name = "conversation_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID conversationId;

    @PrimaryKeyColumn(name = "message_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private UUID messageId;

    @org.springframework.data.cassandra.mapping.Column("sender_id")
    private String senderId;
    
    @org.springframework.data.cassandra.mapping.Column("sender_name")
    private String senderName;
    
    @org.springframework.data.cassandra.mapping.Column("message_text")
    private String messageText;
    
    @org.springframework.data.cassandra.mapping.Column("created_at")
    private Date createdAt;
    
    @org.springframework.data.cassandra.mapping.Column("is_read")
    private Boolean isRead;

    public ChatMessage() {
    }

    public ChatMessage(UUID conversationId, UUID messageId, String senderId, String senderName,
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
