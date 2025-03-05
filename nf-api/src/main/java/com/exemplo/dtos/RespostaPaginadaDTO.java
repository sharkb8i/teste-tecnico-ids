package com.exemplo.dtos;

import java.util.List;

public class RespostaPaginadaDTO<T> {
    
  private List<T> dados;
  private int pagina;
  private long totalResultados;
  private int tamanhoPagina;

  public RespostaPaginadaDTO(List<T> dados, int pagina, long totalResultados, int tamanhoPagina) {
    this.dados = dados;
    this.pagina = pagina;
    this.totalResultados = totalResultados;
    this.tamanhoPagina = tamanhoPagina;
  }

  public List<T> getDados() {
    return dados;
  }

  public void setDados(List<T> dados) {
    this.dados = dados;
  }

  public int getPagina() {
    return pagina;
  }

  public void setPagina(int pagina) {
    this.pagina = pagina;
  }

  public long getTotalResultados() {
    return totalResultados;
  }

  public void setTotalResultados(long totalResultados) {
    this.totalResultados = totalResultados;
  }

  public int getTamanhoPagina() {
    return tamanhoPagina;
  }

  public void setTamanhoPagina(int tamanhoPagina) {
    this.tamanhoPagina = tamanhoPagina;
  }
}