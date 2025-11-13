package cassandra.controller;

import cassandra.dto.ChatMessageDTO;
import cassandra.dto.ConversationDTO;
import cassandra.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat System", description = "Operations for managing conversations and messages in a distributed Cassandra cluster")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @Operation(summary = "Get all conversations", 
               description = "Retrieves a list of all conversations in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of conversations"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationDTO>> getAllConversations() {
        List<ConversationDTO> conversations = chatService.getAllConversations();
        return new ResponseEntity<>(conversations, HttpStatus.OK);
    }

    @Operation(summary = "Get conversation by ID", 
               description = "Retrieves details of a specific conversation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved conversation"),
            @ApiResponse(responseCode = "404", description = "Conversation not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/conversations/{conversationId}")
    public ResponseEntity<ConversationDTO> getConversationById(
            @Parameter(description = "Conversation ID", required = true, example = "11111111-1111-1111-1111-111111111111")
            @PathVariable String conversationId) {
        
        try {
            UUID uuid = UUID.fromString(conversationId);
            ConversationDTO conversation = chatService.getConversationById(uuid);
            
            if (conversation == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            return new ResponseEntity<>(conversation, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get all messages from a conversation", 
               description = "Retrieves all messages from a specific conversation (wide column demo - can return thousands of messages)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved messages"),
            @ApiResponse(responseCode = "400", description = "Invalid conversation ID format"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(
            @Parameter(description = "Conversation ID", required = true, example = "11111111-1111-1111-1111-111111111111")
            @PathVariable String conversationId) {
        
        try {
            UUID uuid = UUID.fromString(conversationId);
            List<ChatMessageDTO> messages = chatService.getMessagesByConversationId(uuid);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get latest N messages from a conversation", 
               description = "Retrieves the most recent messages from a conversation with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved messages"),
            @ApiResponse(responseCode = "400", description = "Invalid conversation ID format or limit value"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/conversations/{conversationId}/messages/latest")
    public ResponseEntity<List<ChatMessageDTO>> getLatestMessages(
            @Parameter(description = "Conversation ID", required = true, example = "11111111-1111-1111-1111-111111111111")
            @PathVariable String conversationId,
            
            @Parameter(description = "Number of messages to retrieve")
            @RequestParam(defaultValue = "50") int limit) {
        
        try {
            UUID uuid = UUID.fromString(conversationId);
            List<ChatMessageDTO> messages = chatService.getLatestMessages(uuid, limit);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Health check", 
               description = "Simple endpoint to verify the chat API is running")
    @ApiResponse(responseCode = "200", description = "API is healthy")
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("Chat API is running!", HttpStatus.OK);
    }
}
