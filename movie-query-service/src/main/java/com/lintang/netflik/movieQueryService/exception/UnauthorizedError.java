package com.lintang.netflik.movieQueryService.exception;

public class UnauthorizedError  extends RuntimeException{
    public UnauthorizedError(String message) {
        super(message);
    }

    public UnauthorizedError(String message, Throwable cause){
        super(message, cause);
    }

    public UnauthorizedError( Throwable cause) {
        super(cause);
    }

}
