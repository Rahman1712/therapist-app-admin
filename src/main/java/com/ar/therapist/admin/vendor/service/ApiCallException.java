package com.ar.therapist.admin.vendor.service;

public class ApiCallException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private ErrorResponse errorResponse;
	
	public ApiCallException(ErrorResponse errorResponse) {
		this.errorResponse = errorResponse;
	}

	public ErrorResponse getErrorResponse() {
		return errorResponse;
	}
}
