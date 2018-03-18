package com.vanya.service;

import com.vanya.dao.MessagesRepository;
import com.vanya.dto.MessageDTO;
import com.vanya.model.MessageEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
