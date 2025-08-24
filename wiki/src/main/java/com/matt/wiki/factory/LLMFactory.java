package com.matt.wiki.factory;

import com.matt.wiki.factory.service.LLMService;
import com.matt.wiki.factory.service.OpenAiLLMService;
import com.matt.wiki.factory.service.QwenLLMService;

public class LLMFactory {

    public enum modelType{
        OPENAI, QWEN
    }

    public static LLMService createLLMService(modelType type){
        return switch (type){
            case QWEN -> new QwenLLMService();
            case OPENAI -> new OpenAiLLMService();
        };
    }
}
