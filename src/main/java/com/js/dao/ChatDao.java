package com.js.dao;

import com.js.entity.ChatDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatDao extends CrudRepository<ChatDetails, Long> {
    List<ChatDetails> findAllByFromId(String username);

    List<ChatDetails> findAllByToId(String username);
}
