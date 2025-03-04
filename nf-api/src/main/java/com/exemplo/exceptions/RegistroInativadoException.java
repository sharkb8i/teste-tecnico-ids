package com.exemplo.exceptions;

public class RegistroInativadoException extends RuntimeException {
    public RegistroInativadoException(String message) {
        super(message);
    }

    public RegistroInativadoException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistroInativadoException(Throwable cause) {
        super(cause);
    }   
}