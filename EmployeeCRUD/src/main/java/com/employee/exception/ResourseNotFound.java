package com.employee.exception;

public class ResourseNotFound extends RuntimeException{

	private static final long serialVersionUID = 1L;

	
	public ResourseNotFound(String message) {
		super(message);
	}
}
