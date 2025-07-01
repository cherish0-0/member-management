package goorm.member_management.member.dto;

import goorm.member_management.member.entity.RoleType;

public record PrincipalDetails(
	Long id,
	String email,
	RoleType role
) {
}
