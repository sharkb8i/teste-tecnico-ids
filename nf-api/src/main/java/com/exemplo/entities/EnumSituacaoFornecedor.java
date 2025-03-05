package com.exemplo.entities;

public enum EnumSituacaoFornecedor implements EnumBase {
  ATIVO, BAIXADO, SUSPENSO;

  @Override
  public String getDescricao() {
    return name();
  }
}