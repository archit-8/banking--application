package com.example.demo.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	// ---------------- BUSINESS EXCEPTIONS ----------------
	@ExceptionHandler({
			InvalidAccountException.class,
			InsufficientAmountException.class,
			IllegalArgumentException.class
	})
	public String handleBusinessExceptions(Exception ex, Model model) {
		model.addAttribute("error", ex.getMessage());
		return "error";
	}

	// ---------------- VALIDATION ERRORS (@Valid DTO) ----------------
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public String handleValidationException(
			MethodArgumentNotValidException ex,
			Model model) {

		String errorMsg = ex.getBindingResult()
				.getFieldError()
				.getDefaultMessage();

		model.addAttribute("error", errorMsg);
		return "error";
	}

	// ---------------- DATABASE CONSTRAINT ERRORS ----------------
	@ExceptionHandler(DataIntegrityViolationException.class)
	public String handleDataIntegrity(
			DataIntegrityViolationException ex,
			Model model) {

		model.addAttribute(
				"error",
				"Duplicate or invalid data. Please check your input."
		);
		return "error";
	}

	// ---------------- BEAN VALIDATION (SERVICE LEVEL) ----------------
	@ExceptionHandler(ConstraintViolationException.class)
	public String handleConstraintViolation(
			ConstraintViolationException ex,
			Model model) {

		model.addAttribute("error", ex.getMessage());
		return "error";
	}

	// ---------------- FALLBACK (ANY OTHER ERROR) ----------------
	@ExceptionHandler(Exception.class)
	public String handleAll(Exception ex, Model model) {

		// 🔥 IMPORTANT: log the exception
		ex.printStackTrace();

		model.addAttribute(
				"error",
				"Something went wrong. Please try again later."
		);
		return "error";
	}
}
