package goorm.member_management.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import goorm.member_management.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
