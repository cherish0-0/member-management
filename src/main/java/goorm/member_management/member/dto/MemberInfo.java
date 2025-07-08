package goorm.member_management.member.dto;

import goorm.member_management.member.entity.RoleType;

public record MemberInfo(String email,
                         RoleType role) {
}
