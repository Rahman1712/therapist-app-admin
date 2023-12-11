package com.ar.therapist.admin.entity;

//public enum Role {
//	ADMIN,
//}


import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.ar.therapist.admin.entity.Permission.*;

public enum Role {
	
	ADMIN(
		Set.of(
			SELF_READ,
			SELF_UPDATE,
			ADMIN_READ,
			ADMIN_CREATE,
			ADMIN_UPDATE,
			ADMIN_DELETE,
			USER_READ,
			USER_CREATE,
			USER_UPDATE,
			USER_DELETE,
			THERAPIST_READ,
			THERAPIST_CREATE,
			THERAPIST_UPDATE,
			THERAPIST_DELETE
		)
	),	
	ADMINTRAINEE(
		Set.of(
			SELF_READ,
			SELF_UPDATE,
			USER_READ,
			USER_CREATE,
			USER_UPDATE,
			USER_DELETE,
			THERAPIST_READ,
			THERAPIST_CREATE,
			THERAPIST_UPDATE,
			THERAPIST_DELETE
		)
	),
	EDITOR(
		Set.of(
			SELF_READ,
			SELF_UPDATE,
			USER_READ,
			USER_CREATE,
			USER_UPDATE,
			USER_DELETE
		) 
	)
	;
	
    private final Set<Permission> permissions;
	
	private Role(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	
	public Set<Permission> getPermissions(){
		return permissions;
	}
	
	public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
		Set<SimpleGrantedAuthority> permissions = getPermissions()
				.stream().map(permission -> 
				new SimpleGrantedAuthority(permission.getPermission()))
				.collect(Collectors.toSet());
		
		permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
		return permissions;
	}
} 

/*
import lombok.Getter;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor

@Getter
private final Set<Permission> permissions;

public List<SimpleGrantedAuthority> getAuthorities(){
	var authorities = getPermissions()
		.stream()
		.map(permission -> new SimpleGrantedAuthority(permission.name()))
		.collect(Collectors.toList());
	authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
	return authorities;
}

*/