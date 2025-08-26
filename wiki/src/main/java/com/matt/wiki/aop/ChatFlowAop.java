package com.matt.wiki.aop;


import com.matt.wiki.entity.ChatHistoryEntity;
import com.matt.wiki.repository.ChatHistoryRepository;
import com.matt.wiki.service.CategoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
public class ChatFlowAop {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    private String aiRole = "1";

    private String userRole = "0";

    @Pointcut("@annotation(com.matt.wiki.aop.ChatFlow)")
    public void pointcut(){

    }

    @Around("pointcut()")
    public Object arount(ProceedingJoinPoint joinPoint) throws Throwable{
        Object[] args = joinPoint.getArgs();
        String sessionId = (String)args[0];
        String message = (String)args[1];
        LOG.info("-------sessionId:{}, message:{}",sessionId,message);
        saveChatHistory(sessionId,message, userRole);
        Object result = joinPoint.proceed();
        LOG.info("-------sessionId:{}, aiMessage:{}",sessionId,result);
        saveChatHistory(sessionId,result.toString(),aiRole);
        return result;
    }

    @Resource
    private ChatHistoryRepository chatHistoryRepository;


    private void saveChatHistory(String sessionId, String message, String userRole) {
        ChatHistoryEntity entity = new ChatHistoryEntity();
        entity.setSessionId(sessionId);
        entity.setRole(userRole);
        entity.setContent(message);
        chatHistoryRepository.save(entity);
    }
}
