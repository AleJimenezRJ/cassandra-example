package cassandra.dto;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class ConversationDTO {

    private UUID conversationId;
    private String conversationName;
    private Date createdAt;
    private Set<String> participants;
    private Date lastMessageTime;
    private Integer messageCount;

    public ConversationDTO() {
    }

    public ConversationDTO(UUID conversationId, String conversationName, Date createdAt,
                          Set<String> participants, Date lastMessageTime) {
        this.conversationId = conversationId;
        this.conversationName = conversationName;
        this.createdAt = createdAt;
        this.participants = participants;
        this.lastMessageTime = lastMessageTime;
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Set<String> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<String> participants) {
        this.participants = participants;
    }

    public Date getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Date lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }
}
