package com.example.project.chat;

import com.example.project.chat.data.DTO.MessageDTO;
import com.example.project.chat.data.DTO.MessageModel;
import com.example.project.chat.data.notification;
import com.example.project.chat.services.MessageService;
import com.example.project.chat.services.notificationService;
import com.example.project.token.TokenServices;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.jav.exposerversdk.ExpoMessageSound;
import io.github.jav.exposerversdk.ExpoPushMessage;
import io.github.jav.exposerversdk.PushClient;
import io.github.jav.exposerversdk.PushClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import com.example.project.users.data.help.userRole;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MyWebSocketHandler implements WebSocketHandler {
    private final TokenServices tokenServices;
    private final MessageService messageService;
    private static final Map<String, List<WebSocketSession>> userSessionsMap = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final notificationService notificationService;
    @Autowired
    public MyWebSocketHandler(TokenServices tokenServices, MessageService messageService, com.example.project.chat.services.notificationService notificationService) {
        this.tokenServices = tokenServices;
        this.messageService = messageService;
        this.notificationService = notificationService;
        objectMapper.registerModule(new JavaTimeModule());
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = session.getUri().getQuery();
        String email = tokenServices.getMail(userId);
        String role = tokenServices.getRole(userId);
        if(role.equals(userRole.USER.toString())) {
            userSessionsMap.compute(email, (key, existingSessions) -> {
                if (existingSessions == null) {
                    existingSessions = new ArrayList<>();
                }
                boolean exist = false;
                for (int i = 0; i < existingSessions.size(); i++) {
                    if (existingSessions.get(i).getUri().equals(session.getUri())&&existingSessions.get(i).isOpen()) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    existingSessions.add(session);
                }
                return existingSessions;
            });
            TextMessage textMessage = new TextMessage(email);
            session.sendMessage(textMessage);
        }
        else {
            userSessionsMap.compute("Manager", (key, existingSessions) -> {
                if (existingSessions == null) {
                    existingSessions = new ArrayList<>();
                }
                boolean exist = false;
                for (int i = 0; i < existingSessions.size(); i++) {
                    if (existingSessions.get(i).getUri().equals(session.getUri())) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    existingSessions.add(session);
                }
                return existingSessions; // Return the updated list
            });
        }
//        String recipientToken = "ExponentPushToken[mW_1MIDSFX4fwjxovZjQ5Y]";
//
//        // Create a push message
//        ExpoPushMessage pushMessage = new ExpoPushMessage();
//        pushMessage.getTo().add(recipientToken);
//        pushMessage.setTitle("Cleanora");
//        pushMessage.setBody("You have received a new message.");
//        pushMessage.setSound(new ExpoMessageSound("default"));
//
//
//        try {
//            // Initialize the PushClient
//            PushClient pushClient = new PushClient();
//
//            // Send the push notification
//            pushClient.sendPushNotificationsAsync(List.of(pushMessage));
//            System.out.println("Push notification sent successfully.");
//        } catch (PushClientException e) {
//            System.err.println("Failed to send push notification: " + e.getMessage());
//        }


    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = (String) message.getPayload();

        MessageModel receivedMessage = objectMapper.readValue(payload, MessageModel.class);
        MessageDTO messageDTO = messageService.addNewMessage(receivedMessage);

        String messageJson = objectMapper.writeValueAsString(messageDTO);

        TextMessage textMessage = new TextMessage(messageJson);
        session.sendMessage(textMessage);

        if (receivedMessage.getTo() != null) {
            String receiverKey = receivedMessage.getTo();
            List<WebSocketSession> receiverSessions = userSessionsMap.get(receiverKey);

            if (receiverSessions != null) {
                for (int i = 0; i < receiverSessions.size(); i++) {
                    WebSocketSession session1 = receiverSessions.get(i);
                    if (session1.isOpen()) {
                        System.out.println("Send message to " + session1.getId());
                        session1.sendMessage(textMessage);
                    }
                }
            }

            List<notification> notifications = notificationService.getAllByEmail(receiverKey);
            List<ExpoPushMessage> messages = new ArrayList<>();
            for (notification notification:notifications){
                ExpoPushMessage pushMessage = new ExpoPushMessage();
                pushMessage.getTo().add(notification.getDeviceId());
                pushMessage.setTitle("Cleanora");
                pushMessage.setBody("You have received a new message.");
                pushMessage.setSound(new ExpoMessageSound("default"));
                messages.add(pushMessage);
            }
            try {
            PushClient pushClient = new PushClient();

            // Send the push notification
            pushClient.sendPushNotificationsAsync(messages);
            System.out.println("Push notification sent successfully.");
        } catch (PushClientException e) {
            System.err.println("Failed to send push notification: " + e.getMessage());
        }
        }
        else if(receivedMessage.getFrom()!=null) {
            List<WebSocketSession> managerSessions = userSessionsMap.get("Manager");
            if (managerSessions != null) {
                for (int i = 0; i < managerSessions.size(); i++) {
                    WebSocketSession session1 = managerSessions.get(i);
                    if (session1.isOpen()) {
                        System.out.println("send message to " + session1.getId());
                        session1.sendMessage(textMessage);
                    }
                }

            }
            List<notification> notifications = notificationService.getAllByEmail("Manager");
            List<ExpoPushMessage> messages = new ArrayList<>();
            for (notification notification:notifications){
                ExpoPushMessage pushMessage = new ExpoPushMessage();
                pushMessage.getTo().add(notification.getDeviceId());
                pushMessage.setTitle("Cleanora");
                pushMessage.setBody("You have received a new message.");
                pushMessage.setSound(new ExpoMessageSound("default"));
                messages.add(pushMessage);
            }
            try {
                PushClient pushClient = new PushClient();

                pushClient.sendPushNotificationsAsync(messages);
                System.out.println("Push notification sent successfully.");
            } catch (PushClientException e) {
                System.err.println("Failed to send push notification: " + e.getMessage());
            }
        }


    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // Handle transport errors (e.g., connection failures)
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        // This method is called when the WebSocket connection is closed.

        // Remove the session from the userSessionsMap
        String userId = session.getUri().getQuery();
        List<WebSocketSession> userSessions = userSessionsMap.get(userId);
        if (userSessions != null) {
            userSessions.remove(session);
            if (userSessions.isEmpty()) {
                // Remove the user entry if there are no more sessions
                userSessionsMap.remove(userId);
            }
        }

        System.out.println("WebSocket connection closed for user ID " + userId + " with session ID " + session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
