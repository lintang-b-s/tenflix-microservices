package com.lintang.netflik.orderservice.exception;


//import com.google.rpc.Status;
import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GrpcExceptionAdvice {
    @GrpcExceptionHandler(ResourceNotFoundException.class)
    public RuntimeException handleResourceNotFoundException(ResourceNotFoundException e) {
        Status status = Status.NOT_FOUND.withDescription(e.getMessage()).withCause(e);

        return status.asRuntimeException();
    }

    @GrpcExceptionHandler(Exception.class)
    public RuntimeException handleException(Exception e) {
        Status status = Status.INTERNAL.withCause(e).withDescription("Internal Server Error");
        return status.asRuntimeException();
    }


    @GrpcExceptionHandler(BadRequestException.class)
    public RuntimeException handleBadRequestException(BadRequestException e) {
        Status status = Status.INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e);
        return status.asRuntimeException();
    }

}
