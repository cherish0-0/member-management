package goorm.member_management.member.dto.response;

import goorm.member_management.security.dto.TokenInfo;

public record MemberLoginResponse(
	String userEmail,
	TokenInfo accessToken
) {
}
