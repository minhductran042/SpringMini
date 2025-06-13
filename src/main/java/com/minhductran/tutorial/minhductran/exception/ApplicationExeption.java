package com.minhductran.tutorial.minhductran.exception;

public class ApplicationExeption extends RuntimeException {
    private ErrorCode errorCode;

    public ApplicationExeption(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }


    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
