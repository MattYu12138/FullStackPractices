package com.matt.wiki.repository;

import com.matt.wiki.entity.GuestRegisterEntity;

import java.util.List;

public interface GuestRegisterRepository extends BaseRepository<GuestRegisterEntity> {
    /**
     * 根据手机号查询登记信息
     * @param phone
     * @return
     */
    List<GuestRegisterEntity> findAllByPhone(String phone);
}
