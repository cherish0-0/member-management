package goorm.member_management.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.member_management.error.dto.ErrorCode;
import goorm.member_management.error.exception.CustomException;
import goorm.member_management.member.dto.MemberInfo;
import goorm.member_management.member.dto.request.MemberSignUpRequest;
import goorm.member_management.member.entity.Member;
import goorm.member_management.member.entity.RoleType;
import goorm.member_management.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public void signUp(MemberSignUpRequest request) {
        // 이메일 중복 체크
        if (memberRepository.existsByEmail(request.email())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 비밀번호 암호화
        final String encodedPassword = passwordEncoder.encode(request.password());

        memberRepository.save(new Member(request.name(), request.email(), encodedPassword, RoleType.USER));
    }

    @Transactional(readOnly = true)
    public MemberInfo signIn(String email, String password) {
        final Member member = findByEmail(email);
        checkPassword(password, member.getPassword());
        return new MemberInfo(member.getEmail(), member.getRole());
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMAIL));
    }

    private void checkPassword(String rawPassword,
        String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new CustomException(ErrorCode.NOT_MATCHES_PASSWORD);
        }
    }

}