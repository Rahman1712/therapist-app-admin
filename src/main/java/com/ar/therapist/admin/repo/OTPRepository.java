package com.ar.therapist.admin.repo;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ar.therapist.admin.entity.OtpData;
import com.ar.therapist.admin.entity.Admin;

import jakarta.transaction.Transactional;

@Repository
public interface OTPRepository extends JpaRepository<OtpData, Long>{

	public Optional<OtpData> findByAdminId(Long id);
	
	public Optional<OtpData> findByAdmin(Admin admin);

	@Modifying
	@Transactional
    @Query("UPDATE OtpData o SET o.otp = :otp, o.expirationTime = :expirationTime WHERE o.admin.id = :adminId")
    void updateOtpAndExpirationTimeByUserId(String otp, LocalDateTime expirationTime, Long adminId);
	
//	@Query("SELECT o FROM OtpData o WHERE o.admin.id = ?1")
//	Optional<OtpData> findByAdminId(Long adminid);
}
