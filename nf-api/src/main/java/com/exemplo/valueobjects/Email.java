package com.exemplo.valueobjects;

import java.util.regex.Pattern;

public class Email {
  private static final Pattern EMAIL_REGEX = Pattern.compile("^[\\w\\.-]+@[\\w\\.-]+\\.\\w+$");

  private final String endereco;

  public Email(String endereco) {
    if (endereco == null || !EMAIL_REGEX.matcher(endereco).matches())
      throw new IllegalArgumentException("Endereço de e-mail inválido: " + endereco);
    
    this.endereco = endereco;
  }

  public String getEndereco() {
    return endereco;
  }

  @Override
  public String toString() {
    return endereco;
  }
}