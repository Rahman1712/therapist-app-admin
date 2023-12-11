package com.ar.therapist.admin.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ar.therapist.admin.entity.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long>{

	@Query("""
		SELECT t FROM Token t INNER JOIN Admin a ON t.admin.id = a.id
		WHERE a.id = :adminId AND (t.expired = false OR t.revoked = false)
	""")
	List<Token> findAllValidTokensByAdmin(Long adminId);
	
	Optional<Token> findByToken(String token);
	
	@Query("""
		SELECT t.logged_at FROM Token t INNER JOIN Admin a ON t.admin.id = a.id
		WHERE a.id = :adminId ORDER BY t.logged_at DESC LIMIT 5
	""")
	List<LocalDateTime> findLastFiveLoginsByAdmin(Long adminId);
}
