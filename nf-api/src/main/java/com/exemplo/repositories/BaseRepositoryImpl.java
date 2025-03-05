package com.exemplo.repositories;

import com.exemplo.interfaces.BaseRepository;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class BaseRepositoryImpl<T> implements BaseRepository<T> {
    
  @Inject
  protected EntityManager entityManager;

  private final Class<T> entityClass;

  public BaseRepositoryImpl(EntityManager entityManager, Class<T> entityClass) {
    this.entityManager = entityManager;
    this.entityClass = entityClass;
  }

  @Override
  public void salvar(T entity) {
    if (entity == null)
      return;

    if (entityManager.contains(entity))
      entityManager.merge(entity);
    else
      entityManager.persist(entity);
  }

  @Override
  public Optional<T> encontrarPorId(UUID id) {
    return Optional.ofNullable(entityManager.find(entityClass, id));
  }

  @Override
  public Optional<T> encontrarPorTermo(String termo, Object valor) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<T> query = cb.createQuery(entityClass);
    Root<T> root = query.from(entityClass);

    if (valor instanceof String)
      query.where(cb.equal(root.get(termo), valor));
    else if (valor instanceof Number)
      query.where(cb.equal(root.get(termo), valor));
    else if (valor instanceof LocalDateTime)
      query.where(cb.equal(root.get(termo), valor));
    else
      throw new IllegalArgumentException("Tipo de valor não suportado.");

    TypedQuery<T> typedQuery = entityManager.createQuery(query);
    return typedQuery.getResultStream().findFirst();
  }

  @Override
  public void deletar(T entity) {
    if (entity == null)
      return;
    
    entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
  }

  @Override
  public List<T> pesquisar() {
    TypedQuery<T> query = entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass);
    return query.getResultList();
  }

  @Override
  public long contar() {
    TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e", Long.class);
    return query.getSingleResult();
  }

  @Override
  public List<T> pesquisarComPaginacao(String termo, int pagina, int tamanhoPagina) {
    String queryString = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.nome LIKE :termo";
    TypedQuery<T> query = entityManager.createQuery(queryString, entityClass);
    query.setParameter("termo", "%" + termo + "%");
    query.setFirstResult((pagina - 1) * tamanhoPagina);
    query.setMaxResults(tamanhoPagina);
    return query.getResultList();
  }

  @Override
  public boolean uuidValido(String str) {
    try {
      UUID.fromString(str);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}