package com.vanya.service;

import com.vanya.dao.MessagesRepository;
import com.vanya.dto.MessageDTO;
import com.vanya.model.MessageEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private ModelMapper modelMapper;

    public MessageDTO addNewMessage(long ownerId, MessageDTO messageDTO) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setOwnerId(ownerId);
        messageEntity.setSendTo(messageDTO.getSendTo());
        messageEntity.setSubject(messageDTO.getSubject());
        messageEntity.setText(messageDTO.getText());

        return modelMapper.map(messagesRepository.save(messageEntity), MessageDTO.class);
    }

    public Page<MessageDTO> getAllMessages(long ownerId, PageRequest pageRequest,
                                           String subject,
                                           List<String> userNames) {

        return messagesRepository
                .findAllByOwnerIdAndSubjectLikeAndSendToIn(ownerId,
                        pageRequest,
                        "%" + subject + "%",
                        userNames)
                .map(x -> modelMapper.map(x, MessageDTO.class));

    }

    public Page<MessageDTO> getAllMessages(String sendTo,
                                           PageRequest pageRequest,
                                           String subject,
                                           List<Long> ownerIds) {

        return messagesRepository
                .findAllBySendToAndSubjectLikeAndOwnerIdIn(sendTo,
                        pageRequest,
                        "%" + subject + "%",
                        ownerIds)
                .map(x -> modelMapper.map(x, MessageDTO.class));

    }

    public MessageDTO getMessage(long messageId) {
        return modelMapper.map(messagesRepository.findOne(messageId), MessageDTO.class);
    }

    public void readMessage(long messageId) {
        messagesRepository.setIsRead(messageId);

    }

    public int getCountOfUnreadMessages(String username) {
        return messagesRepository.selectCountUnreadMessages(username);
    }
}
