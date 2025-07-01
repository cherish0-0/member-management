package goorm.member_management.member.dto.request;

public record MemberUpdateRequest(
	String currentName,
	String newName,
	String currentPassword,
	String newPassword,
	String currentEmail,
	String newEmail
) {
}
