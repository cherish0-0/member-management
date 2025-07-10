package goorm.member_management.member.dto.request;

public record MemberUpdateRequest(
    String name,
    String email
) {
}
