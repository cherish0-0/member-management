package goorm.member_management.error.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// 공통 에러
	INTERNAL_SERVER_ERROR("서버 내부 오류입니다.", 500),

	// create 관련 에러
	DUPLICATE_EMAIL("이미 존재하는 이메일입니다.", 400);

	private final String message;
	private final int code;
}
