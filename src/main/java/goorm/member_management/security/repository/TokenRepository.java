package goorm.member_management.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import goorm.member_management.security.dto.RefreshTokenInfo;

public interface TokenRepository extends JpaRepository<RefreshTokenInfo, Long> {
}
