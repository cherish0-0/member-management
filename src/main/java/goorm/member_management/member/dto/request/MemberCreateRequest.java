package goorm.member_management.member.dto.request;

public record MemberCreateRequest(
	String name,
	String email,
	String password
) {
}
