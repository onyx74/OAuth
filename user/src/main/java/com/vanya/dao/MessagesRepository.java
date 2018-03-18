package com.vanya.dao;

import com.vanya.model.MessageEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesRepository extends PagingAndSortingRepository<MessageEntity, Long> {
}
