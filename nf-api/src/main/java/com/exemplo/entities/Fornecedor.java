package com.exemplo.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Fornecedor extends PanacheEntityBase {
    
  @Id
  private UUID id;

  @Column(unique = true)
  public String codigo;
  
  @Column(name = "razao_social")
  public String razaoSocial;

  public String email;
  public String telefone;
  
  @Column(unique = true)
  public String cnpj;

  @Enumerated(EnumType.STRING)
  public EnumSituacaoFornecedor situacao;

  @Column(name = "data_baixa")
  public LocalDateTime dataBaixa;

  public Fornecedor() {}
  
  public Fornecedor(UUID id,
      String codigo,
      String razaoSocial,
      String email,
      String telefone,
      String cnpj,
      EnumSituacaoFornecedor situacao) {
    this.id = (id == null) ? UUID.randomUUID() : id;
    this.codigo = codigo;
    this.razaoSocial = razaoSocial;
    this.email = email;
    this.telefone = telefone;
    this.cnpj = cnpj;
    this.situacao = situacao;
    this.dataBaixa = null;
  }

  public Fornecedor(UUID id,
      String codigo,
      String razaoSocial,
      String email,
      String telefone,
      String cnpj,
      EnumSituacaoFornecedor situacao,
      LocalDateTime dataBaixa) {
    this.id = (id == null) ? UUID.randomUUID() : id;
    this.codigo = codigo;
    this.razaoSocial = razaoSocial;
    this.email = email;
    this.telefone = telefone;
    this.cnpj = cnpj;
    this.situacao = situacao;
    this.dataBaixa = dataBaixa;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getRazaoSocial() {
    return razaoSocial;
  }

  public void setRazaoSocial(String razaoSocial) {
    this.razaoSocial = razaoSocial;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getCnpj() {
    return cnpj;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

  public EnumSituacaoFornecedor getSituacao() {
    return situacao;
  }

  public void setSituacao(EnumSituacaoFornecedor situacao) {
    this.situacao = situacao;
  }

  public LocalDateTime getDataBaixa() {
    return dataBaixa;
  }

  public void setDataBaixa(LocalDateTime dataBaixa) {
    this.dataBaixa = dataBaixa;
  }
}