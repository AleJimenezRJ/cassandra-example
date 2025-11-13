package cassandra.service.impl;

import cassandra.dto.ChatMessageDTO;
import cassandra.dto.ConversationDTO;
import cassandra.entity.ChatMessage;
import cassandra.entity.Conversation;
import cassandra.repository.ChatMessageRepository;
import cassandra.repository.ConversationRepository;
import cassandra.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChatServiceImpl implements ChatService {

    private final ConversationRepository conversationRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatServiceImpl(ConversationRepository conversationRepository,
                          ChatMessageRepository chatMessageRepository) {
        this.conversationRepository = conversationRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public List<ConversationDTO> getAllConversations() {
        List<ConversationDTO> conversationDTOs = new ArrayList<>();
        Iterable<Conversation> conversations = conversationRepository.findAll();
        
        for (Conversation conversation : conversations) {
            ConversationDTO dto = new ConversationDTO(
                    conversation.getConversationId(),
                    conversation.getConversationName(),
                    conversation.getCreatedAt(),
                    conversation.getParticipants(),
                    conversation.getLastMessageTime()
            );
            conversationDTOs.add(dto);
        }
        
        return conversationDTOs;
    }

    @Override
    public ConversationDTO getConversationById(UUID conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId).orElse(null);
        
        if (conversation == null) {
            return null;
        }
        
        return new ConversationDTO(
                conversation.getConversationId(),
                conversation.getConversationName(),
                conversation.getCreatedAt(),
                conversation.getParticipants(),
                conversation.getLastMessageTime()
        );
    }

    @Override
    public List<ChatMessageDTO> getMessagesByConversationId(UUID conversationId) {
        List<ChatMessage> messages = chatMessageRepository.findAllByConversationId(conversationId);
        return convertToMessageDTOs(messages);
    }

    @Override
    public List<ChatMessageDTO> getLatestMessages(UUID conversationId, int limit) {
        List<ChatMessage> messages = chatMessageRepository.findByConversationId(conversationId, limit);
        return convertToMessageDTOs(messages);
    }

    private List<ChatMessageDTO> convertToMessageDTOs(List<ChatMessage> messages) {
        List<ChatMessageDTO> messageDTOs = new ArrayList<>();
        
        for (ChatMessage message : messages) {
            ChatMessageDTO dto = new ChatMessageDTO(
                    message.getConversationId(),
                    message.getMessageId(),
                    message.getSenderId(),
                    message.getSenderName(),
                    message.getMessageText(),
                    message.getCreatedAt(),
                    message.getIsRead()
            );
            messageDTOs.add(dto);
        }
        
        return messageDTOs;
    }
}
