package com.matt.wiki.controller;


import com.matt.wiki.aiservice.AiAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class AiController {


    @Autowired
    private AiAssistant aiAssistant;

    @GetMapping("/chat")
    public String chat(
            @RequestParam(value = "message", defaultValue = "Hello") String message,
            @RequestParam(value = "userId", defaultValue = "111") String userId) {
        return aiAssistant.chat(userId, message);
    }

    @GetMapping(value = "/chat-stream", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    public Flux<String> chatStream(
            @RequestParam(value = "message", defaultValue = "Hello") String message,
            @RequestParam(value = "userId", defaultValue = "111") String userId) {
        return aiAssistant.chatStream(userId, message);
    }
}
