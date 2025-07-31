package com.minhductran.tutorial.minhductran.dto.response;

import org.springframework.http.ResponseEntity;

public class ResponseErrorEntity extends ApiResponse {

    public ResponseErrorEntity(int status, String message) {
        super(status, message);
    }
}
