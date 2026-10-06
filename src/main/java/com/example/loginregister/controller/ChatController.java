package com.example.loginregister.controller;

import com.example.loginregister.model.ChatMessage;
import com.example.loginregister.service.ChatService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /* ------------------------------------------------------------------ */
    /*  CHAT PAGE                                                          */
    /* ------------------------------------------------------------------ */

    @GetMapping("/chat")
    public String chatPage(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", session.getAttribute("username"));
        return "chat";
    }

    /* ------------------------------------------------------------------ */
    /*  JSON ENDPOINT for polling                                          */
    /* ------------------------------------------------------------------ */

    /**
     * Returns the last 50 messages as JSON.
     * Only id, senderName, content and time are exposed — never the User entity itself,
     * so the password hash can never leak.
     */
    @GetMapping("/chat/messages")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> messages(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).build();
        }

        List<Map<String, Object>> payload = chatService.getRecentMessages().stream()
                .map(this::toJson)
                .toList();

        return ResponseEntity.ok(payload);
    }

    private Map<String, Object> toJson(ChatMessage message) {
        return Map.of(
                "id", message.getId(),
                "sender", message.getSender().getUsername(),
                "content", message.getContent(),
                "time", message.getCreatedAt().toString()
        );
    }

    /* ------------------------------------------------------------------ */
    /*  SEND                                                               */
    /* ------------------------------------------------------------------ */

    @PostMapping("/chat/send")
    @ResponseBody
    public ResponseEntity<Void> send(@RequestParam(name = "content", required = false) String content,
                                     HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        if (content == null || content.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (content.length() > 500) {
            return ResponseEntity.badRequest().build();
        }

        chatService.sendMessage(content, userId);
        return ResponseEntity.ok().build();
    }
}