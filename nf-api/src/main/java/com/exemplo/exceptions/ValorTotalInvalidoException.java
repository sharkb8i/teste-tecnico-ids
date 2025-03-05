package com.exemplo.exceptions;

public class ValorTotalInvalidoException extends RuntimeException {
  public ValorTotalInvalidoException(String message) {
    super(message);
  }

  public ValorTotalInvalidoException(String message, Throwable cause) {
    super(message, cause);
  }

  public ValorTotalInvalidoException(Throwable cause) {
    super(cause);
  }
}