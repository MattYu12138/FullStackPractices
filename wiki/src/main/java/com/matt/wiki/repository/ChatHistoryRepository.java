package com.matt.wiki.repository;

import com.matt.wiki.entity.ChatHistoryEntity;

import java.util.List;

public interface ChatHistoryRepository extends BaseRepository<ChatHistoryEntity> {

    /**
     * 获取用户当前会话20条记录
     * @param sessionId
     * @return
     */
    List<ChatHistoryEntity> findTop20BySessionIdOrderByIdDesc(String sessionId);
}
