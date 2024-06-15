package com.example.project.chat.services;

import com.example.project.chat.data.Message;
import com.example.project.users.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> findBySenderOrReceiverAndTimestampAfter(
            User sender, User receiver, LocalDateTime timestamp);

    @Query(nativeQuery = true, value =
            "SELECT uu.user_id, m.* " +
                    "FROM (" +
                    "    SELECT DISTINCT COALESCE(sender_user_id, receiver_user_id) AS user_id " +
                    "    FROM messages" +
                    ") uu " +
                    "LEFT JOIN messages m ON " +
                    "    (uu.user_id = m.sender_user_id OR uu.user_id = m.receiver_user_id) " +
                    "    AND m.timestamp = (" +
                    "        SELECT MAX(sub.timestamp) " +
                    "        FROM messages sub " +
                    "        WHERE (sub.sender_user_id = uu.user_id OR sub.receiver_user_id = uu.user_id)" +
                    ")")
    List<Message> findLastMessagesForUsers();
}
