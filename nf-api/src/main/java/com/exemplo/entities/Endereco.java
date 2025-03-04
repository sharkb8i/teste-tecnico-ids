package com.exemplo.entities;

import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Endereco extends PanacheEntityBase {

    @Id
    private UUID id;

    private String logradouro;
    private Long numero;
    private String bairro;
    private String cidade;
    private String estado;

    public Endereco() { }

    public Endereco(UUID id, String logradouro, Long numero, String bairro, String cidade, String estado) {
        this.id = (id == null)? UUID.randomUUID() : id;
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }

    public UUID getId() {
        return id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public Long getNumero() {
        return numero;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }
}