package com.matt.wiki.aiservice;


import com.matt.wiki.aioutput.GuestRegisterOutput;
import com.matt.wiki.aioutput.IntentionOutput;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatModel = "qwenChatModel",
        streamingChatModel = "qwenStreamingChatModel", tools = "chatHistoryTools")
public interface AiAssistant {

//    String chat(@MemoryId  String id, @UserMessage String message);
//
//    Flux<String> chatStream(@MemoryId String id, @UserMessage String message);

    @SystemMessage(fromResource = "/prompts/guestRegister.txt")
    @UserMessage("当前sessionId:{{sessionId}}; 用户当前消息:{{message}}")
    GuestRegisterOutput guestRegister(@V("sessionId") String sessionId,@V("message") String message);

    @SystemMessage(fromResource = "prompts/getIntention.txt")
    @UserMessage("当前sessionId:{{sessionId}}; 用户当前消息:{{message}}")
    IntentionOutput intention(@V("sessionId") String sessionId, @V("message") String message);


}
