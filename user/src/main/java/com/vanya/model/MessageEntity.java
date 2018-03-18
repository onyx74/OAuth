package com.vanya.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "messages")
@Data
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private long ownerId;

    private String sendTo;

    private String subject;

    private String text;

    private Date createdAt = new Date(System.currentTimeMillis());
}
