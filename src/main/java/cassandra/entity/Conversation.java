package cassandra.entity;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Table("conversations")
public class Conversation {

    @PrimaryKey
    @Column("conversation_id")
    private UUID conversationId;
    
    @Column("conversation_name")
    private String conversationName;
    
    @Column("created_at")
    private Date createdAt;
    
    @Column("participants")
    private Set<String> participants;
    
    @Column("last_message_time")
    private Date lastMessageTime;

    public Conversation() {
    }

    public Conversation(UUID conversationId, String conversationName, Date createdAt, 
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
}
