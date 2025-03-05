package com.exemplo.dtos;

import java.math.BigDecimal;
import java.util.List;

public class EditarNotaFiscalDTO {
  private String codFornecedor;
  private BigDecimal valorTotal;
  private EnderecoDTO endereco;
  private List<ItemNotaFiscalDTO> itens;
  
  public EditarNotaFiscalDTO(String codFornecedor,
        BigDecimal valorTotal,
        EnderecoDTO endereco, 
        List<ItemNotaFiscalDTO> itens) {
    this.codFornecedor = codFornecedor;
    this.valorTotal = valorTotal;
    this.endereco = endereco;
    this.itens = itens;
  }

  public String getCodFornecedor() {
    return codFornecedor;
  }

  public BigDecimal getValorTotal() {
    return valorTotal;
  }

  public EnderecoDTO getEndereco() {
    return endereco;
  }

  public List<ItemNotaFiscalDTO> getItens() {
    return itens;
  }
}