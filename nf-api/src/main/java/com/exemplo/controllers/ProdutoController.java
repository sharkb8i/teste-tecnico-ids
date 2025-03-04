package com.exemplo.controllers;

import com.exemplo.dtos.CriarProdutoDTO;
import com.exemplo.dtos.EditarProdutoDTO;
import com.exemplo.dtos.RespostaPaginadaDTO;
import com.exemplo.entities.Produto;
import com.exemplo.services.ProdutoService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoController {

    @Inject
    private ProdutoService produtoService;

    @POST
    @Transactional
    public Response criarProduto(CriarProdutoDTO dto) {
        Produto produto = produtoService.criarProduto(dto);
        return Response.ok(produto).status(201).build();
    }

    @GET
    @Path("/{id}")
    public Response pesquisarProdutoPorId(@PathParam("id") UUID id) {
        Produto produto = produtoService.encontrarPorId(id);
        return Response.ok(produto).status(200).build();
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    public Response editarProduto(@PathParam("id") UUID id, EditarProdutoDTO dto) {
        Produto produto = produtoService.editarProduto(id, dto);
        return Response.ok(produto).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response excluirProduto(@PathParam("id") UUID id) {
        produtoService.excluirProduto(id);
        return Response.noContent().build();
    }

    @GET
    public RespostaPaginadaDTO<Produto> pesquisarProdutos(@QueryParam("termo") String termo,
                                                          @QueryParam("pagina") @DefaultValue("1") int pagina,
                                                          @QueryParam("tamanhoPagina") @DefaultValue("10") int tamanhoPagina) {
        return produtoService.pesquisarProdutos(termo, pagina, tamanhoPagina);
    }
}