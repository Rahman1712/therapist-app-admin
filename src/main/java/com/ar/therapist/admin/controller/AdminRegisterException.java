package com.ar.therapist.admin.controller;

import com.ar.therapist.admin.exception.ErrorResponse;

public class AdminRegisterException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ErrorResponse errorResponse;

	public AdminRegisterException(ErrorResponse errorResponse) {
		this.errorResponse = errorResponse;
	}

	public ErrorResponse getErrorResponse() {
		return errorResponse;
	}
}
