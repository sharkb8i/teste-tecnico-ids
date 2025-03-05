package com.exemplo.repositories;

import java.util.List;
import java.util.UUID;

import com.exemplo.dtos.RespostaPaginadaDTO;
import com.exemplo.entities.EnumSituacaoFornecedor;
import com.exemplo.entities.Fornecedor;
import com.exemplo.interfaces.FornecedorRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@ApplicationScoped
public class FornecedorRepositoryImpl extends BaseRepositoryImpl<Fornecedor> implements FornecedorRepository {
    
  public FornecedorRepositoryImpl() {
    super(null, Fornecedor.class);
  }

  @Inject
  public FornecedorRepositoryImpl(EntityManager entityManager) {
    super(entityManager, Fornecedor.class);
  }

  @Override
  public RespostaPaginadaDTO<Fornecedor> pesquisarFornecedores(String termo, int pagina, int tamanhoPagina) {
    TypedQuery<Fornecedor> query;

    String selectString = "SELECT f FROM Fornecedor f ";
    String countString = "SELECT COUNT(f) FROM Fornecedor f ";
    String whereString = "WHERE f.codigo LIKE :codigo OR f.razaoSocial LIKE :razaoSocial OR f.email LIKE :email OR f.telefone LIKE :telefone OR f.cnpj LIKE :cnpj";
    
    boolean termoUUID = termo != null ? uuidValido(termo) : false;
    boolean termoEnum = termo != null ? enumSituacaoValido(termo) : false;

    if (termoUUID) {
      whereString = "WHERE f.id = :id";
      query = entityManager.createQuery(selectString.concat(whereString), Fornecedor.class);
      query.setParameter("id", UUID.fromString(termo)); 
    } else {
      StringBuilder queryString = new StringBuilder(termo != null
        ? selectString.concat(whereString)
        : selectString);

      if (termoEnum)
        queryString.append(" OR f.situacao = :situacao");

      query = entityManager.createQuery(queryString.toString(), Fornecedor.class);
      if (termo != null) {
        query.setParameter("codigo", "%" + termo + "%");
        query.setParameter("razaoSocial", "%" + termo + "%");
        query.setParameter("email", "%" + termo + "%");
        query.setParameter("telefone", termo);
        query.setParameter("cnpj", termo);
      }

      if (termoEnum)
        query.setParameter("situacao", EnumSituacaoFornecedor.valueOf(termo.toUpperCase()));
    }

    StringBuilder countQueryString = new StringBuilder(termo != null
      ? countString.concat(whereString)
      : countString);

    if (termoEnum)
      countQueryString.append(" OR f.situacao = :situacao");

    TypedQuery<Long> countQuery = entityManager.createQuery(countQueryString.toString(), Long.class);
    if (termo != null) {
      countQuery.setParameter("codigo", "%" + termo + "%");
      countQuery.setParameter("razaoSocial", "%" + termo + "%");
      countQuery.setParameter("email", "%" + termo + "%");
      countQuery.setParameter("telefone", "%" + termo + "%");
      countQuery.setParameter("cnpj", "%" + termo + "%");
    }
      
    if (termoEnum)
      countQuery.setParameter("situacao", EnumSituacaoFornecedor.valueOf(termo.toUpperCase()));

    long totalResultados = countQuery.getSingleResult();

    List<Fornecedor> fornecedores = query.getResultList();
      
    query.setFirstResult((pagina - 1) * tamanhoPagina);
    query.setMaxResults(tamanhoPagina);
      
    return new RespostaPaginadaDTO<Fornecedor>(fornecedores, pagina, totalResultados, tamanhoPagina);
  }

  private boolean enumSituacaoValido(String termo) {
    try {
      EnumSituacaoFornecedor.valueOf(termo.toUpperCase());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}