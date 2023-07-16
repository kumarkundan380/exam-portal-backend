package com.exam.exception;

public class ResourceNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String resourceName,String fieldName,Long fieldValue) {
		super(String.format("%s Not Found with %s : %d", resourceName, fieldName, fieldValue));
	}

}
