package goorm.member_management.member.dto.request;

public record MemberSignInRequest(
    String email,
    String password
) {
}
