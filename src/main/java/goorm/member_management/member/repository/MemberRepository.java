package goorm.member_management.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import goorm.member_management.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	boolean existsByEmail(String email);

	Optional<Member> findByEmail(String email);
}
