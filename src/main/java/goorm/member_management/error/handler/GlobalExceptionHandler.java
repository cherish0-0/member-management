package goorm.member_management.error.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import goorm.member_management.error.dto.ErrorCode;
import goorm.member_management.error.exception.CustomException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<String> handleCustomException(CustomException ex) {
		log.error("exceptionMessage: {}",ex.getMessage());
		return ResponseEntity
			.status(ex.getErrorCode().getCode())
			.body(ex.getErrorCode().getMessage());
	}

	// CustomException에서 지정하지 않은 모든 예외 : 서버 오류로 간주
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGenericException(Exception ex) {
		log.error("Unhandled Exception: ", ex);
		return ResponseEntity
			.internalServerError()
			.body(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
	}
}
