package com.vanya.dao;

import com.vanya.model.FriendEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FriendsRepository extends PagingAndSortingRepository<FriendEntity, Long> {

    boolean existsByUserIdAndFriendId(long userId, long friendId);

    @Transactional
    void deleteByUserIdAndFriendId(long userId, long friendId);
}
