package goorm.member_management.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.member_management.error.dto.ErrorCode;
import goorm.member_management.error.exception.CustomException;
import goorm.member_management.member.dto.request.MemberCreateRequest;
import goorm.member_management.member.dto.response.MemberLoginResponse;
import goorm.member_management.member.entity.Member;
import goorm.member_management.member.entity.RoleType;
import goorm.member_management.member.repository.MemberRepository;
import goorm.member_management.security.JwtProvider;
import goorm.member_management.security.dto.TokenInfo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	// 트랜잭션 동기화를 위해 @Transactional 어노테이션 추가
	@Transactional
	public void createMember(MemberCreateRequest request) {

		// 이메일 중복 체크
		if (memberRepository.existsByEmail(request.email())) {
			throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
		}

		// 비밀번호 암호화
		final String encodedPassword = passwordEncoder.encode(request.password());

		final Member member = new Member(request.name(), request.email(), encodedPassword, RoleType.USER);

		memberRepository.save(member);
	}

	@Transactional(readOnly = true)
	public MemberLoginResponse loginMember(String email, String password) {

		final Member member = findByEmail(email);

		checkPassword(password, member);

		final TokenInfo accessToken = jwtProvider.createAccessToken(email);

		return new MemberLoginResponse(member.getEmail(), accessToken);
	}

	private Member findByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMAIL));
	}

	private void checkPassword(String password, Member member) {
		if (!passwordEncoder.matches(password, member.getPassword())) {
			throw new CustomException(ErrorCode.NOT_MATCHES_PASSWORD);
		}
	}
}