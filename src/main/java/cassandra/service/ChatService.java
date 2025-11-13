package cassandra.service;

import cassandra.dto.ChatMessageDTO;
import cassandra.dto.ConversationDTO;

import java.util.List;
import java.util.UUID;

public interface ChatService {

    /**
     * Get all conversations
     */
    List<ConversationDTO> getAllConversations();

    /**
     * Get a specific conversation by ID
     */
    ConversationDTO getConversationById(UUID conversationId);

    /**
     * Get all messages from a conversation
     */
    List<ChatMessageDTO> getMessagesByConversationId(UUID conversationId);

    /**
     * Get the latest N messages from a conversation
     */
    List<ChatMessageDTO> getLatestMessages(UUID conversationId, int limit);
}
