package com.example.loginregister.service;

import com.example.loginregister.model.ChatMessage;
import com.example.loginregister.model.User;
import com.example.loginregister.repository.ChatMessageRepository;
import com.example.loginregister.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public ChatService(ChatMessageRepository chatMessageRepository,
                       UserRepository userRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }

    /** Last 50 messages, oldest first (so the chat reads top → bottom). */

    public List<ChatMessage> getRecentMessages() {
        List<ChatMessage> newestFirst = chatMessageRepository.findTop50ByOrderByIdDesc();
        List<ChatMessage> oldestFirst = new ArrayList<>(newestFirst);
        Collections.reverse(oldestFirst);
        return oldestFirst;
    }

    public void sendMessage(String content, Long senderId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalStateException("Sender not found: " + senderId));

        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setContent(content.trim());
        chatMessageRepository.save(message);
    }
}