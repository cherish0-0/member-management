package goorm.member_management.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.member_management.member.dto.response.MemberResponse;
import goorm.member_management.member.dto.response.PageResponse;
import goorm.member_management.member.entity.Member;
import goorm.member_management.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MemberResponse> getMembers(Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);

        Page<MemberResponse> memberResponses = members.map(member ->
            new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole())
        );

        return PageResponse.from(memberResponses);
    }
}
