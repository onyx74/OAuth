package com.vanya.model.token;

import com.vanya.model.UserEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@EqualsAndHashCode
@ToString
public class ChangePasswordToken {
    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id", foreignKey = @ForeignKey(name = "FK_USER"))
    private UserEntity user;

    private Date expiryDate;

    public ChangePasswordToken() {

    }

    public ChangePasswordToken(final String token) {

        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public ChangePasswordToken(final String token, final UserEntity user) {
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    private Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public void updateToken(final String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }
}
