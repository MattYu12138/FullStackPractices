package com.matt.wiki.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.matt.wiki.domain.User;
import com.matt.wiki.domain.UserExample;
import com.matt.wiki.exception.BusinessException;
import com.matt.wiki.exception.BusinessExceptionCode;
import com.matt.wiki.mapper.UserMapper;
import com.matt.wiki.req.UserQueryReq;
import com.matt.wiki.req.UserSaveReq;
import com.matt.wiki.resp.PageResp;
import com.matt.wiki.resp.UserQueryResp;
import com.matt.wiki.util.CopyUtil;
import com.matt.wiki.util.SnowFlake;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public PageResp<UserQueryResp> list(UserQueryReq userQueryReq) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();

        if (!ObjectUtils.isEmpty(userQueryReq.getLoginName())) {
            criteria.andLoginNameLike("%" + userQueryReq.getLoginName() + "%");
        }


        PageHelper.startPage(userQueryReq.getPage(), userQueryReq.getSize());
        List<User> userList = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOG.info("总行数: {}", pageInfo.getTotal());
        LOG.info("总页数:{}", pageInfo.getPages());

//        List<UserQueryResp> respList = new ArrayList<>();
//
//        for (User user : userList) {
//            UserQueryResp userResp = new UserQueryResp();
////            BeanUtils.copyProperties(user, userResp);
////            respList.add(userResp);
//
//            UserQueryResp userREsp = CopyUtil.copy(user, UserQueryResp.class);
//
//            respList.add(userREsp);
//        }


        var respList = CopyUtil.copyList(userList, UserQueryResp.class);

        PageResp<UserQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }

    /*
    * save
    * */
    public void save(UserSaveReq userSaveReq) {
        User userSave = CopyUtil.copy(userSaveReq, User.class);
        if(ObjectUtils.isEmpty(userSave.getId())){
            var userDB = selectByLoginName(userSaveReq.getLoginName());
            if(ObjectUtils.isEmpty(userDB)){
                userSave.setId(snowFlake.nextId());
                userMapper.insert(userSave);
            }else{
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }

        }else{

//            防止绕过前端验证
            userSave.setPassword(null);
            userMapper.updateByPrimaryKeySelective(userSave);
        }

    }

    public User selectByLoginName(String LoginName){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(LoginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if(userList.size() == 0){
            return null;
        }
        return userList.get(0);
    }

    /*
    * delete
    * */
    public void delete(Long id){
        System.out.println("正在删除id： " + id);
        userMapper.deleteByPrimaryKey(id);
    }

}
