package com.exemplo.dtos;

import com.exemplo.entities.EnumBase;
import com.exemplo.entities.EnumSituacaoFornecedor;
import com.exemplo.valueobjects.Email;
import com.exemplo.valueobjects.Telefone;

public class EditarFornecedorDTO {
  private String codigo;
  private String razaoSocial;
  private String email;
  private String telefone;
  private EnumSituacaoFornecedor situacao;

  public EditarFornecedorDTO(String codigo,
      String razaoSocial,
      String email,
      String telefone,
      String situacao) {
    this.codigo = codigo;
    this.razaoSocial = razaoSocial;
    this.email = email == null ? email : new Email(email).getEndereco();
    this.telefone = telefone == null ? telefone : new Telefone(telefone).getNumero();
    this.situacao = situacao == null ? 
      EnumSituacaoFornecedor.ATIVO : 
      EnumBase.fromString(EnumSituacaoFornecedor.class, situacao);
  }

  public String getCodigo() {
    return codigo;
  }

  public String getRazaoSocial() {
    return razaoSocial;
  }

  public String getEmail() {
    return email;
  }

  public String getTelefone() {
    return telefone;
  }

  public EnumSituacaoFornecedor getSituacao() {
    return situacao;
  }
}