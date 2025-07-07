package com.minhductran.tutorial.minhductran.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ResponseEntity<T> {
    private int status;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    //DELETE
    public ResponseEntity(int status, String message) {
        this.status = status;
        this.message = message;
    }

    //GET, POST, PUT, PATCH
    public ResponseEntity(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
