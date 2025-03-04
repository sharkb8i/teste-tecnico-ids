package com.exemplo.controllers;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

import com.exemplo.dtos.EditarNotaFiscalDTO;
import com.exemplo.dtos.EntradaNotaFiscalDTO;
import com.exemplo.dtos.ItemNotaFiscalDTO;
import com.exemplo.dtos.RespostaPaginadaDTO;
import com.exemplo.entities.NotaFiscal;
import com.exemplo.services.NotaFiscalService;

@Path("/notas-fiscais")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NotaFiscalController {

    @Inject
    private NotaFiscalService notaFiscalService;

    @POST
    @Transactional
    public Response entradaNotaFiscal(EntradaNotaFiscalDTO dto) {
        NotaFiscal notaFiscal = notaFiscalService.entradaNotaFiscal(dto);
        return Response.ok(notaFiscal).status(201).build();
    }

    @GET
    @Path("/{id}")
    public Response pesquisarNotaFiscalPorId(@PathParam("id") UUID id) {
        NotaFiscal notaFiscal = notaFiscalService.encontrarPorId(id);
        return Response.ok(notaFiscal).status(200).build();
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    public Response editarNotaFiscal(@PathParam("id") UUID id, EditarNotaFiscalDTO dto) {
        NotaFiscal notaFiscal = notaFiscalService.editarNotaFiscal(id, dto);
        return Response.ok(notaFiscal).build();
    }

    @PATCH
    @Path("/{id}/adicionar")
    @Transactional
    public Response adicionarItemNotaFiscal(@PathParam("id") UUID id, ItemNotaFiscalDTO dto) {
        notaFiscalService.adicionarItemNotaFiscal(id, dto);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/remover/{idItem}")
    @Transactional
    public Response removerItemNotaFiscal(@PathParam("id") UUID id, @PathParam("idItem") UUID idItem) {
        notaFiscalService.removerItemNotaFiscal(id, idItem);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response excluirNotaFiscal(@PathParam("id") UUID id) {
        notaFiscalService.excluirNotaFiscal(id);
        return Response.noContent().build();
    }

    @GET
    public RespostaPaginadaDTO<NotaFiscal> pesquisarNotasFiscais(@QueryParam("termo") String termo,
                                                                 @QueryParam("pagina") @DefaultValue("1") int pagina,
                                                                 @QueryParam("tamanhoPagina") @DefaultValue("10") int tamanhoPagina) {
        return notaFiscalService.pesquisarNotasFiscais(termo, pagina, tamanhoPagina);
    }
}