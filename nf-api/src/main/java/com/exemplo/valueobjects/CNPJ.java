package com.exemplo.valueobjects;

import java.util.InputMismatchException;
import java.util.regex.Pattern;

public class CNPJ {
    private static final Pattern CNPJ_REGEX = Pattern.compile("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}");

    private final String numero;

    public CNPJ(String numero) {
        
        if (numero == null || numero.isBlank())
            throw new IllegalArgumentException("CNPJ não pode ser nulo ou vazio.");

        String cnpjLimpo = limparCnpj(numero);

        if (numero.equals(cnpjLimpo)) {
            if (cnpjLimpo.length() != 14)
                throw new IllegalArgumentException("CNPJ inválido: deve conter exatamente 14 dígitos.");

            this.numero = formatarCnpj(cnpjLimpo);
        } else {
            if (!CNPJ_REGEX.matcher(numero).matches())
                throw new IllegalArgumentException("Formato de CNPJ inválido: esperado XX.XXX.XXX/XXXX-XX.");
            
            this.numero = numero;
        }
    }

    private String limparCnpj(String cnpj) {
        return cnpj == null ? "" : cnpj.replaceAll("\\D", "");
    }

    public static boolean validarCnpj(String cnpj) {
        cnpj = cnpj.replaceAll("[^0-9]", "");
        
        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}"))
            return false;
        
        try {
            int soma = 0;
            int peso = 5;
            for (int i = 0; i < 12; i++) {
                soma += (cnpj.charAt(i) - '0') * peso;
                peso = (peso == 2) ? 9 : peso - 1;
            }
            int digito1 = (soma % 11 < 2) ? 0 : (11 - soma % 11);

            soma = 0;
            peso = 6;
            for (int i = 0; i < 13; i++) {
                soma += (cnpj.charAt(i) - '0') * peso;
                peso = (peso == 2) ? 9 : peso - 1;
            }
            int digito2 = (soma % 11 < 2) ? 0 : (11 - soma % 11);


            return cnpj.charAt(12) - '0' == digito1 && cnpj.charAt(13) - '0' == digito2;
        } catch (InputMismatchException e) {
            return false;
        }
    }

    public String getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        return formatarCnpj(numero);
    }

    private String formatarCnpj(String cnpj) {
        return cnpj.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }
}