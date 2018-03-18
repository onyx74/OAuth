package com.vanya.service;

import com.vanya.dao.FriendsRepository;
import com.vanya.dao.UserRepository;
import com.vanya.dto.UserDto;
import com.vanya.model.FriendEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendsService {

    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;


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

    //move To userService
    public List<UserDto> findAllPagebleLike(String s, PageRequest pageRequest) {
        return null;
    }

    public Page<Long> findAllPagebleIds(long userId, PageRequest pageRequest) {
        return friendsRepository.findAllByUserId(userId, pageRequest).map(FriendEntity::getFriendId);
    }

    public List<UserDto> findAllPageable(long userId, PageRequest pageRequest) {
        List<Long> friendsIds = friendsRepository.findAllByUserId(userId, pageRequest)
                .getContent()
                .stream()
                .map(FriendEntity::getFriendId)
                .collect(Collectors.toList());
        return userRepository
                .findAllByIdIn(friendsIds)
                .stream()
                .map(x -> mapper.map(x, UserDto.class))
                .collect(Collectors.toList());
    }
}
