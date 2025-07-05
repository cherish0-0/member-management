package goorm.member_management.error.dto;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// 공통 에러
	INTERNAL_SERVER_ERROR("서버 내부 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),

	// create 관련 에러
	DUPLICATE_EMAIL("이미 존재하는 이메일입니다.", HttpStatus.CONFLICT),

	// 로그인 관련 에러
	NOT_FOUND_EMAIL("존재하지 않는 이메일입니다.", HttpStatus.NOT_FOUND),
	NOT_MATCHES_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);

	private final String message;
	private final HttpStatus status;
}
