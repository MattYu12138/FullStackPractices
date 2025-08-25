package com.matt.wiki.aiservice;


import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatModel = "qwenChatModel",
        streamingChatModel = "qwenStreamingChatModel", chatMemoryProvider = "chatMemoryProvider", tools = "testUtil")
public interface AiAssistant {

    String chat(@MemoryId  String id, @UserMessage String message);

    Flux<String> chatStream(@MemoryId String id, @UserMessage String message);

}
