package com.makersharks.exception.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
	private final String resourceName;
	private final String fieldName;
	private final String fieldValue;
	public ResourceNotFoundException(String resourceName, String fieldName, String location) {
		super(String.format("%s not found with %s: '%s'",resourceName,fieldName,location));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = location;
	}


}
