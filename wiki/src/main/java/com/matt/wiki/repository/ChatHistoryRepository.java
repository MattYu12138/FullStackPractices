package com.matt.wiki.repository;

import com.matt.wiki.entity.ChatHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatHistoryRepository extends BaseRepository<ChatHistoryEntity> {

    /**
     * 获取用户当前会话20条记录
     * @param sessionId
     * @return
     */
    List<ChatHistoryEntity> findTop20BySessionIdOrderByIdDesc(String sessionId);

    Page<ChatHistoryEntity> findAllBySessionId(String sessionId, Pageable pageable);

    /*
     * 通过会话ID删除所有相关的聊天记录
     * 此操作标记为事务性和修改性，意味着它将在一个数据库事务中执行，以保持数据一致性
     */
    @Transactional
    //表示修改数据库的操作
    @Modifying
    void deleteBySessionId(String sessionId);
}
