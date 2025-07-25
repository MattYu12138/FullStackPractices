package com.matt.wiki.controller;

import com.matt.wiki.req.EbookQueryReq;
import com.matt.wiki.req.EbookSaveReq;
import com.matt.wiki.resp.CommonResp;
import com.matt.wiki.resp.EbookQueryResp;
import com.matt.wiki.resp.PageResp;
import com.matt.wiki.service.EbookService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/ebook")
@RestController
public class EbookController {
    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(EbookQueryReq ebookQueryReq){
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(ebookQueryReq);
        resp.setContent(list);
        return resp;

    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq ebookSaveReq){
        CommonResp resp = new CommonResp<>();
        ebookService.save(ebookSaveReq);
        return resp;

    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable("id") Long id){
        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }

}
