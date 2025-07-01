package goorm.member_management.member.dto.response;

import goorm.member_management.member.entity.Member;

public record MemberFindResponse(
		Long id,
		String name,
		String email

) {

	public static MemberFindResponse from(Member member) {
		return new MemberFindResponse(
				member.getId(),
				member.getName(),
				member.getEmail()
		);
	}
}
