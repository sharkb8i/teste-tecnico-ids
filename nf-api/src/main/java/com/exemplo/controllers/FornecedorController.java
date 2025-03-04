package com.exemplo.controllers;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

import com.exemplo.dtos.CriarFornecedorDTO;
import com.exemplo.dtos.EditarFornecedorDTO;
import com.exemplo.dtos.RespostaPaginadaDTO;
import com.exemplo.entities.Fornecedor;
import com.exemplo.services.FornecedorService;
import com.exemplo.valueobjects.CNPJ;

@Path("/fornecedores")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FornecedorController {
    
    @Inject
    private FornecedorService fornecedorService;

    @POST
    @Transactional
    public Response criarFornecedor(CriarFornecedorDTO dto) {
        if (dto == null || dto.getCnpj() == null || !CNPJ.validarCnpj(dto.getCnpj().getNumero()))
            throw new IllegalArgumentException("CNPJ inválido: " + dto.getCnpj().getNumero() + ".");

        Fornecedor fornecedor = fornecedorService.criarFornecedor(dto);
        return Response.ok(fornecedor).status(201).build();
    }

    @GET
    @Path("/{id}")
    public Response pesquisarFornecedorPorId(@PathParam("id") UUID id) {
        Fornecedor fornecedor = fornecedorService.encontrarPorId(id);
        return Response.ok(fornecedor).status(200).build();
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    public Response editarFornecedor(@PathParam("id") UUID id, EditarFornecedorDTO dto) {
        Fornecedor fornecedor = fornecedorService.editarFornecedor(id, dto);
        return Response.ok(fornecedor).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response excluirFornecedor(@PathParam("id") UUID id) {
        fornecedorService.excluirFornecedor(id);
        return Response.noContent().build();
    }

    @GET
    public RespostaPaginadaDTO<Fornecedor> pesquisarFornecedores(@QueryParam("termo") String termo,
                                                                 @QueryParam("pagina") @DefaultValue("1") int pagina,
                                                                 @QueryParam("tamanhoPagina") @DefaultValue("10") int tamanhoPagina) {
        return fornecedorService.pesquisarFornecedores(termo, pagina, tamanhoPagina);
    }
}