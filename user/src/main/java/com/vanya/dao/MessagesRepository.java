package com.vanya.dao;


import com.vanya.model.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesRepository extends PagingAndSortingRepository<MessageEntity, Long> {

    Page<MessageEntity> findAllByOwnerId(long ownerId, Pageable pageable);
}
