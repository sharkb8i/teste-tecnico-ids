package com.exemplo.interfaces;

import com.exemplo.dtos.RespostaPaginadaDTO;
import com.exemplo.entities.Fornecedor;

public interface FornecedorRepository extends BaseRepository<Fornecedor> {
    RespostaPaginadaDTO<Fornecedor> pesquisarFornecedores(String termo, int pagina, int tamanhoPagina);   
}