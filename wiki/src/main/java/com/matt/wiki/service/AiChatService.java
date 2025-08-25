package com.matt.wiki.service;

import reactor.core.publisher.Flux;

public interface AiChatService {
    /**
     * 针对用户意图进行流程编排
     * @param userId
     * @param message
     */
    Flux<String> chatStream(String userId, String message);
}
