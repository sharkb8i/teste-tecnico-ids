package com.exemplo.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Produto extends PanacheEntityBase {

    @Id
    private UUID id;

    public String codigo;
    public String descricao;

    @Enumerated(EnumType.STRING)
    public EnumSituacaoProduto situacao;

    @Column(name = "data_exclusao")
    public LocalDateTime dataExclusao;

    public Produto() {}
    
    public Produto(UUID id,
            String codigo,
            String descricao,
            EnumSituacaoProduto situacao) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.codigo = codigo;
        this.descricao = descricao;
        this.situacao = situacao;
        this.dataExclusao = null;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public EnumSituacaoProduto getSituacao() {
        return situacao;
    }

    public void setSituacao(EnumSituacaoProduto situacao) {
        this.situacao = situacao;
    }

    public LocalDateTime getDataExclusao() {
        return dataExclusao;
    }

    public void setDataExclusao(LocalDateTime dataExclusao) {
        this.dataExclusao = dataExclusao;
    }
    
    public boolean hasMovimentacao() {
        // Verifique se o produto tem movimentação no banco de dados
        return false;  // Simulação. Substituir por verificação real.
    }
}