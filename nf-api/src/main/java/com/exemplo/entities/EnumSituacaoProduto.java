package com.exemplo.entities;

public enum EnumSituacaoProduto implements EnumBase {
  ATIVO, INATIVO;

  @Override
  public String getDescricao() {
    return name();
  }
}