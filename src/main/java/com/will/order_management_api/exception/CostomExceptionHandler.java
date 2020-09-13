package com.will.order_management_api.exception;


import com.will.order_management_api.util.HelperMethods;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class CostomExceptionHandler {


    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handlerForIdNotFoundException(IdNotFoundException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                                                        .timeStamp(new Date())
                                                        .status(HttpStatus.NOT_FOUND.value())
                                                        .errorMesseage(e.getMessage())
                                                        .build();

        ResponseEntity<ExceptionResponse> errorMesseage =
                new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        return errorMesseage;
    }


    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handlerForInvalidDateFormatException(InvalidDateFormatException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .timeStamp(new Date())
                .status(HttpStatus.BAD_REQUEST.value())
                .errorMesseage(e.getMessage())
                .build();

        ResponseEntity<ExceptionResponse> errorMesseage =
                new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        return errorMesseage;
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handlerForInvalidItemNameException(InvalidItemNameException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .timeStamp(new Date())
                .status(HttpStatus.NOT_FOUND.value())
                .errorMesseage(e.getMessage())
                .build();

        ResponseEntity<ExceptionResponse> errorMesseage =
                new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        return errorMesseage;
    }


    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handlerForProductCreationException(ProductCreationException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .timeStamp(new Date())
                .status(HttpStatus.BAD_REQUEST.value())
                .errorMesseage(e.getMessage())
                .build();

        ResponseEntity<ExceptionResponse> errorMesseage =
                new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        return errorMesseage;
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handlerForIllegalArgumentException(IllegalArgumentException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .timeStamp(new Date())
                .status(HttpStatus.BAD_REQUEST.value())
                .errorMesseage(e.getMessage())
                .build();

        ResponseEntity<ExceptionResponse> errorMesseage =
                new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        return errorMesseage;
    }





}
