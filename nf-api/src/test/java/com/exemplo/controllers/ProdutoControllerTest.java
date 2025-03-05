package com.exemplo.controllers;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.exemplo.dtos.CriarProdutoDTO;
import com.exemplo.dtos.EditarProdutoDTO;
import com.exemplo.dtos.RespostaPaginadaDTO;
import com.exemplo.entities.EnumSituacaoProduto;
import com.exemplo.entities.Produto;
import com.exemplo.exceptions.RegistroDuplicadoException;
import com.exemplo.exceptions.RegistroNaoEncontradoException;
import com.exemplo.services.ProdutoService;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;

import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProdutoControllerTest {

  @InjectMock
  ProdutoService produtoService;

  private UUID produtoId;
  private Produto produto;

  @BeforeEach
  void setup() {
    produtoId = UUID.randomUUID();
    produto = new Produto();
    produto.setId(produtoId);
    produto.setCodigo("P001");
    produto.setDescricao("PRODUTO TESTE");
    produto.setSituacao(EnumSituacaoProduto.ATIVO);
  }

  @Test
  @Order(1)
  void testCriarProdutoSucesso() {
    when(produtoService.criarProduto(any(CriarProdutoDTO.class))).thenReturn(produto);

    given()
      .contentType(MediaType.APPLICATION_JSON)
      .body(produto)
    .when()
      .post("/produtos")
    .then()
      .statusCode(201);
  }

  @Test
  @Order(2)
  void testCriarProdutoCodigoDuplicado() {
    when(produtoService.criarProduto(any(CriarProdutoDTO.class)))
      .thenThrow(new RegistroDuplicadoException("Já existe um produto cadastrado com este código: " + produto.getCodigo() + "."));

    given()
      .contentType(MediaType.APPLICATION_JSON)
      .body(produto)
    .when()
      .post("/produtos")
    .then()
      .statusCode(400)
      .body("$", hasKey("erro"))
      .body("$", hasKey("mensagem"))
      .body("erro", equalTo("Registro Duplicado"))
      .body("mensagem", equalTo("Já existe um produto cadastrado com este código: " + produto.getCodigo() + "."));
  }

  @Test
  @Order(3)
  void testPesquisarProdutoPorId() {
    when(produtoService.encontrarPorId(produtoId)).thenReturn(produto);

    given()
      .when().get("/produtos/" + produtoId)
      .then().statusCode(200);
  }

  @Test
  @Order(4)
  void testEditarProdutoSucesso() {
    EditarProdutoDTO dto = new EditarProdutoDTO(
      "P001",
      "DESCRIÇÃO EDITADA",
      "INATIVO"
    );

    when(produtoService.editarProduto(eq(produtoId), any(EditarProdutoDTO.class))).thenReturn(produto);

    given()
      .contentType(MediaType.APPLICATION_JSON)
      .body(dto)
      .when()
        .patch("/produtos/" + produtoId)
      .then()
        .statusCode(200);
  }

  @Test
  @Order(5)
  void testEditarProdutoSituacaoInvalida() {
    String situacaoInvalida = "XXX";
    try {
      EditarProdutoDTO dto = new EditarProdutoDTO(
        "P001",
        "DESCRIÇÃO EDITADA",
        situacaoInvalida
      );

      when(produtoService.editarProduto(produtoId, dto))
        .thenThrow(new IllegalArgumentException("Valor inválido para EnumSituacaoProduto"));
      
      given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .patch("/produtos/" + produtoId)
        .then()
        .statusCode(404);
    } catch (IllegalArgumentException ex) {
      assertEquals("Valor inválido para EnumSituacaoProduto: " + situacaoInvalida + ".", ex.getMessage());
    }
  }

  @Test
  @Order(6)
  void testExcluirProduto() {
    doNothing().when(produtoService).excluirProduto(produtoId);
    
    given()
      .when().delete("/produtos/" + produtoId)
      .then().statusCode(204);
  }

  @Test
  @Order(7)
  void testExcluirProdutoInexistente() {
    UUID uuid = UUID.randomUUID();

    doThrow(new RegistroNaoEncontradoException("Produto não encontrado para o id: " + uuid.toString() + "."))
      .when(produtoService)
      .excluirProduto(uuid);

    given()
      .when().delete("/produtos/" + uuid.toString())
      .then()
        .statusCode(404)
        .body("erro", equalTo("Registro Não Encontrado"))
        .body("mensagem", equalTo("Produto não encontrado para o id: " + uuid.toString() + "."));
  }

  @Test
  @Order(9)
  void testPesquisarProdutos() {
    RespostaPaginadaDTO<Produto> respostaMock = new RespostaPaginadaDTO<>(
      List.of(produto),
      1,
      1,
      10
    );

    when(produtoService.pesquisarProdutos(anyString(), anyInt(), anyInt()))
      .thenReturn(respostaMock);

    given()
      .when().get("/produtos?termo=P001&pagina=1&tamanhoPagina=10")
      .then().statusCode(200);
  }
}