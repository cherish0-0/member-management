package goorm.member_management.security.dto;

import java.util.Date;

public record TokenInfo(
	String accessToken,
	Date accessTokenExpirationTime,
	String userEmail,
	String tokenId
) {
}
