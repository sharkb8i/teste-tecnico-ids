package com.exemplo.dtos;

import com.exemplo.entities.EnumSituacaoProduto;

public class ProdutoDTO {

  private Long id;
  private String codigo;
  private String descricao;
  private EnumSituacaoProduto situacao;

  public ProdutoDTO(String codigo, String descricao, EnumSituacaoProduto situacao) {
    this.codigo = codigo;
    this.descricao = descricao;
    this.situacao = situacao;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public EnumSituacaoProduto getSituacao() {
    return situacao;
  }

  public void setSituacao(EnumSituacaoProduto situacao) {
    this.situacao = situacao;
  }
}