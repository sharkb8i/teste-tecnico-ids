package com.exemplo.services;

import java.time.LocalDateTime;
import java.util.UUID;

import com.exemplo.dtos.CriarProdutoDTO;
import com.exemplo.dtos.EditarProdutoDTO;
import com.exemplo.dtos.RespostaPaginadaDTO;
import com.exemplo.entities.EnumSituacaoProduto;
import com.exemplo.entities.Produto;
import com.exemplo.exceptions.RegistroDuplicadoException;
import com.exemplo.exceptions.RegistroNaoEncontradoException;
import com.exemplo.exceptions.RegistroNaoExcludenteException;
import com.exemplo.interfaces.ItemNotaFiscalRepository;
import com.exemplo.interfaces.ProdutoRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProdutoService {

    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    ItemNotaFiscalRepository itemNotaFiscalRepository;

    public Produto criarProduto(CriarProdutoDTO dto) {
        boolean produtoExiste = produtoRepository.encontrarPorTermo("codigo", dto.getCodigo()).isPresent();
        if (produtoExiste)
            throw new RegistroDuplicadoException("Já existe um produto cadastrado com este código: " + dto.getCodigo() + ".");

        Produto produto = new Produto(null,
            dto.getCodigo(), 
            dto.getDescricao(),
            dto.getSituacao());
        
        produtoRepository.salvar(produto);
        return produto;
    }

    public Produto editarProduto(UUID id, EditarProdutoDTO dto) {
        Produto produto = produtoRepository.encontrarPorId(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Produto não encontrado para o id: " + id + "."));

        produto.setDescricao(dto.getDescricao());

        if (dto.getCodigo() != null)
            produto.setCodigo(dto.getCodigo());

        produtoRepository.salvar(produto);
        return produto;
    }

    public void excluirProduto(UUID id) {
        Produto produto = produtoRepository.encontrarPorId(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Produto não encontrado para o id: " + id + "."));

        boolean produtoVinculadoNotaFiscal = itemNotaFiscalRepository.validarProdutoVinculado(id);

        if (produtoVinculadoNotaFiscal)
            throw new RegistroNaoExcludenteException("Produto com id " + id + " está vinculado a uma nota fiscal e não pode ser excluído.");

        produto.setSituacao(EnumSituacaoProduto.INATIVO);
        produto.setDataExclusao(LocalDateTime.now());

        produtoRepository.salvar(produto);
    }

    public RespostaPaginadaDTO<Produto> pesquisarProdutos(String termo, int pagina, int tamanhoPagina) {
        return produtoRepository.pesquisarProdutos(termo, pagina, tamanhoPagina);
    }

    public Produto encontrarPorId(UUID id) {
        Produto produto = produtoRepository.encontrarPorId(id)
            .orElseThrow(() -> new RegistroNaoEncontradoException("Produto não encontrado para o id: " + id + "."));

        return produto;
    }
}