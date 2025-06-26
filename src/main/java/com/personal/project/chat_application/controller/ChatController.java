package com.personal.project.chat_application.controller;
import com.personal.project.chat_application.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    /**
     * Handles sending chat messages.
     * Messages sent to "/app/chat.sendMessage" will be processed by this method.
     * The return value of this method is sent to subscribers of "/topic/public".
     *
     * @param chatMessage The message payload, automatically mapped to ChatMessage object.
     * @return The ChatMessage object to be sent to the public topic.
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        logger.info("Received message: Type={}, Content='{}', Sender='{}'",
                chatMessage.getType(), chatMessage.getContent(), chatMessage.getSender());

        chatMessage.setTimestamp(Instant.now());

        return chatMessage;
    }

    /**
     * Handles users joining the chat.
     * Messages sent to "/app/chat.addUser" will be processed by this method.
     * This method adds the sender's username to the WebSocket session attributes
     * and sends a "JOIN" message to the public topic.
     *
     * @param chatMessage The message payload, which should contain the sender's username.
     * @param headerAccessor Used to access and modify STOMP message headers, specifically session attributes.
     * @return The ChatMessage object to be sent to the public topic, indicating a user has joined.
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        logger.info("User joined: Sender='{}'", chatMessage.getSender());

        if (headerAccessor.getSessionAttributes() != null) {
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        }

        chatMessage.setType(ChatMessage.MessageType.JOIN);
        chatMessage.setTimestamp(Instant.now());
        chatMessage.setContent(chatMessage.getSender() + " has joined the chat!");

        return chatMessage;
    }
}
