package com.matt.wiki.factory.service;

import dev.langchain4j.model.openai.OpenAiChatModel;

public class OpenAiLLMService implements LLMService{

    private final OpenAiChatModel openAiChatModel;

    public OpenAiLLMService() {
        this.openAiChatModel = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .modelName("gpt-4o-mini").build();
    }

    @Override
    public String chat(String prompt) {
        return openAiChatModel.chat(prompt);
    }
}
