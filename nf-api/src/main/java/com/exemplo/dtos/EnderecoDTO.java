package com.exemplo.dtos;

public class EnderecoDTO {
  private String logradouro;
  private Long numero;
  private String bairro;
  private String cidade;
  private String estado;

  public EnderecoDTO(String logradouro,
      Long numero,
      String bairro,
      String cidade,
      String estado) {
      
    this.logradouro = logradouro;
    this.numero = numero;
    this.bairro = bairro;
    this.cidade = cidade;
    this.estado = estado;
  }

  public String getLogradouro() {
    return logradouro;
  }

  public Long getNumero() {
    return numero;
  }

  public String getBairro() {
    return bairro;
  }

  public String getCidade() {
    return cidade;
  }

  public String getEstado() {
    return estado;
  }
}