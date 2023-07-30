package com.lintang.netflik.movieservice.exception;

public class InternalServerEx extends  RuntimeException{
    public InternalServerEx(String message) {
        super(message);
    }

    public InternalServerEx(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerEx(Throwable cause) {
        super(cause);
    }
}
