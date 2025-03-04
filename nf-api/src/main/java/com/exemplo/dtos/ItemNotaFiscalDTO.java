package com.exemplo.dtos;

import java.math.BigDecimal;

public class ItemNotaFiscalDTO {
    private String codProduto;
    private BigDecimal valorUnitario;
    private BigDecimal quantidade;

    public ItemNotaFiscalDTO(String codProduto, BigDecimal valorUnitario, BigDecimal quantidade) {
        this.codProduto = codProduto;
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
    }

    public String getCodProduto() {
        return codProduto;
    }
    
    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }
    
    public BigDecimal getQuantidade() {
        return quantidade;
    }
}