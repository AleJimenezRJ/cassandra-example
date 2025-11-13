package cassandra.repository;

import cassandra.entity.ChatMessage;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends CrudRepository<ChatMessage, UUID> {

    @Query("SELECT * FROM chat_messages WHERE conversation_id=?0 LIMIT ?1")
    List<ChatMessage> findByConversationId(UUID conversationId, int limit);
    
    @Query("SELECT * FROM chat_messages WHERE conversation_id=?0")
    List<ChatMessage> findAllByConversationId(UUID conversationId);
}
