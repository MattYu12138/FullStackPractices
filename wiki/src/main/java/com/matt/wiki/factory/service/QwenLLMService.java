package com.matt.wiki.factory.service;

import dev.langchain4j.community.model.dashscope.QwenChatModel;

public class QwenLLMService implements LLMService {


    private final QwenChatModel qwenChatModel;

    public QwenLLMService() {
        this.qwenChatModel = QwenChatModel.builder().build();
    }

    @Override
    public String chat(String prompt) {
        return qwenChatModel.chat(prompt);
    }
}
