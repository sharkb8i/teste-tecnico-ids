package com.exemplo.dtos;

import com.exemplo.entities.EnumBase;
import com.exemplo.entities.EnumSituacaoFornecedor;
import com.exemplo.valueobjects.CNPJ;
import com.exemplo.valueobjects.Email;
import com.exemplo.valueobjects.Telefone;

public class CriarFornecedorDTO {
    private String codigo;
    private String razaoSocial;
    private Email email;
    private Telefone telefone;
    private CNPJ cnpj;
    private EnumSituacaoFornecedor situacao;

    public CriarFornecedorDTO(String codigo,
            String razaoSocial,
            String email,
            String telefone,
            String cnpj,
            String situacao) {
        this.codigo = codigo;
        this.razaoSocial = razaoSocial;
        this.email = new Email(email);
        this.telefone = new Telefone(telefone);
        this.cnpj = new CNPJ(cnpj);
        this.situacao = situacao == null ? 
            EnumSituacaoFornecedor.ATIVO : 
            EnumBase.fromString(EnumSituacaoFornecedor.class, situacao);
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

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }

    public CNPJ getCnpj() {
        return cnpj;
    }

    public void setCnpj(CNPJ cnpj) {
        this.cnpj = cnpj;
    }

    public EnumSituacaoFornecedor getSituacao() {
        return situacao;
    }

    public void setSituacao(EnumSituacaoFornecedor situacao) {
        this.situacao = situacao;
    }
}