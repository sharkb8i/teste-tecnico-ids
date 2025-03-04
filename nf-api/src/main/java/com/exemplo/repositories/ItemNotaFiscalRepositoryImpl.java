package com.exemplo.repositories;

import java.util.UUID;

import com.exemplo.entities.ItemNotaFiscal;
import com.exemplo.interfaces.ItemNotaFiscalRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@ApplicationScoped
public class ItemNotaFiscalRepositoryImpl extends BaseRepositoryImpl<ItemNotaFiscal> implements ItemNotaFiscalRepository {

    public ItemNotaFiscalRepositoryImpl() {
        super(null, ItemNotaFiscal.class);
    }

    @Inject
    public ItemNotaFiscalRepositoryImpl(EntityManager entityManager) {
        super(entityManager, ItemNotaFiscal.class);
    }

    @Override
    public boolean validarProdutoVinculado(UUID idProduto) {
        String queryString = "SELECT COUNT(i) FROM ItemNotaFiscal i WHERE i.produto.id = :idProduto";
        
        TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
        query.setParameter("idProduto", idProduto);
        
        long count = query.getSingleResult();
        
        return count > 0;
    }
}