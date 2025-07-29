package com.matt.wiki.controller;

import com.matt.wiki.req.DocQueryReq;
import com.matt.wiki.req.DocSaveReq;
import com.matt.wiki.resp.DocQueryResp;
import com.matt.wiki.resp.CommonResp;
import com.matt.wiki.resp.PageResp;
import com.matt.wiki.service.DocService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RequestMapping("/doc")
@RestController
public class DocController {
    @Resource
    private DocService docService;

    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq docQueryReq){
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(docQueryReq);
        resp.setContent(list);
        return resp;

    }


    @GetMapping("/all/{ebookId}")
    public CommonResp all(@PathVariable Long ebookId){
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all(ebookId);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid DocSaveReq docSaveReq){
        CommonResp resp = new CommonResp<>();
        docService.save(docSaveReq);
        return resp;
    }

    @DeleteMapping("/delete/{idsStr}")
    public CommonResp delete(@PathVariable String idsStr){
        CommonResp resp = new CommonResp<>();
        List<String> list = Arrays.asList(idsStr.split(","));
        docService.delete(list);
        return resp;
    }

    @GetMapping("/find-content/{id}")
    public CommonResp findContent(@PathVariable Long id){
        CommonResp<String> resp = new CommonResp<>();
        String content = docService.findContent(id);
        resp.setContent(content);
        return resp;
    }
}
