package com.exemplo.exceptions;

public class RegistroNaoExcludenteException extends RuntimeException {
  public RegistroNaoExcludenteException(String message) {
    super(message);
  }

  public RegistroNaoExcludenteException(String message, Throwable cause) {
    super(message, cause);
  }

  public RegistroNaoExcludenteException(Throwable cause) {
    super(cause);
  }
}