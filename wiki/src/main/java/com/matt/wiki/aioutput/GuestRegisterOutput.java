package com.matt.wiki.aioutput;


import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

@Data
public class GuestRegisterOutput {

    @Description("LLM output to Guest")
    private String output;

    @Description("用户姓名")
    private String UserName;

    @Description("手机号码")
    private String phone;

    @Description("邮箱")
    private String email;

    @Description("案件类型")
    private String caseType;

    @Description("地区")
    private String area;

    @Description("语言")
    private String language;

    @Description("紧急程度")
    private String urgency;

    @Description("是否完成登记")
    private String completed;

}
