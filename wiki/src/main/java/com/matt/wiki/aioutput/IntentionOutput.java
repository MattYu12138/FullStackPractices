package com.matt.wiki.aioutput;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * AI注册意图输出结果，由于AI返回的JSON中可能会包含其他字段，为了避免解析失败，
 * 将未知字段记录为忽略。
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class IntentionOutput {

    @Description("意图分析 1.预约 2.费用 3.进度查询 4.FAQ 5.其他")
    private Integer intention;


    @Description("LLM output to Guest")
    private String output;
}
