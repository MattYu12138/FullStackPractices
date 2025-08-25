package com.matt.wiki.aiservice;

import com.matt.wiki.aioutput.IntentionOutput;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatModel = "qwenChatModel",
        streamingChatModel = "qwenStreamingChatModel", chatMemoryProvider = "chatMemoryProvider", tools = "testUtil")
@SystemMessage(fromResource = "prompts/getIntention.txt")
public interface AiIntentionAssistant {
    @UserMessage("当前sessionId:{{sessionId}}; 用户当前消息:{{message}}")
    IntentionOutput intention(@V("sessionId") String sessionId, @V("message") String message);

}
