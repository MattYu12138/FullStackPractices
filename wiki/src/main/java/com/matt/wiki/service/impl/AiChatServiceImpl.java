package com.matt.wiki.service.impl;

import com.matt.wiki.aioutput.GuestRegisterOutput;
import com.matt.wiki.aioutput.IntentionOutput;
import com.matt.wiki.aiservice.AiAssistant;
import com.matt.wiki.entity.GuestRegisterEntity;
import com.matt.wiki.repository.GuestRegisterRepository;
import com.matt.wiki.service.AiChatService;
import com.matt.wiki.service.CategoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class AiChatServiceImpl implements AiChatService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private AiAssistant aiAssistant;

//    @Resource
//    private AiIntentionAssistant aiIntentionAssistant;

    @Resource
    private GuestRegisterRepository guestRegisterRepository;

    @Override
    public Flux<String> chatStream(String userId, String message) {

//        用户意图
        IntentionOutput intention = aiAssistant.intention(userId, message);
        LOG.info("----" + intention);
        Integer intent = intention.getIntention();
        if (intent == null) {
            return Flux.just(intention.getOutput());
        }
        String output = intention.getOutput();
        switch (intent){
            case 1 :
//                    1.预约
                output = guestRegister(userId,message);
                break;
            case 2 :
//                    2.费用
                break;
            case 3 :
//                    3.进度查询
                break;
            case 4 :
//                    4.FAQ
                break;
            case 5 :
//                    5.其他"
                break;
            default :
                return Flux.just(intention.getOutput());
        }
        return Flux.just(output);
    }

    private String guestRegister(String userId, String message){
        GuestRegisterOutput guestRegisterOutput = aiAssistant.guestRegister(userId,message);
        LOG.info("----" + guestRegisterOutput);
        String completed = guestRegisterOutput.getCompleted();
        if ("yes".equalsIgnoreCase(completed) || "true".equalsIgnoreCase(completed)) {
            // 持久化层数据库
            GuestRegisterEntity entity = new GuestRegisterEntity();
            BeanUtils.copyProperties(guestRegisterOutput, entity);
            entity.setCompleted(Boolean.TRUE);
            guestRegisterRepository.save(entity);

        }
        return guestRegisterOutput.getOutput();
    }
}
