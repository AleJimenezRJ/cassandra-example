package cassandra.controller;

import cassandra.dto.ChatMessageDTO;
import cassandra.dto.ConversationDTO;
import cassandra.service.ChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@Api(value = "Chat System", description = "Operations for managing conversations and messages")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @ApiOperation(value = "Get all conversations", 
                  notes = "Retrieves a list of all conversations in the system",
                  response = ConversationDTO.class, 
                  responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of conversations"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationDTO>> getAllConversations() {
        List<ConversationDTO> conversations = chatService.getAllConversations();
        return new ResponseEntity<>(conversations, HttpStatus.OK);
    }

    @ApiOperation(value = "Get conversation by ID", 
                  notes = "Retrieves details of a specific conversation",
                  response = ConversationDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved conversation"),
            @ApiResponse(code = 404, message = "Conversation not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/conversations/{conversationId}")
    public ResponseEntity<ConversationDTO> getConversationById(
            @ApiParam(value = "Conversation ID", required = true, example = "11111111-1111-1111-1111-111111111111")
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

    @ApiOperation(value = "Get all messages from a conversation", 
                  notes = "Retrieves all messages from a specific conversation (wide column demo - can return thousands of messages)",
                  response = ChatMessageDTO.class, 
                  responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved messages"),
            @ApiResponse(code = 400, message = "Invalid conversation ID format"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(
            @ApiParam(value = "Conversation ID", required = true, example = "11111111-1111-1111-1111-111111111111")
            @PathVariable String conversationId) {
        
        try {
            UUID uuid = UUID.fromString(conversationId);
            List<ChatMessageDTO> messages = chatService.getMessagesByConversationId(uuid);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Get latest N messages from a conversation", 
                  notes = "Retrieves the most recent messages from a conversation with pagination support",
                  response = ChatMessageDTO.class, 
                  responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved messages"),
            @ApiResponse(code = 400, message = "Invalid conversation ID format or limit value"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/conversations/{conversationId}/messages/latest")
    public ResponseEntity<List<ChatMessageDTO>> getLatestMessages(
            @ApiParam(value = "Conversation ID", required = true, example = "11111111-1111-1111-1111-111111111111")
            @PathVariable String conversationId,
            
            @ApiParam(value = "Number of messages to retrieve", required = false, defaultValue = "50")
            @RequestParam(defaultValue = "50") int limit) {
        
        try {
            UUID uuid = UUID.fromString(conversationId);
            List<ChatMessageDTO> messages = chatService.getLatestMessages(uuid, limit);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Health check", 
                  notes = "Simple endpoint to verify the chat API is running")
    @ApiResponse(code = 200, message = "API is healthy")
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("Chat API is running!", HttpStatus.OK);
    }
}
