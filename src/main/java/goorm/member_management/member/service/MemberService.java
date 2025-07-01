package goorm.member_management.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import goorm.member_management.member.dto.PrincipalDetails;
import goorm.member_management.member.dto.request.MemberUpdateRequest;
import goorm.member_management.member.dto.response.MemberFindResponse;
import goorm.member_management.member.entity.Member;
import goorm.member_management.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public List<MemberFindResponse> findAllMembers() {
		return memberRepository.findAll()
				.stream()
				.map(MemberFindResponse::from)
				.toList();
	}

	public MemberFindResponse findMemberById(Long id) {
		Member memberById = memberRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다."));

		return MemberFindResponse.from(memberById);
	}

	public MemberFindResponse findMe(PrincipalDetails loginMember) {
		Member meByEmail = memberRepository.findByEmail(loginMember.email())
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다."));

		return MemberFindResponse.from(meByEmail);
	}

	public void updateMemberById(Long id, MemberUpdateRequest request) {
		updateMember(id, request);
	}

	public void updateMe(PrincipalDetails loginMember, MemberUpdateRequest request) {
		updateMember(loginMember.id(),  request);
	}

	public void deleteMemberById(Long id) {
		memberRepository.deleteById(id);
	}

	public void deleteMe(PrincipalDetails loginMember) {
		memberRepository.deleteById(loginMember.id());
	}

	private void updateMember(Long id, MemberUpdateRequest request) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다."));

		if (request.currentName() != null && !request.currentName().equals(member.getName())) {
			member.updateName(request.newName());
		}

		if (request.currentEmail() != null && !request.newEmail().equals(request.currentEmail())) {
			member.updateEmail(request.newEmail());
		}

		if (request.currentPassword() != null && !request.newPassword().equals(request.currentPassword())) {
			member.updatePassword(request.newPassword());
		}
	}
}
