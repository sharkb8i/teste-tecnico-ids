package com.exemplo.dtos;

import com.exemplo.entities.EnumBase;
import com.exemplo.entities.EnumSituacaoProduto;

public class CriarProdutoDTO {
  private String codigo;
  private String descricao;
  private EnumSituacaoProduto situacao;

  public CriarProdutoDTO(String codigo, String descricao, String situacao) {
    this.codigo = codigo;
    this.descricao = descricao;
    this.situacao = situacao == null ? 
      EnumSituacaoProduto.ATIVO : 
      EnumBase.fromString(EnumSituacaoProduto.class, situacao);
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