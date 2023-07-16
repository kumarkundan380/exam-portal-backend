package com.exam.repository;

import com.exam.model.User;
import com.exam.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
	
	Optional<VerificationToken> findByToken(String token);

	Optional<VerificationToken> findByUser(User user);

}
