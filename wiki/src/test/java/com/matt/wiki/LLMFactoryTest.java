package com.matt.wiki;

import com.matt.wiki.factory.LLMFactory;
import com.matt.wiki.factory.service.LLMService;
import com.matt.wiki.factory.service.OpenAiLLMService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LLMFactoryTest {

    @Test
    public void testOpenAiFactoryReturnsService() {
        LLMService llmService = LLMFactory.createLLMService(LLMFactory.modelType.OPENAI);
        assertNotNull(llmService);
        assertTrue(llmService instanceof OpenAiLLMService);
    }

    @Test
    public void testQwenFactoryWithoutApiKey() {
        LLMService llmService = LLMFactory.createLLMService(LLMFactory.modelType.QWEN);
        assertNotNull(llmService);
        String result = llmService.chat("hello");
        assertEquals("DashScope API key not defined", result);
    }
}
