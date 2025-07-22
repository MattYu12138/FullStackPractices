package com.matt.wiki.controller;

import com.matt.wiki.domain.Test;
import com.matt.wiki.service.TestService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class TestController {

    @Value("${hello.test}")
    private String testH;

    @Resource
    private TestService testService;

    @GetMapping("/hello")
    public String hello(){
        return "hello world" + testH;
    }



    @PostMapping("/hello/post")
    public String helloPost(String name){
        return "hello world! Post, " + name + ".";
    }

    @GetMapping("/test/list")
    public List<Test> list(){
        return testService.list();
    }

}
