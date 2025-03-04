package com.exemplo.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "nota_fiscal")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotaFiscal extends PanacheEntityBase {

    @Id
    private UUID id;

    @Column(name = "numero_nota")
    public Long numeroNota;

    @Column(name = "valor_total")
    public BigDecimal valorTotal;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id")
    public Fornecedor fornecedor;

    @OneToMany(mappedBy = "nf", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ItemNotaFiscal> itens;

    @Column(name = "data_emissao")
    public LocalDateTime dataEmissao;

    @Column(name = "data_exclusao")
    public LocalDateTime dataExclusao;

    public NotaFiscal() { }

    public NotaFiscal(UUID id,
            Endereco endereco,
            BigDecimal valorTotal,
            Fornecedor fornecedor,
            List<ItemNotaFiscal> itens) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.numeroNota = gerarNumeroNota();
        this.endereco = endereco;
        this.valorTotal = valorTotal;
        this.fornecedor = fornecedor;
        this.itens = itens;
    }

    public NotaFiscal(UUID id,
            Endereco endereco,
            BigDecimal valorTotal,
            List<ItemNotaFiscal> itens,
            LocalDateTime dataEmissao) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.numeroNota = gerarNumeroNota();
        this.endereco = endereco;
        this.valorTotal = valorTotal;
        this.itens = itens;
        this.dataEmissao = dataEmissao;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void addItem(ItemNotaFiscal item) {
        // this.itens.add(item);
        // item.setNotaFiscal(this);
        // this.fornecedor.update();
    }

    public void removeItem(ItemNotaFiscal item) {
        // this.itens.remove(item);
        // item.setNotaFiscal(null);
        // this.fornecedor.update();
    }

    public Long getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(Long numeroNota) {
        if (numeroNota > 999999999L)
            throw new IllegalArgumentException("O número da nota não pode ser maior que 999.999.999.");
        
        this.numeroNota = numeroNota;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemNotaFiscal> getItens() {
        return itens;
    }

    public void setItens(List<ItemNotaFiscal> itens) {
        this.itens = itens;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public LocalDateTime getDataExclusao() {
        return dataExclusao;
    }

    public void setDataExclusao(LocalDateTime dataExclusao) {
        this.dataExclusao = dataExclusao;
    }
    
    public static Long gerarNumeroNota() {
        NotaFiscal ultimaNota = NotaFiscal.find("order by numeroNota desc").firstResult();
        
        if (ultimaNota == null)
            return 1L;
        
        Long numeroUltimaNota = ultimaNota.getNumeroNota();
        
        if (numeroUltimaNota >= 999999999L)
            return 1L;
        else
            return numeroUltimaNota + 1;
    }
}