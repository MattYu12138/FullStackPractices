package com.matt.wiki.aioutput;


import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

@Data
public class IntentionOutput {

    @Description("意图分析 1.预约 2.费用 3.进度查询 4.FAQ 5.其他")
    private Integer intention;


    @Description("LLM output to Guest")
    private String output;
}
