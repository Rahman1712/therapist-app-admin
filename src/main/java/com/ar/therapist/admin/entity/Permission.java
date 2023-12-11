package com.ar.therapist.admin.entity;


public enum Permission {

	
	SELF_READ("self:read"),
	SELF_UPDATE("self:update"),
	ADMIN_READ("admin:read"),
	ADMIN_CREATE("admin:create"),
	ADMIN_UPDATE("admin:update"),
	ADMIN_DELETE("admin:delete"),
	USER_READ("user:read"),
	USER_CREATE("user:create"),
	USER_UPDATE("user:update"),
	USER_DELETE("user:delete"),
	THERAPIST_READ("therapist:read"),
	THERAPIST_CREATE("therapist:create"),
	THERAPIST_UPDATE("therapist:update"),
	THERAPIST_DELETE("therapist:delete");
	
	private final String permission;
	
	private Permission(String permission) {
		this.permission = permission;
	}
	
	public String getPermission() {
		return permission;
	}
}

