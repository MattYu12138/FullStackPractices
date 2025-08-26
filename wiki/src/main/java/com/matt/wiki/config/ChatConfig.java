package com.matt.wiki.config;

import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ChatConfig {

    public static final String QAQ_CHAT_BEAN = "qwenQaQStreamingChatModel";

    @Bean(QAQ_CHAT_BEAN)
    public QwenStreamingChatModel qwenStreamingChatModel() {
        return QwenStreamingChatModel.builder()
                .apiKey("sk-86c0a5ecbf284c0b9b58da54a394fcf2")
                .modelName("qwq-32b")
                .build();
    }

}
