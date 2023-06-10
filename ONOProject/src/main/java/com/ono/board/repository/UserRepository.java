package com.ono.board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ono.board.domain.SiteUser;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
	Optional<SiteUser> findByusername(String username);
	Optional<SiteUser> findById(Long id);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	boolean existsByNickname(String nickname);
	boolean existsByMobile(String mobile);
}