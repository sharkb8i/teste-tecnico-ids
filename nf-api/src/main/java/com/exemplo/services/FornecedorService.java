package com.exemplo.services;

import java.time.LocalDateTime;
import java.util.UUID;

import com.exemplo.dtos.CriarFornecedorDTO;
import com.exemplo.dtos.EditarFornecedorDTO;
import com.exemplo.dtos.RespostaPaginadaDTO;
import com.exemplo.entities.Fornecedor;
import com.exemplo.entities.EnumSituacaoFornecedor;
import com.exemplo.exceptions.RegistroDuplicadoException;
import com.exemplo.exceptions.RegistroNaoEncontradoException;
import com.exemplo.exceptions.RegistroNaoExcludenteException;
import com.exemplo.interfaces.FornecedorRepository;
import com.exemplo.interfaces.NotaFiscalRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FornecedorService {
    
    @Inject
    FornecedorRepository fornecedorRepository;

    @Inject
    NotaFiscalRepository notaFiscalRepository;

    public Fornecedor criarFornecedor(CriarFornecedorDTO dto) {        
        boolean fornecedorExiste = fornecedorRepository
            .encontrarPorTermo("cnpj", dto.getCnpj().getNumero())
            .isPresent();

        if (fornecedorExiste)
            throw new RegistroDuplicadoException("Já existe um fornecedor cadastrado com este CNPJ: " + dto.getCnpj().getNumero() + ".");

        fornecedorExiste = fornecedorRepository
            .encontrarPorTermo("codigo", dto.getCodigo())
            .isPresent();
            
        if (fornecedorExiste)
            throw new RegistroDuplicadoException("Já existe um fornecedor cadastrado com este código: " + dto.getCodigo() + ".");
        
        Fornecedor fornecedor = new Fornecedor(
            null,
            dto.getCodigo(),
            dto.getRazaoSocial(),
            dto.getEmail().getEndereco(),
            dto.getTelefone().getNumero(),
            dto.getCnpj().getNumero(),
            dto.getSituacao(),
            null
        );

        fornecedorRepository.salvar(fornecedor);
        return fornecedor;
    }

    public Fornecedor editarFornecedor(UUID id, EditarFornecedorDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.encontrarPorId(id)
            .orElseThrow(() -> new RegistroNaoEncontradoException("Fornecedor não encontrado para o id: " + id + "."));

        if (dto.getCodigo() != null)
            fornecedor.setCodigo(dto.getCodigo());
        
        if (dto.getRazaoSocial() != null)
            fornecedor.setRazaoSocial(dto.getRazaoSocial());
        
        if (dto.getEmail() != null)
            fornecedor.setEmail(dto.getEmail());
        
        if (dto.getTelefone() != null)
            fornecedor.setTelefone(dto.getTelefone());

        if (dto.getSituacao() != null)
            fornecedor.setSituacao(dto.getSituacao());

        fornecedorRepository.salvar(fornecedor);
        return fornecedor;
    }

    public void excluirFornecedor(UUID id) {
        Fornecedor fornecedor = fornecedorRepository.encontrarPorId(id)
            .orElseThrow(() -> new RegistroNaoEncontradoException("Fornecedor não encontrado para o id: " + id + "."));

        boolean fornecedorVinculadoNotaFiscal = notaFiscalRepository.validarFornecedorVinculado(id);

        if (fornecedorVinculadoNotaFiscal)
            throw new RegistroNaoExcludenteException("Fornecedor com id " + id + " está vinculado a uma nota fiscal e não pode ser excluído.");

        fornecedor.setSituacao(EnumSituacaoFornecedor.BAIXADO);
        fornecedor.setDataBaixa(LocalDateTime.now());

        fornecedorRepository.salvar(fornecedor);
    }

    public RespostaPaginadaDTO<Fornecedor> pesquisarFornecedores(String termo, int pagina, int tamanhoPagina) {
        return fornecedorRepository.pesquisarFornecedores(termo, pagina, tamanhoPagina);
    }

    public Fornecedor encontrarPorId(UUID id) {
        Fornecedor fornecedor = fornecedorRepository.encontrarPorId(id)
            .orElseThrow(() -> new RegistroNaoEncontradoException("Fornecedor não encontrado para o id: " + id + "."));

        return fornecedor;
    }
}