package goorm.member_management.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import goorm.member_management.error.dto.ErrorCode;
import goorm.member_management.error.exception.CustomException;
import goorm.member_management.member.dto.request.MemberCreateRequest;
import goorm.member_management.member.entity.Member;
import goorm.member_management.member.entity.RoleType;
import goorm.member_management.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

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
}