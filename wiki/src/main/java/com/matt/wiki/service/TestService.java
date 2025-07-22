package com.matt.wiki.service;


import com.matt.wiki.domain.Test;
import com.matt.wiki.mapper.TestMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Resource
    private TestMapper testMapper;

    public List<Test> list(){
        return testMapper.list();
    }

}
