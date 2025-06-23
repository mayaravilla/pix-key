package com.example.pix.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerAdvice {


    @ExceptionHandler(value = {DomainException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorTemplateDTO> handlerException(DomainException ex) {
        var errorResponse = ErrorTemplateDTO.builder()
                .status(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .message(ex.getMessage())
                .erro(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorTemplateDTO> notFoundException(NotFoundException ex) {
        var errorResponse = ErrorTemplateDTO.builder()
                .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .message(ex.getMessage())
                .erro(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
