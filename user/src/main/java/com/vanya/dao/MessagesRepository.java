package com.vanya.dao;


import com.vanya.model.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MessagesRepository extends PagingAndSortingRepository<MessageEntity, Long> {

    Page<MessageEntity> findAllByOwnerIdAndSubjectLikeAndSendToIn(long ownerId,
                                                                  Pageable pageable,
                                                                  String subject,
                                                                  List<String> sentTo);

    Page<MessageEntity> findAllBySendToAndSubjectLikeAndOwnerIdIn(String sendTo,
                                                                  Pageable pageable,
                                                                  String subject,
                                                                  List<Long> ownerId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE MessageEntity  m SET m.checked= true WHERE m.id= ?1")
    int setIsRead(long messageId);

    @Query("SELECT COUNT(m) FROM MessageEntity m WHERE m.sendTo= ?1 AND m.checked= false ")
    int selectCountUnreadMessages(String userName);
}
