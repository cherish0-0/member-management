package goorm.member_management.error.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import goorm.member_management.error.dto.ErrorCode;
import goorm.member_management.error.dto.ErrorResponse;
import goorm.member_management.error.exception.CustomException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        log.error("exceptionMessage: {}", ex.getMessage());

        final List<String> errors = new ArrayList<>();

        errors.add(ex.getErrorCode().getMessage());

        return ResponseEntity
            .status(ex.getErrorCode().getStatus())
            .body(new ErrorResponse(errors));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        final List<String> errors = new ArrayList<>();

        errors.add(ErrorCode.AUTHORIZATION_DENIED.getMessage());

        log.error("Authorization Denied: {}", ex.getMessage());

        return ResponseEntity
            .status(ErrorCode.AUTHORIZATION_DENIED.getStatus())
            .body(new ErrorResponse(errors));
    }

    // 회원 가입 DTO 유효성 검사 실패 시 발생하는 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        final List<String> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            final String errorMessage = error.getDefaultMessage(); // DTO에 지정한 메시지
            log.error("exceptionMessage: {}", errorMessage);

            errors.add(errorMessage);
        });

        final ErrorResponse response = new ErrorResponse(errors);

        return ResponseEntity
            .badRequest()
            .body(response);
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
