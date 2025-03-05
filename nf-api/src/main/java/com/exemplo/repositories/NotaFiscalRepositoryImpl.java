package com.exemplo.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.exemplo.dtos.RespostaPaginadaDTO;
import com.exemplo.entities.NotaFiscal;
import com.exemplo.interfaces.NotaFiscalRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@ApplicationScoped
public class NotaFiscalRepositoryImpl extends BaseRepositoryImpl<NotaFiscal> implements NotaFiscalRepository  {

  public NotaFiscalRepositoryImpl() {
    super(null, NotaFiscal.class);
  }

  @Inject
  public NotaFiscalRepositoryImpl(EntityManager entityManager) {
    super(entityManager, NotaFiscal.class);
  }

  @Override
  public RespostaPaginadaDTO<NotaFiscal> pesquisarNotasFiscais(String termo, int pagina, int tamanhoPagina) {
    TypedQuery<NotaFiscal> query;

    String selectString = "SELECT nf FROM NotaFiscal nf LEFT JOIN FETCH nf.itens ";
    String countString = "SELECT COUNT(nf) FROM NotaFiscal nf ";
    String whereString = " WHERE nf.valorTotal = :valorTotal OR nf.numeroNota = :numeroNota";

    boolean termoUUID = termo != null ? uuidValido(termo) : false;
    boolean termoNumerico = termo != null && termo.matches("-?\\d+(\\.\\d+)?");

    if (termoUUID) {
      query = entityManager.createQuery(selectString.concat("WHERE f.id = :id"), NotaFiscal.class);
      query.setParameter("id", UUID.fromString(termo)); 
    } else {
      StringBuilder queryString = new StringBuilder(selectString);

      if (termo != null && termoNumerico) {
        queryString.append(whereString);
      } else {
        // append other params
      }

      query = entityManager.createQuery(queryString.toString(), NotaFiscal.class);
      if (termo != null && termoNumerico) {             
        query.setParameter("numeroNota", Long.parseLong(termo));
        query.setParameter("valorTotal",  new BigDecimal(termo));
      } else {
        // query.setParameter("endereco", "%" + termo + "%");
        // itens.produto.codigo
        // itens.fornecedor.codigo
      }
    }

    StringBuilder countQueryString = new StringBuilder(countString);

    if (termo != null && termoNumerico) {
      countQueryString.append(whereString);
    } else {
      // append other params
    }

    TypedQuery<Long> countQuery = entityManager.createQuery(countQueryString.toString(), Long.class);
    if (termo != null && termoNumerico) {
      countQuery.setParameter("numeroNota", Long.parseLong(termo));
      countQuery.setParameter("valorTotal",  new BigDecimal(termo));
    } else {
      // query.setParameter("endereco", "%" + termo + "%");
      // itens.produto.codigo
      // itens.fornecedor.codigo
    }

    long totalResultados = countQuery.getSingleResult();

    List<NotaFiscal> nfs = query.getResultList();
      
    query.setFirstResult((pagina - 1) * tamanhoPagina);
    query.setMaxResults(tamanhoPagina);
    
    return new RespostaPaginadaDTO<NotaFiscal>(nfs, pagina, totalResultados, tamanhoPagina);
  }

  @Override
  public boolean validarFornecedorVinculado(UUID idFornecedor) {
    String queryString = "SELECT COUNT(nf) FROM NotaFiscal nf WHERE nf.fornecedor.id = :idFornecedor";
    
    TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
    query.setParameter("idFornecedor", idFornecedor);
    
    long count = query.getSingleResult();
    
    return count > 0;
  }
}