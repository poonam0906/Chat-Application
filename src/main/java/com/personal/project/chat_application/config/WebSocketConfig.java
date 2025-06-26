package com.personal.project.chat_application.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Registers STOMP endpoints that the clients will use to connect to our WebSocket server.
     *
     * @param registry StompEndpointRegistry used to register STOMP endpoints
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    /**
     * Configures the message broker options.
     *
     * @param registry MessageBrokerRegistry used to configure the message broker
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Configure an in-memory message broker.
        // Messages whose destination starts with "/topic" will be routed to the message broker.
        // This means clients can subscribe to "/topic/public" to receive public chat messages.
        registry.enableSimpleBroker("/topic");

        // Set the application destination prefix.
        // Messages whose destination starts with "/app" will be routed to `@MessageMapping`
        // annotated methods in our controllers. For example, a message sent to "/app/chat.sendMessage"
        // will be handled by the sendMessage method in ChatController.
        registry.setApplicationDestinationPrefixes("/app");
    }
}
