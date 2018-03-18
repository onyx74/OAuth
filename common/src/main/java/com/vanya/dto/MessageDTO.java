package com.vanya.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Date;

@Data
public class MessageDTO {
    private Long id;

    private long ownerId;

    private String sendTo;

    private String subject;

    private String text;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    private Date createdAt;

    private boolean checked;

}
