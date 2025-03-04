package com.exemplo.entities;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "item_nota_fiscal")
public class ItemNotaFiscal extends PanacheEntityBase {

    @Id
    private UUID id;

    @Column(name = "valor_unitario")
    public BigDecimal valorUnitario;
    
    public BigDecimal quantidade;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nota_fiscal_id", nullable = false)
    public NotaFiscal nf;
    
    @ManyToOne
    @JoinColumn(name = "produto_id")
    public Produto produto;

    public ItemNotaFiscal() { }

    public ItemNotaFiscal(UUID id,
            Produto produto,
            BigDecimal valorUnitario,
            BigDecimal quantidade,
            NotaFiscal nf) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.produto = produto;
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
        this.nf = nf;
    }

    public UUID getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public NotaFiscal getNf() {
        return nf;
    }

    public void setNf(NotaFiscal nf) {
        this.nf = nf;
    }
    
    public BigDecimal calcularValorTotal() {
        return valorUnitario.multiply(quantidade);
    }
}