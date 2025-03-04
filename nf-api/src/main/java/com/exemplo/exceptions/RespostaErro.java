package com.exemplo.exceptions;

public class RespostaErro {
    private String erro;
    private String mensagem;

    public RespostaErro(String erro, String mensagem) {
        this.erro = erro;
        this.mensagem = mensagem;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}