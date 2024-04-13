package com.example.common;

public enum ErrorCode {

    SUCCESS(0, "ok");

    private final int code;
    private final String message;

    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {return code;}
    public String getMessage() {return message;}

}
