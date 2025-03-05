package com.exemplo.dtos;

import com.exemplo.entities.EnumBase;
import com.exemplo.entities.EnumSituacaoProduto;

public class EditarProdutoDTO {
  private String codigo;
  private String descricao;
  private EnumSituacaoProduto situacao;

  public EditarProdutoDTO(String codigo, String descricao, String situacao) {
      this.codigo = codigo;
      this.descricao = descricao;
      this.situacao = situacao == null ? 
    EnumSituacaoProduto.ATIVO : 
    EnumBase.fromString(EnumSituacaoProduto.class, situacao);
  }

  public String getCodigo() {
    return codigo;
  }

  public String getDescricao() {
    return descricao;
  }

  public EnumSituacaoProduto getSituacao() {
    return situacao;
  }
}