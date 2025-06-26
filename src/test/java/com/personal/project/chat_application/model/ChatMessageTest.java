package com.personal.project.chat_application.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ChatMessageTest {

    @Test
    void testNoArgsConstructor() {
        ChatMessage message = new ChatMessage();

        assertNotNull(message);
        assertNotNull(message.getTimestamp());
        assertTrue(message.getTimestamp().isAfter(Instant.now().minus(1, ChronoUnit.SECONDS)));
        assertTrue(message.getTimestamp().isBefore(Instant.now().plus(1, ChronoUnit.SECONDS)));
    }

    @Test
    void testAllArgsConstructor() {
        ChatMessage.MessageType type = ChatMessage.MessageType.CHAT;
        String content = "Hello, world!";
        String sender = "TestUser";
        Instant specificTimestamp = Instant.now().minus(5, ChronoUnit.MINUTES); // A specific past timestamp

        ChatMessage message = new ChatMessage(type, content, sender, specificTimestamp);

        assertEquals(type, message.getType());
        assertEquals(content, message.getContent());
        assertEquals(sender, message.getSender());
        assertEquals(specificTimestamp, message.getTimestamp());
    }

    @Test
    void testConstructorWithTypeContentSender() {
        ChatMessage.MessageType type = ChatMessage.MessageType.JOIN;
        String content = "User has joined!";
        String sender = "NewUser";

        ChatMessage message = new ChatMessage(type, content, sender);

        assertEquals(type, message.getType());
        assertEquals(content, message.getContent());
        assertEquals(sender, message.getSender());
        assertNotNull(message.getTimestamp());
        assertTrue(message.getTimestamp().isAfter(Instant.now().minus(1, ChronoUnit.SECONDS)));
        assertTrue(message.getTimestamp().isBefore(Instant.now().plus(1, ChronoUnit.SECONDS)));
    }

    @Test
    void testGettersAndSetters() {
        ChatMessage message = new ChatMessage();

        message.setType(ChatMessage.MessageType.LEAVE);
        message.setContent("User has left.");
        message.setSender("OldUser");
        Instant customTimestamp = Instant.parse("2023-01-01T10:00:00Z");
        message.setTimestamp(customTimestamp);

        assertEquals(ChatMessage.MessageType.LEAVE, message.getType());
        assertEquals("User has left.", message.getContent());
        assertEquals("OldUser", message.getSender());
        assertEquals(customTimestamp, message.getTimestamp());
    }

    @Test
    void testToString() {
        ChatMessage message = new ChatMessage(ChatMessage.MessageType.CHAT, "Test message", "Tester", Instant.EPOCH);
        String expectedToString = "ChatMessage(type=CHAT, content=Test message, sender=Tester, timestamp=1970-01-01T00:00:00Z)";
        assertEquals(expectedToString, message.toString());
    }
}
