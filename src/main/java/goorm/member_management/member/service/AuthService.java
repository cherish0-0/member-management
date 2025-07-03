package goorm.member_management.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	// @Transactional : 프록시 우회로 트랜잭션이 적용되지 않는 내부 메서드에서 트랜잭션을 적용하기 위해 사용
	// 데이터베이스 작업이 성공적으로 완료되면 커밋되고, 예외 발생 시 롤백됨
	@Transactional
	public void createMember(MemberCreateRequest request) {

		// 이메일 중복 체크
		if (memberRepository.existsByEmail(request.email())) {
			throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
		}

		// 비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(request.password());

		Member member = new Member(request.name(), request.email(), encodedPassword, RoleType.USER);

		memberRepository.save(member);
	}
}