package goorm.member_management.error.exception;

import goorm.member_management.error.dto.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

  private final ErrorCode errorCode;

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
        this.errorCode = errorCode;
	}
}
