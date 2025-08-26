package com.matt.wiki.service;

import com.matt.wiki.aop.ChatFlow;
import com.matt.wiki.vo.ChatHistoryVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AiChatService {
    /**
     * 针对用户意图进行流程编排
     * @param userId
     * @param message
     */
    @ChatFlow
    String chatStream(String userId, String message);

    /**
     * 查询聊天历史记录
     * @param userId
     * @param page
     * @return
     */
    Page<ChatHistoryVo> queryChatHistory(String userId, Pageable page);

    /**
     * 删除聊天历史记录
     * @param userId
     */
    void clearChatHistory(String userId);

//    public String embeddingIndex();
//
//    List<String> embeddingQuery(String message);

}
