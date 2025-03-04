package com.exemplo.dtos;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntradaNotaFiscalDTO {
    private String dataEmissao;
    private String codFornecedor;
    
    @JsonProperty("endereco")
    private EnderecoDTO endereço;
    
    private BigDecimal valorTotal;

    private List<ItemNotaFiscalDTO> itens;

    public EntradaNotaFiscalDTO(Long numeroNota,
            String codFornecedor,
            EnderecoDTO endereço,
            BigDecimal valorTotal,
            List<ItemNotaFiscalDTO> itens) {
        this.codFornecedor = codFornecedor;
        this.endereço = endereço;
        this.valorTotal = valorTotal;
        this.itens = itens;
        this.dataEmissao = null;
    }
    
    public String getDataEmissao() {
        return dataEmissao;
    }
    
    public String getCodFornecedor() {
        return codFornecedor;
    }
    
    public EnderecoDTO getEndereço() {
        return endereço;
    }
    
    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    
    public List<ItemNotaFiscalDTO> getItens() {
        return itens;
    }
}