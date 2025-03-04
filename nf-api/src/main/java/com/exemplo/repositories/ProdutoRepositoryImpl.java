package com.exemplo.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.UUID;

import com.exemplo.dtos.RespostaPaginadaDTO;
import com.exemplo.entities.EnumSituacaoProduto;
import com.exemplo.entities.Produto;
import com.exemplo.interfaces.ProdutoRepository;

@ApplicationScoped
public class ProdutoRepositoryImpl extends BaseRepositoryImpl<Produto> implements ProdutoRepository {
    
    public ProdutoRepositoryImpl() {
        super(null, Produto.class);
    }

    @Inject
    public ProdutoRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Produto.class);
    }

    @Override
    public RespostaPaginadaDTO<Produto> pesquisarProdutos(String termo, int pagina, int tamanhoPagina) {
        TypedQuery<Produto> query;

        String selectString = "SELECT p FROM Produto p ";
        String countString = "SELECT COUNT(p) FROM Produto p ";
        String whereString = "WHERE p.codigo LIKE :codigo OR p.descricao LIKE :descricao";

        boolean termoUUID = termo != null ? uuidValido(termo) : false;
        boolean termoEnum = termo != null ? enumSituacaoValido(termo) : false;

        if (termoUUID) {
            whereString = "WHERE f.id = :id";
            query = entityManager.createQuery(selectString.concat(whereString), Produto.class);
            query.setParameter("id", UUID.fromString(termo)); 
        } else {
            StringBuilder queryString = new StringBuilder(termo != null
                ? selectString.concat(whereString)
                : selectString);
            
            if (termoEnum)
                queryString.append(" OR p.situacao = :situacao");

            query = entityManager.createQuery(queryString.toString(), Produto.class);
            if (termo != null) {
                query.setParameter("codigo", "%" + termo + "%");
                query.setParameter("descricao", "%" + termo + "%");
            }

            if (termoEnum)
                query.setParameter("situacao", EnumSituacaoProduto.valueOf(termo.toUpperCase()));
        }

        StringBuilder countQueryString = new StringBuilder(termo != null
            ? countString.concat(whereString)
            : countString);

        if (termoEnum)
            countQueryString.append(" OR p.situacao = :situacao");

        TypedQuery<Long> countQuery = entityManager.createQuery(countQueryString.toString(), Long.class);
        if (termo != null) {
            countQuery.setParameter("codigo", "%" + termo + "%");
            countQuery.setParameter("descricao", "%" + termo + "%");
        }

        if (termoEnum)
            countQuery.setParameter("situacao", EnumSituacaoProduto.valueOf(termo.toUpperCase()));

        long totalResultados = countQuery.getSingleResult();

        List<Produto> produtos = query.getResultList();
        
        query.setFirstResult((pagina - 1) * tamanhoPagina);
        query.setMaxResults(tamanhoPagina);
        
        return new RespostaPaginadaDTO<Produto>(produtos, pagina, totalResultados, tamanhoPagina);
    }

    private boolean enumSituacaoValido(String termo) {
        try {
            EnumSituacaoProduto.valueOf(termo.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}