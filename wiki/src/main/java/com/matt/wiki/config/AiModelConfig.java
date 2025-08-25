package com.matt.wiki.config;

import com.matt.wiki.util.TestUtil;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiModelConfig {

    @Bean
    public TestUtil testUtil() {
        return new TestUtil();
    }

    @Bean(name = "qwenChatModel")
    public ChatLanguageModel qwenChatModel(ChatLanguageModel chatLanguageModel) {
        return chatLanguageModel;
    }

    @Bean(name = "qwenStreamingChatModel")
    public StreamingChatLanguageModel qwenStreamingChatModel(
            StreamingChatLanguageModel streamingChatLanguageModel) {
        return streamingChatLanguageModel;
    }
}

