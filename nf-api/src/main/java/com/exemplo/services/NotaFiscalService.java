package com.exemplo.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.exemplo.dtos.EditarNotaFiscalDTO;
import com.exemplo.dtos.EntradaNotaFiscalDTO;
import com.exemplo.dtos.ItemNotaFiscalDTO;
import com.exemplo.dtos.RespostaPaginadaDTO;
import com.exemplo.entities.Endereco;
import com.exemplo.entities.EnumSituacaoProduto;
import com.exemplo.entities.Fornecedor;
import com.exemplo.entities.ItemNotaFiscal;
import com.exemplo.entities.NotaFiscal;
import com.exemplo.entities.Produto;
import com.exemplo.exceptions.RegistroInativadoException;
import com.exemplo.exceptions.RegistroNaoEncontradoException;
import com.exemplo.exceptions.ValorTotalInvalidoException;
import com.exemplo.interfaces.FornecedorRepository;
import com.exemplo.interfaces.ItemNotaFiscalRepository;
import com.exemplo.interfaces.NotaFiscalRepository;
import com.exemplo.interfaces.ProdutoRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class NotaFiscalService {
       
    @Inject
    NotaFiscalRepository notaFiscalRepository;

    @Inject
    FornecedorRepository fornecedorRepository;

    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    ItemNotaFiscalRepository itemNotaFiscalRepository;

    public NotaFiscal entradaNotaFiscal(EntradaNotaFiscalDTO dto) {
        Optional<Fornecedor> fornecedor = fornecedorRepository
            .encontrarPorTermo("codigo", dto.getCodFornecedor());
        
        if (!fornecedor.isPresent())
            throw new RegistroNaoEncontradoException("Fornecedor não encontrado para o código: " + dto.getCodFornecedor() + ".");

        List<ItemNotaFiscal> itensNotaFiscal = dto.getItens().stream()
            .map(itemDTO -> {
                Produto prod = produtoRepository.encontrarPorTermo("codigo", itemDTO.getCodProduto())
                    .orElseThrow(() -> new RegistroNaoEncontradoException("Produto não encontrado para o código: " + itemDTO.getCodProduto() + "."));
                    
                if (prod.getSituacao() == EnumSituacaoProduto.INATIVO)
                    throw new RegistroInativadoException("Produto de código " + itemDTO.getCodProduto() + " inativado.");

                return new ItemNotaFiscal(
                    null,
                    prod,
                    itemDTO.getValorUnitario(),
                    itemDTO.getQuantidade(),
                    null
                );
            })
            .collect(Collectors.toList());

        BigDecimal valorCalculado = itensNotaFiscal.stream()
            .map(item -> item.getValorUnitario().multiply(item.getQuantidade()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (valorCalculado.compareTo(dto.getValorTotal()) != 0)
            throw new ValorTotalInvalidoException("O valor total calculado sobre os itens da nota (" + valorCalculado + ") não corresponde ao valor total informado (" + dto.getValorTotal() + ").");

        NotaFiscal nf = new NotaFiscal(
            null,
            new Endereco(
                null,
                dto.getEndereço().getLogradouro(),
                dto.getEndereço().getNumero(),
                dto.getEndereço().getBairro(),
                dto.getEndereço().getCidade(),
                dto.getEndereço().getEstado()
            ),
            dto.getValorTotal(),
            fornecedor.get(),
            itensNotaFiscal);
        
        itensNotaFiscal.forEach(item -> item.setNf(nf));
        
        notaFiscalRepository.salvar(nf);
        return nf;
    }

    public void emitirNotaFiscalPorId(UUID id) {
        NotaFiscal nf = notaFiscalRepository.encontrarPorId(id)
            .orElseThrow(() -> new RegistroNaoEncontradoException("Nota fiscal não encontrada para o id: " + id + "."));
        
        nf.setDataEmissao(LocalDateTime.now());
        
        notaFiscalRepository.salvar(nf);
    }

    public NotaFiscal editarNotaFiscal(UUID id, EditarNotaFiscalDTO dto) {
        NotaFiscal nf = notaFiscalRepository.encontrarPorId(id)
            .orElseThrow(() -> new RegistroNaoEncontradoException("Nota fiscal não encontrada para o id: " + id + "."));
        
        if (dto.getCodFornecedor() != null) {
            Fornecedor fornecedor = fornecedorRepository.encontrarPorTermo("codigo", dto.getCodFornecedor())
                .orElseThrow(() -> new RegistroNaoEncontradoException("Fornecedor não encontrado para o código: " + dto.getCodFornecedor() + "."));
            
            nf.setFornecedor(fornecedor);
        }

        if (dto.getValorTotal() != null && dto.getItens().size() == 0) {
            BigDecimal valorCalculado = nf.getItens().stream()
                .map(item -> item.getValorUnitario().multiply(item.getQuantidade()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            if (valorCalculado.compareTo(dto.getValorTotal()) != 0)
                throw new ValorTotalInvalidoException("O valor total calculado sobre os itens da nota (" + valorCalculado + ") não corresponde ao valor total informado (" + dto.getValorTotal() + ").");
        }

        if (dto.getEndereco() != null) {
            nf.setEndereco(new Endereco(
                null,
                dto.getEndereco().getLogradouro(),
                dto.getEndereco().getNumero(),
                dto.getEndereco().getBairro(),
                dto.getEndereco().getCidade(),
                dto.getEndereco().getEstado()
            ));
        }

        if (dto.getItens().size() > 0) {
            Map<String, ItemNotaFiscalDTO> itensDtoMap = dto.getItens().stream()
                .collect(Collectors.toMap(ItemNotaFiscalDTO::getId, itemDTO -> itemDTO));
            
            Iterator<ItemNotaFiscal> iterator = nf.getItens().iterator();
            while (iterator.hasNext()) {
                ItemNotaFiscal itemBanco = iterator.next();
                ItemNotaFiscalDTO itemDTO = itensDtoMap.get(itemBanco.getId().toString());
    
                if (itemDTO != null) {
                    itemBanco.setQuantidade(itemDTO.getQuantidade());
                    itemBanco.setValorUnitario(itemDTO.getValorUnitario());
                    itensDtoMap.remove(itemBanco.getId().toString());
                } else {
                    iterator.remove();
                    itemNotaFiscalRepository.deletar(itemBanco);
                }
            }

            for (ItemNotaFiscalDTO itemDTO : itensDtoMap.values()) {
                Produto produto = produtoRepository.encontrarPorTermo("codigo", itemDTO.getCodProduto())
                    .orElseThrow(() -> new RegistroNaoEncontradoException("Produto não encontrado para o código: " + itemDTO.getCodProduto() + "."));
                
                ItemNotaFiscal novoItem = new ItemNotaFiscal(
                    (itemDTO.getId() == null) ? UUID.randomUUID() : UUID.fromString(itemDTO.getId()),
                    produto,
                    itemDTO.getValorUnitario(),
                    itemDTO.getQuantidade(),
                    null
                );
                nf.addItem(novoItem);
            }
            
            BigDecimal valorTotalAtualizado = dto.getItens().stream()
                .map(item -> item.getValorUnitario().multiply(item.getQuantidade()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            if (valorTotalAtualizado.compareTo(dto.getValorTotal()) != 0)
                throw new ValorTotalInvalidoException("O valor total calculado sobre os itens da nota (" + valorTotalAtualizado + ") não corresponde ao valor total informado (" + dto.getValorTotal() + ").");

            nf.setValorTotal(valorTotalAtualizado);
        }

        notaFiscalRepository.salvar(nf);
        return nf;
    }

    public void adicionarItemNotaFiscal(UUID id, ItemNotaFiscalDTO dto) {
        NotaFiscal nf = notaFiscalRepository.encontrarPorId(id)
            .orElseThrow(() -> new RegistroNaoEncontradoException("Nota fiscal não encontrada para o id: " + id + "."));
        
        Produto produto = produtoRepository.encontrarPorTermo("codigo", dto.getCodProduto())
            .orElseThrow(() -> new RegistroNaoEncontradoException("Produto não encontrado para o código: " + dto.getCodProduto() + "."));
        
        Optional<ItemNotaFiscal> itemExistente = nf.getItens().stream()
            .filter(item -> item.getProduto().getCodigo().equals(dto.getCodProduto()))
            .findFirst();

        if (itemExistente.isPresent()) {
            ItemNotaFiscal item = itemExistente.get();
            item.setQuantidade(item.getQuantidade().add(dto.getQuantidade()));
        } else {
            ItemNotaFiscal item = new ItemNotaFiscal(
                null,
                produto,
                dto.getValorUnitario(),
                dto.getQuantidade(),
                nf
            );

            nf.getItens().add(item);
            itemNotaFiscalRepository.salvar(item);
        }

        recalcularValorTotal(nf);
        notaFiscalRepository.salvar(nf);
    }

    public void removerItemNotaFiscal(UUID id, UUID idItem) {
        NotaFiscal nf = notaFiscalRepository.encontrarPorId(id)
            .orElseThrow(() -> new RegistroNaoEncontradoException("Nota fiscal não encontrada para o id: " + id + "."));

        ItemNotaFiscal item = nf.getItens().stream()
            .filter(i -> i.getId().equals(idItem))
            .findFirst()
            .orElseThrow(() -> new RegistroNaoEncontradoException("Item da nota fiscal não encontrado para o id: " + idItem + "."));
        
        nf.getItens().remove(item);
        itemNotaFiscalRepository.deletar(item);
        recalcularValorTotal(nf);
        notaFiscalRepository.salvar(nf);
    }

    public void excluirNotaFiscal(UUID id) {
        NotaFiscal nf = notaFiscalRepository.encontrarPorId(id)
            .orElseThrow(() -> new RegistroNaoEncontradoException("Nota fiscal não encontrada para o id: " + id + "."));
        
        nf.setDataExclusao(LocalDateTime.now());

        notaFiscalRepository.salvar(nf);
    }

    public RespostaPaginadaDTO<NotaFiscal> pesquisarNotasFiscais(String termo, int pagina, int tamanhoPagina) {
        return notaFiscalRepository.pesquisarNotasFiscais(termo, pagina, tamanhoPagina);
    }

    public NotaFiscal encontrarPorId(UUID id) {
        NotaFiscal nf = notaFiscalRepository.encontrarPorId(id)
            .orElseThrow(() -> new RegistroNaoEncontradoException("Nota fiscal não encontrada para o id: " + id + "."));

        return nf;
    }

    private void recalcularValorTotal(NotaFiscal nf) {
        BigDecimal valorTotal = nf.getItens().stream()
            .map(item -> item.getValorUnitario().multiply(item.getQuantidade()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    
        nf.setValorTotal(valorTotal);
    }
}