package com.exemplo.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {
    
    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof RegistroDuplicadoException)
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new RespostaErro("Registro Duplicado", exception.getMessage()))
                .build();
        else if (exception instanceof RegistroNaoEncontradoException)
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new RespostaErro("Registro Não Encontrado", exception.getMessage()))
                .build();
        else if (exception instanceof RegistroInativadoException)
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new RespostaErro("Registro Inativado", exception.getMessage()))
                .build();
        else if (exception instanceof RegistroNaoExcludenteException)
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new RespostaErro("Registro Não Excludente", exception.getMessage()))
                .build();
        else if (exception instanceof ValorTotalInvalidoException)
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new RespostaErro("Valo Total Inválido", exception.getMessage()))
                .build();
        else if (exception instanceof IllegalArgumentException) {
            String mensagemErro = exception.getMessage();
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new RespostaErro("Erro de Validação", mensagemErro))
                .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new RespostaErro("Erro Interno", "Ocorreu um erro inesperado: " + exception + "."))
                .build();
    }
}