package goorm.member_management.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.member_management.error.dto.ErrorCode;
import goorm.member_management.error.exception.CustomException;
import goorm.member_management.member.dto.MemberInfo;
import goorm.member_management.member.dto.Tokens;
import goorm.member_management.member.dto.request.MemberSignUpRequest;
import goorm.member_management.member.entity.Member;
import goorm.member_management.member.entity.RoleType;
import goorm.member_management.member.repository.MemberRepository;
import goorm.member_management.security.JwtProvider;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    /**
     * 회원 가입
     * email 중복 체크 후, 비밀번호 암호화하여 저장
     */
    @Transactional
    public void signUp(MemberSignUpRequest request, RoleType role) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        final String encodedPassword = passwordEncoder.encode(request.password());

        memberRepository.save(new Member(request.name(), request.email(), encodedPassword, role));
    }

    /**
     * 로그인
     * email 로 회원 조회 후, 비밀번호 검증
     * 토큰 생성 후, refreshToken 저장
     * 반환 값은 MemberInfo 객체로, 이메일, 역할, 토큰 정보를 포함
     */
    @Transactional
    public MemberInfo signIn(String email, String password) {
        final Member member = findByEmail(email);
        checkPassword(password, member.getPassword());
        final Tokens tokens = jwtProvider.createTokens(member.getId(), member.getEmail(), member.getRole());
        member.setRefreshToken(tokens.refreshToken());

        return new MemberInfo(member.getEmail(), member.getRole(), tokens);
    }

    /**
     * 토큰 갱신
     * refreshToken 유효성 검사 후, 해당 회원 조회
     * 새로운 토큰 생성 후, refreshToken 업데이트
     */
    @Transactional
    public Tokens refresh(String refreshToken) {
        jwtProvider.validateToken(refreshToken, true);

        Member member = memberRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new CustomException(ErrorCode.TOKEN_INVALID));

        final Tokens tokens = jwtProvider.createTokens(member.getId(), member.getEmail(), member.getRole());
        member.setRefreshToken(tokens.refreshToken());

        return tokens;
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMAIL));
    }

    private void checkPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new CustomException(ErrorCode.NOT_MATCHES_PASSWORD);
        }
    }

}