package com.exemplo.interfaces;

import com.exemplo.dtos.RespostaPaginadaDTO;
import com.exemplo.entities.Produto;

public interface ProdutoRepository extends BaseRepository<Produto> {
    RespostaPaginadaDTO<Produto> pesquisarProdutos(String termo, int pagina, int tamanhoPagina);
}