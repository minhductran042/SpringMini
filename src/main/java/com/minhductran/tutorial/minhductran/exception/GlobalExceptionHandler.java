package com.minhductran.tutorial.minhductran.exception;

import lombok.Getter;
import lombok.Setter;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
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

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseError handleAccessDeniedException(Exception error, WebRequest request) {
        ResponseError errorResponse = new ResponseError();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.FORBIDDEN.value());
        errorResponse.setError(HttpStatus.FORBIDDEN.getReasonPhrase());
        errorResponse.setMessage(error.getMessage());

        return errorResponse;
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError handleResourceNotFoundException(Exception error, WebRequest request) {
        ResponseError errorResponse = new ResponseError();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        errorResponse.setMessage(error.getMessage());

        return errorResponse;
    }

    @ExceptionHandler({InternalException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError InternalErrorRespone(Exception error, WebRequest request) {
        ResponseError errorResponse = new ResponseError();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        errorResponse.setMessage(error.getMessage());

        return errorResponse;
    }

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError BadRequestException(Exception error, WebRequest request) {
        ResponseError errorResponse = new ResponseError();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage(error.getMessage());

        return errorResponse;
    }


    @Getter
    @Setter
    public class ResponseError {
        private Date timestamp;
        private int status;
        private String path;// Đường dẫn của request lỗi
        private String error;
        private String message;
    }
}




