package com.ono.board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ono.board.domain.SiteUser;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
	
	Optional<SiteUser> findById(Long id);
	
	Optional<SiteUser> findByEmail(String email);
	
	SiteUser findByUsernameOrEmail(String username, String email);
	
	Optional<SiteUser> findByUsername(String username);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	boolean existsByNickname(String nickname);
	boolean existsByMobile(String mobile);

}