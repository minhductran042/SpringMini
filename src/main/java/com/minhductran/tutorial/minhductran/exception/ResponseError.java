package com.minhductran.tutorial.minhductran.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ResponseError {
    private Date timestampe;
    private int status;
    private String path;// Đường dẫn của request lỗi
    private String error;
    private String message;
}
