package com.exemplo.controllers;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.exemplo.dtos.EditarNotaFiscalDTO;
import com.exemplo.dtos.EnderecoDTO;
import com.exemplo.dtos.EntradaNotaFiscalDTO;
import com.exemplo.dtos.ItemNotaFiscalDTO;
import com.exemplo.dtos.RespostaPaginadaDTO;
import com.exemplo.entities.Endereco;
import com.exemplo.entities.EnumSituacaoProduto;
import com.exemplo.entities.Fornecedor;
import com.exemplo.entities.ItemNotaFiscal;
import com.exemplo.entities.NotaFiscal;
import com.exemplo.entities.Produto;
import com.exemplo.exceptions.RegistroNaoEncontradoException;
import com.exemplo.services.NotaFiscalService;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;

import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NotaFiscalControllerTest {

  @InjectMock
  NotaFiscalService notaFiscalService;

  private UUID notaFiscalId;
  private UUID fornecedorId;
  private NotaFiscal notaFiscal;
  private Endereco endereco;
  private Fornecedor fornecedor;
  private List<ItemNotaFiscal> itens;

  @BeforeEach
  void setup() {
    endereco = new Endereco(
      UUID.randomUUID(),
      "Rua Elemento X",
      1111L,
      "Bairro X",
      "Cidade X",
      "X"
    );

    fornecedorId = UUID.randomUUID();
    fornecedor = new Fornecedor();
    fornecedor.setId(fornecedorId);
    fornecedor.setCodigo("F001");
    fornecedor.setRazaoSocial("FORNECEDOR TESTE");
    fornecedor.setEmail("fornecedor@teste.com");
    fornecedor.setTelefone("4530356433");
    fornecedor.setCnpj("12.345.678/0001-95");

    ItemNotaFiscal item1 = new ItemNotaFiscal();
    item1.setId(UUID.randomUUID());
    item1.setValorUnitario(BigDecimal.valueOf(100));
    item1.setQuantidade(BigDecimal.valueOf(10));
    item1.setNf(notaFiscal);
    
    Produto produto1 = new Produto();
    produto1.setId(UUID.randomUUID());
    produto1.setDescricao("Produto 1");
    produto1.setSituacao(EnumSituacaoProduto.ATIVO);
    item1.setProduto(produto1); 

    ItemNotaFiscal item2 = new ItemNotaFiscal();
    item2.setId(UUID.randomUUID());
    item2.setValorUnitario(BigDecimal.valueOf(150));
    item2.setQuantidade(BigDecimal.valueOf(5));
    item2.setNf(notaFiscal);
    
    Produto produto2 = new Produto();
    produto2.setId(UUID.randomUUID());
    produto2.setDescricao("Produto 2");
    produto2.setSituacao(EnumSituacaoProduto.ATIVO);
    item2.setProduto(produto2);

    itens = List.of(item1, item2);

    notaFiscalId = UUID.randomUUID();
    notaFiscal = new NotaFiscal();
    notaFiscal.setId(notaFiscalId);
    notaFiscal.setEndereco(endereco);
    notaFiscal.setFornecedor(fornecedor);
    notaFiscal.setValorTotal(BigDecimal.valueOf(1750));
    notaFiscal.setItens(itens);
  }

  @Test
  @Order(1)
  void testEntradaNotaFiscalSucesso() {
    when(notaFiscalService.entradaNotaFiscal(any(EntradaNotaFiscalDTO.class))).thenReturn(notaFiscal);

    given()
      .contentType(MediaType.APPLICATION_JSON)
      .body(notaFiscal)
    .when()
      .post("/notas-fiscais")
    .then()
      .statusCode(201);
  }

  @Test
  @Order(2)
  void testPesquisarNotaFiscalPorId() {
    when(notaFiscalService.encontrarPorId(notaFiscalId)).thenReturn(notaFiscal);

    given()
      .when().get("/notas-fiscais/" + notaFiscalId)
      .then().statusCode(200);
  }

  @Test
  @Order(3)
  void testEditarNotaFiscalSucesso() {
    EnderecoDTO endDto = new EnderecoDTO(
      "Rua Elemento Y",
      2222L,
      "Bairro Y",
      "Cidade Y",
      "Y"
    );

    List<ItemNotaFiscalDTO> itensDto = itens.stream()
      .map(item -> {
        item.setQuantidade(item.getQuantidade().multiply(BigDecimal.valueOf(2)));  // Duplicar quantidade
        return new ItemNotaFiscalDTO(item.getProduto().getCodigo(), item.getValorUnitario(), item.getQuantidade());
      })
      .collect(Collectors.toList());

    EditarNotaFiscalDTO dto = new EditarNotaFiscalDTO(
      "F002",
      BigDecimal.valueOf(3500),
      endDto,
      itensDto
    );

    when(notaFiscalService.editarNotaFiscal(eq(notaFiscalId), any(EditarNotaFiscalDTO.class))).thenReturn(notaFiscal);

    given()
      .contentType(MediaType.APPLICATION_JSON)
      .body(dto)
      .when()
        .patch("/notas-fiscais/" + notaFiscalId)
      .then()
        .statusCode(200);
  }

  @Test
  @Order(4)
  void testExcluirNotaFiscal() {
    doNothing().when(notaFiscalService).excluirNotaFiscal(notaFiscalId);
    
    given()
      .when().delete("/notas-fiscais/" + notaFiscalId)
      .then().statusCode(204);
  }

  @Test
  @Order(5)
  void testExcluirNotaFiscalInexistente() {
    UUID uuid = UUID.randomUUID();

    doThrow(new RegistroNaoEncontradoException("Nota fiscal não encontrada para o id: " + uuid.toString() + "."))
      .when(notaFiscalService)
      .excluirNotaFiscal(uuid);

    given()
      .when().delete("/notas-fiscais/" + uuid.toString())
      .then()
        .statusCode(404)
        .body("erro", equalTo("Registro Não Encontrado"))
        .body("mensagem", equalTo("Nota fiscal não encontrada para o id: " + uuid.toString() + "."));
  }

  @Test
  @Order(6)
  void testPesquisarNotasFiscais() {
    RespostaPaginadaDTO<NotaFiscal> respostaMock = new RespostaPaginadaDTO<>(
      List.of(notaFiscal),
      1,
      1,
      10
    );

    when(notaFiscalService.pesquisarNotasFiscais(anyString(), anyInt(), anyInt()))
      .thenReturn(respostaMock);

    given()
      .when().get("/notas-fiscais?termo=1750&pagina=1&tamanhoPagina=10")
      .then().statusCode(200);
  }

  @Test
  @Order(7)
  void testEmitirNotaFiscalPorId() {
    doNothing().when(notaFiscalService).emitirNotaFiscalPorId(notaFiscalId);
    
    given()
      .when().get("/notas-fiscais/emitir/" + notaFiscalId)
      .then()
        .statusCode(204);
  }

  @Test
  @Order(8)
  void testAdicionarItemNotaFiscal() {
    ItemNotaFiscalDTO novoItem = new ItemNotaFiscalDTO(
      UUID.randomUUID().toString(),
      "P001",
      BigDecimal.valueOf(200),
      BigDecimal.valueOf(3)
    );

    doNothing().when(notaFiscalService).adicionarItemNotaFiscal(notaFiscalId, novoItem);
    
    given()
      .contentType(MediaType.APPLICATION_JSON)
      .body(novoItem)
      .when().patch("/notas-fiscais/" + notaFiscalId + "/adicionar")
      .then()
        .statusCode(204);
  }

  @Test
  @Order(9)
  void testRemoverItemNotaFiscal() {
    UUID itemId = itens.get(0).getId();

    doNothing().when(notaFiscalService).removerItemNotaFiscal(notaFiscalId, itemId);
    
    given()
      .when().patch("/notas-fiscais/" + notaFiscalId + "/remover/" + itemId)
      .then()
        .statusCode(204);
  }
}