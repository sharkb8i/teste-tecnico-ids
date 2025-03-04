package com.exemplo.interfaces;

import java.util.UUID;

import com.exemplo.entities.ItemNotaFiscal;

public interface ItemNotaFiscalRepository extends BaseRepository<ItemNotaFiscal> {
    boolean validarProdutoVinculado(UUID idProduto);
}