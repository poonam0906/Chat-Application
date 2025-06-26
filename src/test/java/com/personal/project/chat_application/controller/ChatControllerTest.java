package com.personal.project.chat_application.controller;

import com.personal.project.chat_application.model.ChatMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.HashMap;
import java.util.Map;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class ChatControllerTest {

    @InjectMocks
    private ChatController chatController;

    @Mock
    private SimpMessageHeaderAccessor headerAccessor;

    @BeforeEach
    void setUp() {
        Map<String, Object> sessionAttributes = new HashMap<>();
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);
    }

    @Test
    void testSendMessage() {
        ChatMessage inputMessage = new ChatMessage(
                ChatMessage.MessageType.CHAT,
                "Hello from test!",
                "TestSender"
        );

        ChatMessage returnedMessage = chatController.sendMessage(inputMessage);

        assertNotNull(returnedMessage);
        assertEquals(inputMessage.getType(), returnedMessage.getType());
        assertEquals(inputMessage.getContent(), returnedMessage.getContent());
        assertEquals(inputMessage.getSender(), returnedMessage.getSender());
        assertNotNull(returnedMessage.getTimestamp());
        assertTrue(returnedMessage.getTimestamp().isAfter(Instant.now().minusSeconds(1)));
        assertTrue(returnedMessage.getTimestamp().isBefore(Instant.now().plusSeconds(1)));
    }

    @Test
    void testAddUser() {
        ChatMessage inputMessage = new ChatMessage(
                ChatMessage.MessageType.JOIN,
                null,
                "NewUser"
        );

        ChatMessage returnedMessage = chatController.addUser(inputMessage, headerAccessor);

        assertNotNull(returnedMessage);
        assertEquals(ChatMessage.MessageType.JOIN, returnedMessage.getType());
        assertEquals("NewUser", returnedMessage.getSender());
        assertEquals("NewUser has joined the chat!", returnedMessage.getContent());
        assertNotNull(returnedMessage.getTimestamp());
        assertTrue(returnedMessage.getTimestamp().isAfter(Instant.now().minusSeconds(1)));
        assertTrue(returnedMessage.getTimestamp().isBefore(Instant.now().plusSeconds(1)));

        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        assertNotNull(sessionAttributes);
        assertEquals("NewUser", sessionAttributes.get("username"));

        verify(headerAccessor, times(3)).getSessionAttributes();
    }

    @Test
    void testAddUserWithNullSessionAttributes() {
        when(headerAccessor.getSessionAttributes()).thenReturn(null);

        ChatMessage inputMessage = new ChatMessage(
                ChatMessage.MessageType.JOIN,
                null,
                "UserNoSession"
        );

        ChatMessage returnedMessage = chatController.addUser(inputMessage, headerAccessor);

        assertNotNull(returnedMessage);
        assertEquals(ChatMessage.MessageType.JOIN, returnedMessage.getType());
        assertEquals("UserNoSession", returnedMessage.getSender());
        assertEquals("UserNoSession has joined the chat!", returnedMessage.getContent());
        assertNotNull(returnedMessage.getTimestamp());

        verify(headerAccessor, times(1)).getSessionAttributes();
    }
}

