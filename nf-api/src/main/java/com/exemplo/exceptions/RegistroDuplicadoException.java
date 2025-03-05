package com.exemplo.exceptions;

public class RegistroDuplicadoException extends RuntimeException {
  public RegistroDuplicadoException(String message) {
    super(message);
  }

  public RegistroDuplicadoException(String message, Throwable cause) {
    super(message, cause);
  }

  public RegistroDuplicadoException(Throwable cause) {
    super(cause);
  }
}