package com.matt.wiki.util;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class TestUtil {

    @Tool("Get User Class")
    public String getUserClass(String userName){
        System.out.println(userName);
        return null;
    }

    @Tool("Get Today's weather")
    public String getWeather(String address){
        System.out.println(address);
        return "good day";
    }


}
