package com.matt.wiki.service;

import com.matt.wiki.aop.ChatFlow;

public interface AiChatService {
    /**
     * 针对用户意图进行流程编排
     * @param userId
     * @param message
     */
    @ChatFlow
    String chatStream(String userId, String message);
}
