package com.vanya.service;

import com.vanya.dao.FriendsRepository;
import com.vanya.model.FriendEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendsService {

    @Autowired
    private FriendsRepository friendsRepository;

    public boolean isFriend(long userId, long friendId) {
        return friendsRepository.existsByUserIdAndFriendId(userId, friendId);
    }

    public boolean addNewFriend(long userId, long friendId) {
        if (!isFriend(userId, friendId)) {
            FriendEntity friendEntity = new FriendEntity();
            friendEntity.setUserId(userId);
            friendEntity.setFriendId(friendId);
            friendsRepository.save(friendEntity);
            return true;
        }
        return false;
    }

    public boolean removeFriend(long userId, long friendId) {
        if (isFriend(userId, friendId)) {
            friendsRepository.deleteByUserIdAndFriendId(userId, friendId);
            return true;
        }
        return false;
    }
}
