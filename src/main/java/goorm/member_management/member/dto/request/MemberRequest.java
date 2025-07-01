package goorm.member_management.member.dto.request;

public record MemberRequest(
	String name,
	String email,
	String password
) {
}
