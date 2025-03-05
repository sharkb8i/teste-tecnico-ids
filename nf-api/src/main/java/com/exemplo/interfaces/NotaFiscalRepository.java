package com.exemplo.interfaces;

import java.util.UUID;

import com.exemplo.dtos.RespostaPaginadaDTO;
import com.exemplo.entities.NotaFiscal;

public interface NotaFiscalRepository extends BaseRepository<NotaFiscal> {
  RespostaPaginadaDTO<NotaFiscal> pesquisarNotasFiscais(String termo, int pagina, int tamanhoPagina);
  boolean validarFornecedorVinculado(UUID idFornecedor);
}