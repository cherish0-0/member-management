package goorm.member_management.member.dto.request;

public record MemberLoginRequest(
	String email,
	String password
) {
}
