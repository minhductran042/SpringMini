package com.minhductran.tutorial.minhductran.exception;

import java.util.Date;

public class ResponseError {
    private Date timestampe;
    private int status;
    private String path;// Đường dẫn của request lỗi
    private String error;
    private String message;

    public Date getTimestampe() {
        return timestampe;
    }

    public void setTimestampe(Date timestampe) {
        this.timestampe = timestampe;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
