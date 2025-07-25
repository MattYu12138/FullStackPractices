package com.matt.wiki.controller;

import com.matt.wiki.req.CategoryQueryReq;
import com.matt.wiki.req.CategorySaveReq;
import com.matt.wiki.resp.CommonResp;
import com.matt.wiki.resp.CategoryQueryResp;
import com.matt.wiki.resp.PageResp;
import com.matt.wiki.service.CategoryService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/category")
@RestController
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @GetMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq categoryQueryReq){
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(categoryQueryReq);
        resp.setContent(list);
        return resp;

    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid CategorySaveReq categorySaveReq){
        CommonResp resp = new CommonResp<>();
        categoryService.save(categorySaveReq);
        return resp;

    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable("id") Long id){
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }

}
