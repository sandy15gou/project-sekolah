package com.sandy.project.exception;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.sandy.project.dto.ErrorResponseDTO;
import com.sandy.project.enums.ErrorCode;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

	// =========================================================
	// 400 - Data tidak ditemukan
	// =========================================================
	@ExceptionHandler(ResourceNotFoundException.class)
	protected ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		ErrorResponseDTO errorResponse = ErrorResponseDTO.of("data not found", details, ErrorCode.DATA_NOT_FOUND, HttpStatus.BAD_REQUEST);
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// =========================================================
	// 400 - Validasi field (dari @Validated di service/controller)
	// =========================================================
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<ErrorResponseDTO> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			details.add(violation.getMessage());
		}
		ErrorResponseDTO errorResponse = ErrorResponseDTO.of("invalid data", details, ErrorCode.INVALID_DATA, HttpStatus.BAD_REQUEST);
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// =========================================================
	// 400 - Validasi body request (@Valid di @RequestBody)
	// =========================================================
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<String> details = new ArrayList<>();
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			details.add(error.getDefaultMessage());
		}
		ErrorResponseDTO errorResponse = ErrorResponseDTO.of("invalid data", details, ErrorCode.INVALID_DATA, HttpStatus.BAD_REQUEST);
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// =========================================================
	// 400 - Body JSON tidak bisa dibaca / malformed JSON
	// =========================================================
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.warn("HttpMessageNotReadableException: {}", ex.getMessage());
		List<String> details = new ArrayList<>();
		details.add("Format request tidak valid. Pastikan body berupa JSON yang benar.");
		ErrorResponseDTO errorResponse = ErrorResponseDTO.of("malformed JSON request", details, ErrorCode.INVALID_DATA, HttpStatus.BAD_REQUEST);
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// =========================================================
	// 405 - HTTP method tidak didukung (GET saat endpoint butuh POST, dll)
	// =========================================================
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.warn("HttpRequestMethodNotSupportedException: {}", ex.getMessage());
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		ErrorResponseDTO errorResponse = ErrorResponseDTO.of("method not supported", details, ErrorCode.INVALID_DATA, HttpStatus.METHOD_NOT_ALLOWED);
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
	}

	// =========================================================
	// 400 - Query parameter wajib tidak dikirim
	// =========================================================
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		ErrorResponseDTO errorResponse = ErrorResponseDTO.of("missing query parameter", details, ErrorCode.INVALID_DATA, HttpStatus.BAD_REQUEST);
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// =========================================================
	// 415 - Content-Type tidak didukung (misal kirim XML ke endpoint JSON)
	// =========================================================
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.warn("HttpMediaTypeNotSupportedException: {}", ex.getMessage());
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		ErrorResponseDTO errorResponse = ErrorResponseDTO.of("unsupported media type", details, ErrorCode.INVALID_DATA, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
	}

	// =========================================================
	// 400 - Tipe parameter tidak sesuai (misal id=abc saat butuh Long)
	// =========================================================
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add("Invalid parameter format: " + ex.getName());
		ErrorResponseDTO errorResponse = ErrorResponseDTO.of("invalid parameter type", details, ErrorCode.INVALID_DATA, HttpStatus.BAD_REQUEST);
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// =========================================================
	// 409 - Konflik data di database (duplicate key, FK violation, dll)
	// =========================================================
	@ExceptionHandler(DataIntegrityViolationException.class)
	protected ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
		// Log pesan teknikal DB hanya di server — jangan dikirim ke client
		log.warn("DataIntegrityViolationException: {}", ex.getMostSpecificCause().getMessage());
		List<String> details = new ArrayList<>();
		details.add("Data sudah ada atau melanggar aturan integritas data.");
		ErrorResponseDTO errorResponse = ErrorResponseDTO.of("data conflict or already exists", details, ErrorCode.INVALID_DATA, HttpStatus.CONFLICT);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
	}

	// =========================================================
	// 403 - Akses ditolak (role tidak sesuai / belum login)
	// =========================================================
	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
		log.warn("AccessDeniedException - Request: {} | Message: {}", request.getDescription(false), ex.getMessage());
		List<String> details = new ArrayList<>();
		details.add("Anda tidak memiliki izin untuk mengakses resource ini.");
		ErrorResponseDTO errorResponse = ErrorResponseDTO.of("access denied", details, ErrorCode.INTERNAL_ERROR, HttpStatus.FORBIDDEN);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
	}

	// =========================================================
	// 500 - Safety net: menangkap SEMUA error yang tidak tertangani di atas
	// =========================================================
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex, WebRequest request) {
		// Log full stack trace di server untuk debugging — JANGAN kirim ke client!
		log.error("Unhandled exception on request [{}]: {}", request.getDescription(false), ex.getMessage(), ex);
		List<String> details = new ArrayList<>();
		details.add("Terjadi kesalahan internal pada server. Silakan coba beberapa saat lagi.");
		ErrorResponseDTO errorResponse = ErrorResponseDTO.of("internal server error", details, ErrorCode.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}
