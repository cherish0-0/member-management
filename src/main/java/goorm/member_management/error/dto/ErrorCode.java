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
    NOT_MATCHES_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),

    // 회원 정보 관련 에러
    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  
    // 권한 관련 에러
    AUTHORIZATION_DENIED("접근 권한이 없습니다.", HttpStatus.FORBIDDEN),

    // 회원 관련 에러
    MEMBER_NOT_FOUND("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),

    // 토큰 관련 에러
    TOKEN_IS_EMPTY("토큰이 비어있습니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_WRONG_SIGNATURE("잘못된 토큰입니다.", HttpStatus.BAD_REQUEST),
    TOKEN_HASH_NOT_SUPPORTED("지원하지 않는 형식의 토큰입니다.", HttpStatus.BAD_REQUEST),
    TOKEN_VALIDATION_TRY_FAILED("인증에 실패했습니다.", HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus status;

}
