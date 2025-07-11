package goorm.member_management.member.dto.response;

import goorm.member_management.member.entity.RoleType;

public record MemberResponse(
    Long id,
    String name,
    String email,
    RoleType role
) {
}
