package goorm.member_management.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.member_management.error.dto.ErrorCode;
import goorm.member_management.error.exception.CustomException;
import goorm.member_management.member.dto.request.MemberUpdateRequest;
import goorm.member_management.member.dto.response.MemberResponse;
import goorm.member_management.member.dto.response.PageResponse;
import goorm.member_management.member.entity.Member;
import goorm.member_management.member.entity.RoleType;
import goorm.member_management.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @Override
    public PageResponse<MemberResponse> getMembers(Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);

        Page<MemberResponse> memberResponses = members.map(member ->
            new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole())
        );

        return PageResponse.from(memberResponses);
    }

    @Transactional
    @Override
    public MemberResponse updateMember(Long id, MemberUpdateRequest request) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member.update(request.name(), request.email());

        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    @Transactional
    @Override
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 어드민이 자신이 아닌 다른 어드민 계정을 지우려고 할 때 예외 처리
        if (isDeletingAdmin(member) && isNotMine(id, member)) {
            throw new CustomException(ErrorCode.ADMIN_CANNOT_DELETE);
        }

        memberRepository.delete(member);
    }

    private static boolean isNotMine(Long id, Member member) {
        return !member.getId().equals(id);
    }

    private static boolean isDeletingAdmin(Member member) {
        return member.getRole() == (RoleType.ADMIN);
    }

}
