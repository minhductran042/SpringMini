package com.minhductran.tutorial.minhductran.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleValidationException(Exception error, WebRequest request) { // request được dùng đê lấy request
        ResponseError errorResponse = new ResponseError();

        errorResponse.setTimestamp(new Date());

        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());

        errorResponse.setPath(request.getDescription(false)
                .replace("uri=",""));//Lấy đường dẫn của request lỗi

        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());

        String message = error.getMessage();
        int start = message.lastIndexOf("[");
        int end = message.lastIndexOf("]");
        message = message.substring(start+1, end -1); // Lấy message lỗi

        errorResponse.setMessage(message); // Lấy message lỗi
        return errorResponse;
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError handleResourceNotFoundException(ResourceNotFoundException error, WebRequest request) { // request được dùng để lấy path
        ResponseError responseError = new ResponseError();
        responseError.setTimestamp(new Date());
        responseError.setStatus(HttpStatus.NOT_FOUND.value());
        responseError.setPath(request.getDescription(false).replace("uri=", "")); // Lấy đường dẫn của request lỗi
        responseError.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        String message = error.getMessage();
        int start = message.lastIndexOf("[");
        int end = message.lastIndexOf("]");
        message = message.substring(start+1, end -1); // Lấy message lỗi

        responseError.setMessage(message); // Lấy message lỗi
        return responseError;

    }


}
