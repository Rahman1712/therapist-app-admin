package com.ar.therapist.admin.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ar.therapist.admin.entity.Admin;

import jakarta.transaction.Transactional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>{

	public Optional<Admin> findByUsername(String username);
	
	public Optional<Admin> findByEmail(String email);

	@Modifying
	@Transactional
	@Query("UPDATE Admin a SET a.enabled = :enabled WHERE a.id =:id")
	public void updateEnabledById(@Param("id") Long id,@Param("enabled") boolean enabled);


	@Modifying
	@Transactional
	@Query("UPDATE Admin a SET "
			+ "a.password = :newPassword "
			+ "WHERE a.id = :id")
	void updatePassword(@Param("id")Long id, @Param("newPassword")String newPassword);
	
	@Modifying
	@Transactional
	@Query("update Admin a set "
			+ "a.image = :image, "
			+ "a.imageName = :imageName, "
			+ "a.imageType = :imageType "
			+ "where a.id = :id")
	void updateAdminImageById(@Param("id")Long id,
			@Param("image") byte[] image,
			@Param("imageName")String imageName,
			@Param("imageType")String imageType);
	
	@Modifying
	@Transactional
	@Query("update Admin a set a.fullname = :fullname, "
			+ "a.mobile= :mobile, a.email= :email "
			+ "where a.id = :id")
	void updateAdminById(@Param("id")Long id,@Param("fullname") String fullname,
			@Param("mobile")String mobile, @Param("email")String email);
	
	
}
