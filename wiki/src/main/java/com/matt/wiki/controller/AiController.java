package com.matt.wiki.controller;


import com.matt.wiki.aiservice.AiAssistant;
import com.matt.wiki.service.AiChatService;
import com.matt.wiki.vo.ChatHistoryVo;
import com.matt.wiki.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class AiController {


    @Autowired
    private AiAssistant aiAssistant;

    @Autowired
    private AiChatService aiChatService;

    @Autowired
    private SnowFlake snowFlake;

//    @GetMapping("/chat")
//    public String chat(
//            @RequestParam(value = "message", defaultValue = "Hello") String message,
//            @RequestParam(value = "userId", required = false) String userId) {
//        if (userId == null || userId.isEmpty()) {
//            userId = String.valueOf(snowFlake.nextId());
//        }
//        return aiAssistant.chat(userId, message);
//    }

    @GetMapping(value = "/chat-stream", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    public Flux<String> chatStream(
            @RequestParam(value = "message", defaultValue = "Hello") String message,
            @RequestParam(value = "userId", required = false) String userId) {
        if (userId == null || userId.isEmpty()) {
            userId = String.valueOf(snowFlake.nextId());
        }
        return Flux.just(aiChatService.chatStream(userId, message));
    }

    @GetMapping(value = "/chat-history")
    public PagedModel<ChatHistoryVo> queryChatHistory(@RequestParam(value = "userId", required = false) String userId,
                                                      @PageableDefault(direction = Sort.Direction.DESC, sort = "id") Pageable page) {
        if (userId == null || userId.isEmpty()) {
            userId = String.valueOf(snowFlake.nextId());
        }
        return new PagedModel<>(aiChatService.queryChatHistory(userId, page));
    }

    @PostMapping(value = "/clear-chat-history/{userId}")
    public void clearChatHistory(@PathVariable("userId") String userId) {
        aiChatService.clearChatHistory(userId);
    }

//    @GetMapping(value = "/embedding-index")
//    public String embeddingIndex(){
//        return aiChatService.embeddingIndex();
//    }
//    @GetMapping(value = "/embedding-query")
//    public List<String> embeddingQuery(String message){
//        return aiChatService.embeddingQuery(message);
//    }

}
