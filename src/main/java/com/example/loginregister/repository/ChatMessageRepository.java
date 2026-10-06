package com.example.loginregister.repository;

import com.example.loginregister.model.ChatMessage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * The 50 most recent messages, newest first.
     * The @EntityGraph tells Hibernate to load the sender together with each
     * message in the same query — so no lazy access happens later.
     */
    @EntityGraph(attributePaths = "sender")
    List<ChatMessage> findTop50ByOrderByIdDesc();
}