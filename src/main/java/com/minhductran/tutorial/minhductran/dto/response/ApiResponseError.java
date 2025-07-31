package com.minhductran.tutorial.minhductran.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponseError {
    private Date timestamp;
    private int status;
    private String path;// Đường dẫn của request lỗi
    private String error;
    private String message;

    public ApiResponseError(int status, String message) {
        this.timestamp = new Date();
        this.status = status;
        this.message = message;
        this.error = "Error";
    }
}
