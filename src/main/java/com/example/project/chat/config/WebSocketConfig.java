package com.example.project.chat.config;

import com.example.project.chat.MyWebSocketHandler;
import com.example.project.chat.services.MessageService;
import com.example.project.chat.services.notificationService;
import com.example.project.token.TokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final TokenServices tokenServices;
    private final MessageService messageService;
    private final notificationService notificationService;
@Autowired
    public WebSocketConfig(TokenServices tokenServices, MessageService messageService, com.example.project.chat.services.notificationService notificationService) {
        this.tokenServices = tokenServices;
        this.messageService = messageService;
        this.notificationService = notificationService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWebSocketHandler(tokenServices,messageService,notificationService), "/websocket/chat"); // Define the WebSocket path
    }


}
