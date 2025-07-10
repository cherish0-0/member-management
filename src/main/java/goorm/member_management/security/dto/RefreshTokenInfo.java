package goorm.member_management.security.dto;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshTokenInfo {

    @Id
    @Column
    private String email;

    @Column
    private String refreshToken;

    @Column
    private Date expiration;

    public RefreshTokenInfo(String email, String refreshToken, Date expiration) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Date getExpiration() {
        return expiration;
    }
}
