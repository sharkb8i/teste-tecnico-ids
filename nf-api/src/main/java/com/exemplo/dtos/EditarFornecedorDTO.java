package com.exemplo.dtos;

import com.exemplo.valueobjects.Email;
import com.exemplo.valueobjects.Telefone;

public class EditarFornecedorDTO {
    private String codigo;
    private String razaoSocial;
    private String email;
    private String telefone;
    private String dataBaixa;

    public EditarFornecedorDTO(String codigo,
            String razaoSocial,
            String email,
            String telefone,
            String dataBaixa) {
        this.codigo = codigo;
        this.razaoSocial = razaoSocial;
        this.email = email == null ? email : new Email(email).getEndereco();
        this.telefone = telefone == null ? telefone : new Telefone(telefone).getNumero();
        this.dataBaixa = dataBaixa;
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

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getDataBaixa() {
        return dataBaixa;
    }
}