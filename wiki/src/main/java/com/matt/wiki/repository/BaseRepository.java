package com.matt.wiki.repository;

import com.matt.wiki.entity.BaseEntity;
import org.springframework.data.repository.CrudRepository;

public interface BaseRepository<T extends BaseEntity> extends CrudRepository<T, Long> {


}
