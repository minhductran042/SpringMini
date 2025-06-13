package com.minhductran.tutorial.minhductran.exception;

import com.minhductran.tutorial.minhductran.dto.request.APIRespone;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<APIRespone>  handlingRunTimeException(RuntimeException exception) { // exception duoc inject boi Spring
        APIRespone apiRespone = new APIRespone();
        apiRespone.setCode(1001);
        apiRespone.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(apiRespone);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<String> handlingMethodArguementNotValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(exception.getFieldError().getDefaultMessage());
    }
}
