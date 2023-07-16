package com.exam.exception;

import java.util.HashMap;
import java.util.Map;

import com.exam.enums.Status;
import com.exam.response.ExamPortalErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExamPortalErrorResponse<?>> handleResourceNotFoundException(ResourceNotFoundException e){
		return new ResponseEntity<>(ExamPortalErrorResponse.builder()
				.status(Status.ERROR)
				.errorMessage(e.getMessage())
				.build(),HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExamPortalErrorResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
		Map<String,String> response = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError)error).getField();
			String message = error.getDefaultMessage();
			response.put(fieldName, message);
		});
		
		return new ResponseEntity<>(ExamPortalErrorResponse.builder()
				.status(Status.ERROR)
				.errorMessage(response)
				.build(),HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(ExamPortalException.class)
	public ResponseEntity<ExamPortalErrorResponse<?>> handleBlogAppException(ExamPortalException e){
		return new ResponseEntity<>(ExamPortalErrorResponse.builder()
				.status(Status.ERROR)
				.errorMessage(e.getMessage())
				.build(),HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExamPortalErrorResponse<?>> handleRemainingException(Exception e){
		return new ResponseEntity<>(ExamPortalErrorResponse.builder()
				.status(Status.ERROR)
				.errorMessage("Oops! Something went wrong. Please try again: "+e.getMessage())
				.build(),HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
}
