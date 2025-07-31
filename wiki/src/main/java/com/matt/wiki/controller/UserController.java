package com.matt.wiki.controller;

import com.matt.wiki.req.UserQueryReq;
import com.matt.wiki.req.UserSaveReq;
import com.matt.wiki.resp.CommonResp;
import com.matt.wiki.resp.PageResp;
import com.matt.wiki.resp.UserQueryResp;
import com.matt.wiki.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/user")
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq userQueryReq){
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(userQueryReq);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid UserSaveReq userSaveReq){
        userSaveReq.setPassword(DigestUtils.md5DigestAsHex(userSaveReq.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.save(userSaveReq);
        return resp;

    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable("id") Long id){
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }

}
