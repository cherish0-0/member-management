package goorm.member_management.auth.service;

import org.springframework.stereotype.Service;

import goorm.member_management.auth.dto.request.MemberLoginRequest;
import goorm.member_management.auth.dto.request.MemberSignupRequest;
import goorm.member_management.auth.dto.response.MemberSignupResponse;
import goorm.member_management.member.dto.PrincipalDetails;
import goorm.member_management.member.entity.Member;
import goorm.member_management.member.entity.RoleType;
import goorm.member_management.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final MemberRepository memberRepository;

	public MemberSignupResponse createMember(MemberSignupRequest request) {
		Member member = new Member(request.name(), request.email(), request.password(), RoleType.USER);
		memberRepository.save(member);
		return MemberSignupResponse.from(member);
	}

	public MemberSignupResponse createMemberAsAdmin(MemberSignupRequest request) {
		Member member = new Member(request.name(), request.email(), request.password(), RoleType.ADMIN);
		memberRepository.save(member);
		return MemberSignupResponse.from(member);
	}

	public PrincipalDetails login(MemberLoginRequest request) {
		Member member = memberRepository.findByEmail(request.email())
				.orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

		if (!member.getPassword().equals(request.password())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		return new PrincipalDetails(member.getId(), member.getEmail(), member.getRole());
	}

	public void logout(PrincipalDetails loginMember) {
		// TODO JWT 구현 후 로그아웃 로직 추가 필요
	}
}
