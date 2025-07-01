package goorm.member_management.auth.dto.request;

public record MemberSignupRequest(
	String name,
	String email,
	String password
) {
}
