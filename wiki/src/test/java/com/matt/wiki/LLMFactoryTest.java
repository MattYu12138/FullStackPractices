package com.matt.wiki;

import com.matt.wiki.factory.LLMFactory;
import com.matt.wiki.factory.service.LLMService;
import org.junit.jupiter.api.Test;

public class LLMFactoryTest {

    @Test
    public void testOpenAiFactoryReturnsService() {
        LLMService llmService = LLMFactory.createLLMService(LLMFactory.modelType.OPENAI);
        String result = llmService.chat("hello");
        System.out.println(result);
    }

    @Test
    public void testQwenFactoryWithoutApiKey() {
        LLMService llmService = LLMFactory.createLLMService(LLMFactory.modelType.QWEN);
        String result = llmService.chat("hello");
        System.out.println(result);
    }
}
