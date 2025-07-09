package goorm.member_management.security;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import goorm.member_management.error.dto.ErrorCode;
import goorm.member_management.error.exception.CustomException;
import goorm.member_management.member.entity.RoleType;
import goorm.member_management.security.dto.RefreshTokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider {

    private final Key accessTokenHashKey;
    private final Key refreshTokenHashKey;
    private final Duration accessTokenExpiration;
    private final Duration refreshTokenExpiration;

    public JwtProvider(
        @Value("${jwt.accessTokenSecretKey}") String secretKey,
        @Value("${jwt.refreshTokenSecretKey}") String refreshTokenSecretKey,
        @Value("${jwt.accessTokenExpiration}") Duration accessTokenExpiration,
        @Value("${jwt.refreshTokenExpiration}") Duration refreshTokenExpiration
    ) {
        // 1. Base64로 인코딩된 문자열 비밀 키를 실제 바이트 배열로 디코딩
        // JWT 서명 알고리즘(HMAC)은 문자열이 아닌 이진 바이트 데이터를 키로 사용해야 함
        byte[] accessKeyBytes = Decoders.BASE64.decode(secretKey);
        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshTokenSecretKey);

        // 2. 디코딩된 바이트 배열을 사용하여 HMAC 서명에 적합한 SecretKey 객체 생성
        // Keys.hmacShaKeyFor()는 바이트 배열을 기반으로 JWS(JSON Web Signature)에서
        // 사용될 수 있는 안전한 Key 인스턴스를 생성하고 관리함
        this.accessTokenHashKey = Keys.hmacShaKeyFor(accessKeyBytes);
        this.refreshTokenHashKey = Keys.hmacShaKeyFor(refreshKeyBytes);
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    /**
     * JWT 토큰 생성
     * - setSubject: 토큰 페이로드(속성)의 주체 설정
     * - claim: 페이로드의 속성 설정 (키, 값 쌍으로 저장)
     * - compact: JWT 토큰 문자열로 변환
     */
    public String createAccessToken(String email,
        RoleType role) {
        final Date issuedAt = Date.from(Instant.now());
        final Date expiration = Date.from(issuedAt.toInstant().plus(accessTokenExpiration));

        return Jwts.builder()
            .signWith(accessTokenHashKey, SignatureAlgorithm.HS512)
            .setAudience(email)
            .setIssuedAt(issuedAt)
            .setExpiration(expiration)
            .addClaims(Map.of("role", role.name()))
            .compact();
    }

    public RefreshTokenInfo createRefreshToken(String email) {
        final Date issuedAt = Date.from(Instant.now());
        final Date expiration = Date.from(issuedAt.toInstant().plus(refreshTokenExpiration));

        String token = Jwts.builder()
            .signWith(refreshTokenHashKey, SignatureAlgorithm.HS512)
            .setAudience(email)
            .setIssuedAt(issuedAt)
            .setExpiration(expiration)
            .compact();

        return new RefreshTokenInfo(email, token, expiration);
    }

    public Claims validateToken(String token) {
        try {
            return parseToken(token);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.TOKEN_INVALID);
        }
    }

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(accessTokenHashKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

}
