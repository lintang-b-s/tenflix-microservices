package com.lintang.netflik.movieservice.exception;

public class MovieDomainException extends  RuntimeException  {
    public MovieDomainException(String message) { super(message);}
    public MovieDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
