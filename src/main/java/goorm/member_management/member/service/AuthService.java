package goorm.member_management.member.service;

import org.springframework.stereotype.Service;
import goorm.member_management.member.dto.request.MemberCreateRequest;
import goorm.member_management.member.entity.Member;
import goorm.member_management.member.entity.RoleType;
import goorm.member_management.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class AuthService {
	private final MemberRepository memberRepository;
	public void createMember(MemberCreateRequest request) {
		Member member = new Member(request.name(), request.email(), request.password(), RoleType.USER);
		memberRepository.save(member);
	}
}