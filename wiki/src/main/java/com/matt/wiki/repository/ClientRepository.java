package com.matt.wiki.repository;


import com.matt.wiki.entity.ClientEntity;

import java.util.List;

public interface ClientRepository extends BaseRepository<ClientEntity>{

    /**
     * 根据手机号查询物品登记信息
     * @param phone
     * @return
     */
    List<ClientEntity> findAllByPhone(String phone);
}
