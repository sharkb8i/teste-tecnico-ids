package com.exemplo.exceptions;

public class RegistroNaoEncontradoException extends RuntimeException {
  public RegistroNaoEncontradoException(String message) {
    super(message);
  }

  public RegistroNaoEncontradoException(String message, Throwable cause) {
    super(message, cause);
  }

  public RegistroNaoEncontradoException(Throwable cause) {
    super(cause);
  }
}