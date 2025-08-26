package com.matt.wiki.util;


import com.matt.wiki.entity.ClientEntity;
import com.matt.wiki.repository.ClientRepository;
import com.matt.wiki.service.CategoryService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ClientTools {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private ClientRepository clientRepository;

    @Tool("根据手机号查询客户登记信息")
    public List<ClientEntity> queryClientByPhone(@P(value = "手机号码") String phone){
        LOG.info("根据手机号查询物品登记信息，手机号：{}",phone);

        return clientRepository.findAllByPhone(phone);
    }

}
