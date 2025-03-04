package com.exemplo.dtos;

public class EditarNotaFiscalDTO {
    private String codFornecedor;
    private EnderecoDTO endereco;
    
    public EditarNotaFiscalDTO(String codFornecedor, EnderecoDTO endereco) {
        this.codFornecedor = codFornecedor;
        this.endereco = endereco;
    }

    public String getCodFornecedor() {
        return codFornecedor;
    }

    public EnderecoDTO getEndereco() {
        return endereco;
    }
}