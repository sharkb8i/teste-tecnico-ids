package com.exemplo.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaseRepository<T> {
  Optional<T> encontrarPorId(UUID id);
  Optional<T> encontrarPorTermo(String termo, Object valor);
  void salvar(T entity);
  void deletar(T entity);    
  List<T> pesquisar();
  List<T> pesquisarComPaginacao(String termo, int pagina, int tamanhoPagina);
  long contar();
  boolean uuidValido(String str);
}