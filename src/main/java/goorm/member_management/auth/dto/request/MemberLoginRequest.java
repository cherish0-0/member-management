package goorm.member_management.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequest(
	String email,

	String password
) {
}
