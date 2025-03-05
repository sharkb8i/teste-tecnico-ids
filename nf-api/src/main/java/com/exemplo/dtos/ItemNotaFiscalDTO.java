package com.exemplo.dtos;

import java.math.BigDecimal;

public class ItemNotaFiscalDTO {
    private String id;
    private String codProduto;
    private BigDecimal valorUnitario;
    private BigDecimal quantidade;

    public ItemNotaFiscalDTO() { }

    public ItemNotaFiscalDTO(String codProduto, BigDecimal valorUnitario, BigDecimal quantidade) {
        this.codProduto = codProduto;
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
    }

    public ItemNotaFiscalDTO(String id, String codProduto, BigDecimal valorUnitario, BigDecimal quantidade) {
        this.id = id;
        this.codProduto = codProduto;
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
    }

    public String getId() {
        return id;
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