package goorm.member_management.auth.dto.response;

import goorm.member_management.member.entity.Member;
import goorm.member_management.member.entity.RoleType;

public record MemberSignupResponse(
		Long id,
		String name,
		String email,
		RoleType role
) {

	public static MemberSignupResponse from(Member member) {
		return new MemberSignupResponse(
				member.getId(),
				member.getName(),
				member.getEmail(),
				member.getRole()
		);
	}
}
