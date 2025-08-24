package com.matt.wiki;


import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.output.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class LLMTest {

    @MockBean
    private RedisTemplate redisTemplate;
//
//    @Autowired
//    private OpenAiChatModel openAiChatModel;
//
//    @Test
//    public void testDeepSeekViaOpenAiCompatibleApi() {
//        String answer = openAiChatModel.chat("你是谁？");
//        System.out.println(answer);
//    }




    @Autowired
    private QwenChatModel qwenChatModel;

    @Test
    public void testDashScope(){
        String chat = qwenChatModel.chat("你是谁？");
        System.out.println(chat);
    }


    @Test
    public void testDashScopeWanx(){
        WanxImageModel wanxImageModel = WanxImageModel.builder()
                .modelName("wanx2.1-t2i-plus")
                .apiKey("sk-86c0a5ecbf284c0b9b58da54a394fcf2")
                .build();
        Response<Image> response = wanxImageModel.generate("美女");
        System.out.println(response.content().url());

    }
}
