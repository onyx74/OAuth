package com.vanya.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "friends")
@Data
public class FriendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private long userId;
    private long friendId;
}
