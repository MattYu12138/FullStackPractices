package com.matt.wiki.util;


import com.matt.wiki.domain.ChatHistory;
import com.matt.wiki.entity.ChatHistoryEntity;
import com.matt.wiki.repository.ChatHistoryRepository;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ChatHistoryTools {

    @Resource
    private ChatHistoryRepository chatHistoryRepository;

    @Tool("获取用户聊天历史对话")
    public List<ChatHistory> getChatHistory(@P("sessionId") String sessionId){


        List<ChatHistory> result = new ArrayList<>();
        List<ChatHistoryEntity> entityList = chatHistoryRepository.findByTop20SessionIdOderByIdDesc(sessionId);

        for(ChatHistoryEntity entity: entityList){
            ChatHistory history = new ChatHistory();
            history.setRole(entity.getRole());
            history.setContent(entity.getContent());
            result.add(history);
        }

        return result;
    }
}
