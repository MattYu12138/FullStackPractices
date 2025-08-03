package com.matt.wiki.controller;

import com.matt.wiki.aspect.LogAspect;
import com.matt.wiki.req.UserLoginReq;
import com.matt.wiki.req.UserQueryReq;
import com.matt.wiki.req.UserResetPasswordReq;
import com.matt.wiki.req.UserSaveReq;
import com.matt.wiki.resp.CommonResp;
import com.matt.wiki.resp.PageResp;
import com.matt.wiki.resp.UserLoginResp;
import com.matt.wiki.resp.UserQueryResp;
import com.matt.wiki.service.UserService;
import com.matt.wiki.util.SnowFlake;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;


@RequestMapping("/user")
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SnowFlake snowFlake;

    private final static Logger LOG = LoggerFactory.getLogger(LogAspect.class);


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

    @PostMapping("/reset-password")
    public CommonResp resetPassword(@RequestBody @Valid UserResetPasswordReq userResetPasswordReq){
        userResetPasswordReq.setPassword(DigestUtils.md5DigestAsHex(userResetPasswordReq.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.resetPassword(userResetPasswordReq);
        return resp;
    }

    @PostMapping("/login")
    public CommonResp login(@RequestBody @Valid UserLoginReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);
        Long token = snowFlake.nextId();

//        generate token, putting into userLoginResp

        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token.toString(), userLoginResp, 3600 *24, TimeUnit.SECONDS);
        LOG.info("generated token: {}", token);
        resp.setContent(userLoginResp);
        return resp;
    }

    @GetMapping("/logout/{token}")
    public CommonResp logout(@PathVariable String token){
        CommonResp resp = new CommonResp();
        redisTemplate.delete(token);
        LOG.info("delete token: {}", token);
        return resp;

    }

}
