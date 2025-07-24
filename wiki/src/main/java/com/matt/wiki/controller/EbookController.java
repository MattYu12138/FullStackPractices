package com.matt.wiki.controller;

import com.matt.wiki.req.EbookReq;
import com.matt.wiki.resp.CommonResp;
import com.matt.wiki.resp.EbookResp;
import com.matt.wiki.resp.PageResp;
import com.matt.wiki.service.EbookService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/ebook")
@RestController
public class EbookController {
    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(EbookReq ebookReq){
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        PageResp<EbookResp> list = ebookService.list(ebookReq);
        resp.setContent(list);
        return resp;

    }

}
