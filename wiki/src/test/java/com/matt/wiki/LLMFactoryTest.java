package com.matt.wiki;

import com.matt.wiki.factory.LLMFactory;
import com.matt.wiki.factory.service.LLMService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class LLMFactoryTest {

    @Autowired
    private LLMFactory llmFactory;

    @Test
    public void testOpenAiFactoryReturnsService() {
        LLMService llmService = llmFactory.createLLMService(LLMFactory.modelType.OPENAI);
        String result = llmService.chat("hello");
        System.out.println(result);
    }

    @Test
    public void testQwenFactoryWithoutApiKey() {
        LLMService llmService = llmFactory.createLLMService(LLMFactory.modelType.QWEN);
        String result = llmService.chat("hello");
        System.out.println(result);
    }
}
