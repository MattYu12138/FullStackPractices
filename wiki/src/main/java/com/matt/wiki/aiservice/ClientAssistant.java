package com.matt.wiki.aiservice;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

import static com.matt.wiki.config.ChatConfig.QAQ_CHAT_BEAN;


@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, streamingChatModel = QAQ_CHAT_BEAN, tools = {"chatHistoryTools","clientTools"})
public interface ClientAssistant {


    @SystemMessage(fromResource = "/prompts/client.txt")
    @UserMessage("当前sessionId:{{sessionId}};当前用户消息:{{message}}")
    Flux<String> clientProperty(@V("sessionId") String sessionId, @V("message") String message);


}
