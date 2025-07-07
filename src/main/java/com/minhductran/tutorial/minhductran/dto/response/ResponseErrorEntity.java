package com.minhductran.tutorial.minhductran.dto.response;

public class ResponseErrorEntity extends ResponseEntity {

    public ResponseErrorEntity(int status, String message) {
        super(status, message);
    }
}
