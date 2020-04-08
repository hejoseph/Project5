package com.safetyalert.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.safetyalert.exception.MedicalRecordAlreadyExists;
import com.safetyalert.exception.PersonAlreadyExists;
import com.safetyalert.exception.StationAlreadyExists;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LogManager.getLogger("GlobalExceptionHandler");
	
	@ExceptionHandler({ StationAlreadyExists.class})
    public final ResponseEntity<?> handleException(Exception ex, WebRequest request) {
		logger.info("handle exception station already exists ");
		HttpHeaders headers = new HttpHeaders();
		HttpStatus status = HttpStatus.FOUND;
		StationAlreadyExists e = (StationAlreadyExists)ex;
//		List<String> errors = Collections.singletonList(ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), headers, status);
	}
	
	@ExceptionHandler({ MedicalRecordAlreadyExists.class})
    public final ResponseEntity<?> createSameMedicalException(Exception ex, WebRequest request) {
		logger.info("handle exception station already exists ");
		HttpHeaders headers = new HttpHeaders();
		HttpStatus status = HttpStatus.FOUND;
		MedicalRecordAlreadyExists e = (MedicalRecordAlreadyExists)ex;
//		List<String> errors = Collections.singletonList(ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), headers, status);
	}
	
	@ExceptionHandler({ PersonAlreadyExists.class})
    public final ResponseEntity<?> createSamePersonException(Exception ex, WebRequest request) {
		logger.info("handle exception station already exists ");
		HttpHeaders headers = new HttpHeaders();
		HttpStatus status = HttpStatus.FOUND;
		PersonAlreadyExists e = (PersonAlreadyExists)ex;
//		List<String> errors = Collections.singletonList(ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), headers, status);
	}
	
}


