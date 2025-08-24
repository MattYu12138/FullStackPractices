package com.matt.wiki.factory.service;

import dev.langchain4j.community.model.dashscope.QwenChatModel;

public class QwenLLMService implements LLMService {

    private final QwenChatModel qwenChatModel;
    private final boolean hasApiKey;

    public QwenLLMService() {
        String apiKey = System.getenv("DASHSCOPE_API_KEY");
        if (apiKey != null && !apiKey.isBlank()) {
            this.qwenChatModel = QwenChatModel.builder().apiKey(apiKey).build();
            this.hasApiKey = true;
        } else {
            this.qwenChatModel = null;
            this.hasApiKey = false;
        }
    }

    @Override
    public String chat(String prompt) {
        if (!hasApiKey) {
            return "DashScope API key not defined";
        }
        return qwenChatModel.chat(prompt);
    }
}
