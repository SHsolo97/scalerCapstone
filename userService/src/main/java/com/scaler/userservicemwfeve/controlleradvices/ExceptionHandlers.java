package com.scaler.userservicemwfeve.controlleradvices;

import com.scaler.userservicemwfeve.dtos.ArithmeticExceptionDto;
import com.scaler.userservicemwfeve.dtos.ExceptionDto;
import com.scaler.userservicemwfeve.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<ArithmeticExceptionDto> handleArithmeticException() {
        ArithmeticExceptionDto arithmeticExceptionDto = new ArithmeticExceptionDto();
        arithmeticExceptionDto.setMessage("Something has gone wrong with an arithmetic operation."); // More specific message
        return new ResponseEntity<>(arithmeticExceptionDto, HttpStatus.BAD_REQUEST); // Changed to BAD_REQUEST
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ResponseEntity<ExceptionDto> handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException e) { // Return ExceptionDto
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage("Array index out of bounds: " + e.getMessage());
        exceptionDto.setDetail("An attempt was made to access an array with an invalid index.");
        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST); // Changed to BAD_REQUEST
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleException(Exception e) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage("An unexpected error occurred: " + e.getMessage());
        exceptionDto.setDetail("Please contact support if the issue persists.");
        return new ResponseEntity<>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR); // Changed to INTERNAL_SERVER_ERROR
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDto> handleNotFoundException(NotFoundException e) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage(e.getMessage());
        // Detail can be omitted if message is sufficient or set to a generic not found detail
        exceptionDto.setDetail("The requested resource was not found."); 
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }
}
