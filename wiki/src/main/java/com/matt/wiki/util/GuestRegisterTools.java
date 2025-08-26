package com.matt.wiki.util;

import com.matt.wiki.entity.GuestRegisterEntity;
import com.matt.wiki.repository.GuestRegisterRepository;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class GuestRegisterTools {

    @Autowired
    private GuestRegisterRepository guestRegisterRepository;

    @Tool("根据手机号码查询预约信息")
    public List<GuestRegisterEntity> queryLostRegisterByPhone(@P("用户手机号") String phone){
        log.info("根据手机号码查询预约信息,手机号：{}",phone);
        return guestRegisterRepository.findAllByPhone(phone);
    }

}
