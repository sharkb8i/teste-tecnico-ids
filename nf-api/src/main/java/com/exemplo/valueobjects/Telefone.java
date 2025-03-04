package com.exemplo.valueobjects;

public class Telefone {
    private final String numero;

    public Telefone(String numero) {
        if (numero == null || numero.isBlank())
            throw new IllegalArgumentException("Número de telefone não pode ser nulo ou vazio.");

        String telefoneLimpo = limparTelefone(numero);
        validarTamanho(telefoneLimpo);
        this.numero = formatarTelefone(telefoneLimpo);
    }
    
    private String limparTelefone(String telefone) {
        return telefone.replaceAll("\\D", "");
    }

    private void validarTamanho(String telefone) {
        if (telefone.length() == 10)
            return;
        
        if (telefone.length() == 11 && telefone.charAt(2) != '9')
            throw new IllegalArgumentException("Número de telefone inválido: celulares com 11 dígitos devem começar com 9 após DDD (2 dígitos).");
        
        throw new IllegalArgumentException("Número de telefone inválido: deve conter 10 ou 11 dígitos.");
    }

    private String formatarTelefone(String telefone) {
        if (telefone.length() == 10) {
            // Formato (XX) XXXX-XXXX
            return telefone.replaceFirst("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
        } else {
            // Formato (XX) 9XXXX-XXXX
            return telefone.replaceFirst("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        }
    }

    public String getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        return numero;
    }
}