package goorm.member_management.security;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import goorm.member_management.error.dto.ErrorCode;
import goorm.member_management.error.exception.CustomException;
import goorm.member_management.security.dto.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;

@Component
public class JwtProvider {

	private final Key hashKey;
	private final Duration expirationTime;

	public JwtProvider(@Value("${jwt.secretKey}") String secretKey,
		@Value("${jwt.expirationTime}") Duration expirationTime) {

		// 1. Base64로 인코딩된 문자열 비밀 키를 실제 바이트 배열로 디코딩
		// JWT 서명 알고리즘(HMAC)은 문자열이 아닌 이진 바이트 데이터를 키로 사용해야 함
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);

		// 2. 디코딩된 바이트 배열을 사용하여 HMAC 서명에 적합한 SecretKey 객체 생성
		// Keys.hmacShaKeyFor()는 바이트 배열을 기반으로 JWS(JSON Web Signature)에서
		// 사용될 수 있는 안전한 Key 인스턴스를 생성하고 관리함
		this.hashKey = Keys.hmacShaKeyFor(keyBytes);

		this.expirationTime = expirationTime;
	}

	/**
	 * JWT 토큰 생성
	 * - setSubject: 토큰 페이로드(속성)의 주체 설정
	 * - claim: 페이로드의 속성 설정 (키, 값 쌍으로 저장)
	 * - compact: JWT 토큰 문자열로 변환
	 */
	public TokenInfo createAccessToken(String email) {
		final String tokenId = UUID.randomUUID().toString();
		final Date issuedAt = Date.from(Instant.now());
		final Date expiration = Date.from(issuedAt.toInstant().plus(getExpirationTime()));

		String token = Jwts.builder()
			.setSubject(email)
			.claim("tokenId", tokenId)
			.signWith(getHashKey(), SignatureAlgorithm.HS512)
			.setIssuedAt(issuedAt)
			.setExpiration(expiration)
			.compact();

		return new TokenInfo(token, expiration, email, tokenId);
	}

	public Claims validateToken(String token) {

		if (!StringUtils.hasText(token)) {
			throw new CustomException(ErrorCode.TOKEN_IS_EMPTY);
		}

		try {

			parseToken(token);

		} catch (ExpiredJwtException e) {
			throw new CustomException(ErrorCode.TOKEN_EXPIRED);
		} catch (SecurityException | MalformedJwtException e) {
			throw new CustomException(ErrorCode.TOKEN_INVALID);
		} catch (UnsupportedJwtException e) {
			throw new CustomException(ErrorCode.TOKEN_HASH_NOT_SUPPORTED);
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.TOKEN_WRONG_SIGNATURE);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.TOKEN_VALIDATION_TRY_FAILED);
		}
		return parseToken(token);
	}

	private Claims parseToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(hashKey)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public Key getHashKey() {
		return hashKey;
	}

	public Duration getExpirationTime() {
		return expirationTime;
	}
}
