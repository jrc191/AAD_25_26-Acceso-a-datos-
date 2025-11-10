package com.jramcon398.jrc.util;

public class UnsafeException extends RuntimeException {
    public UnsafeException(String message) {
        super(message);
    }

    public UnsafeException(String message, Throwable cause) {
        super(message, cause);
    }
}