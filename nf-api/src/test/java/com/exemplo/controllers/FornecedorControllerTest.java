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

import com.exemplo.dtos.CriarFornecedorDTO;
import com.exemplo.dtos.EditarFornecedorDTO;
import com.exemplo.dtos.RespostaPaginadaDTO;
import com.exemplo.entities.EnumSituacaoFornecedor;
import com.exemplo.entities.Fornecedor;
import com.exemplo.exceptions.RegistroDuplicadoException;
import com.exemplo.exceptions.RegistroNaoEncontradoException;
import com.exemplo.services.FornecedorService;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;

import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FornecedorControllerTest {

  @InjectMock
  FornecedorService fornecedorService;

  private UUID fornecedorId;
  private Fornecedor fornecedor;

  @BeforeEach
  void setup() {
    fornecedorId = UUID.randomUUID();
    fornecedor = new Fornecedor();
    fornecedor.setId(fornecedorId);
    fornecedor.setCodigo("F001");
    fornecedor.setRazaoSocial("FORNECEDOR TESTE");
    fornecedor.setEmail("fornecedor@teste.com");
    fornecedor.setTelefone("4530356433");
    fornecedor.setCnpj("12.345.678/0001-95");
    fornecedor.setSituacao(EnumSituacaoFornecedor.ATIVO);
  }

  @Test
  @Order(1)
  void testCriarFornecedorSucesso() {
    when(fornecedorService.criarFornecedor(any(CriarFornecedorDTO.class))).thenReturn(fornecedor);

    given()
      .contentType(MediaType.APPLICATION_JSON)
      .body(fornecedor)
    .when()
      .post("/fornecedores")
    .then()
      .statusCode(201);
  }

  @Test
  @Order(2)
  void testCriarFornecedorCodigoDuplicado() {
    when(fornecedorService.criarFornecedor(any(CriarFornecedorDTO.class)))
      .thenThrow(new RegistroDuplicadoException("Já existe um fornecedor cadastrado com este código: " + fornecedor.getCodigo() + "."));

    given()
      .contentType(MediaType.APPLICATION_JSON)
      .body(fornecedor)
    .when()
      .post("/fornecedores")
    .then()
      .statusCode(400)
      .body("$", hasKey("erro"))
      .body("$", hasKey("mensagem"))
      .body("erro", equalTo("Registro Duplicado"))
      .body("mensagem", equalTo("Já existe um fornecedor cadastrado com este código: " + fornecedor.getCodigo() + "."));
  }

  @Test
  @Order(3)
  void testCriarFornecedorCnpjDuplicado() {
    when(fornecedorService.criarFornecedor(any(CriarFornecedorDTO.class)))
      .thenThrow(new RegistroDuplicadoException("Já existe um fornecedor cadastrado com este CNPJ: " + fornecedor.getCnpj() + "."));

    given()
      .contentType(MediaType.APPLICATION_JSON)
      .body(fornecedor)
    .when()
      .post("/fornecedores")
    .then()
      .statusCode(400)
      .body("$", hasKey("erro"))
      .body("$", hasKey("mensagem"))
      .body("erro", equalTo("Registro Duplicado"))
      .body("mensagem", equalTo("Já existe um fornecedor cadastrado com este CNPJ: " + fornecedor.getCnpj() + "."));
  }

  @Test
  @Order(4)
  void testPesquisarFornecedorPorId() {
    when(fornecedorService.encontrarPorId(fornecedorId)).thenReturn(fornecedor);

    given()
      .when().get("/fornecedores/" + fornecedorId)
      .then().statusCode(200);
  }

  @Test
  @Order(5)
  void testEditarFornecedorSucesso() {
    EditarFornecedorDTO dto = new EditarFornecedorDTO(
      "F002",
      "MARQUINHOS LTDA",
      "marquinhos@ltda.com",
      "(45) 3035-6464",
      "BAIXADO"
    );

    when(fornecedorService.editarFornecedor(eq(fornecedorId), any(EditarFornecedorDTO.class))).thenReturn(fornecedor);

    given()
      .contentType(MediaType.APPLICATION_JSON)
      .body(dto)
      .when()
        .patch("/fornecedores/" + fornecedorId)
      .then()
        .statusCode(200);
  }

  @Test
  @Order(6)
  void testEditarFornecedorSituacaoInvalida() {
    String situacaoInvalida = "INATIVO";
    try {
      EditarFornecedorDTO dto = new EditarFornecedorDTO(
        "F002",
        "MARQUINHOS LTDA",
        "marquinhos@ltda.com",
        "(45) 3035-6464",
        situacaoInvalida
      );

      when(fornecedorService.editarFornecedor(fornecedorId, dto))
        .thenThrow(new IllegalArgumentException("Valor inválido para EnumSituacaoFornecedor"));
      
      given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .patch("/fornecedores/" + fornecedorId)
        .then()
        .statusCode(404);
    } catch (IllegalArgumentException ex) {
      assertEquals("Valor inválido para EnumSituacaoFornecedor: " + situacaoInvalida + ".", ex.getMessage());
    }
  }

  @Test
  @Order(7)
  void testExcluirFornecedor() {
    doNothing().when(fornecedorService).excluirFornecedor(fornecedorId);

    given()
      .when().delete("/fornecedores/" + fornecedorId)
      .then().statusCode(204);
  }

  @Test
  @Order(8)
  void testExcluirFornecedorInexistente() {
    UUID uuid = UUID.randomUUID();

    doThrow(new RegistroNaoEncontradoException("Fornecedor não encontrado para o id: " + uuid.toString() + "."))
      .when(fornecedorService)
      .excluirFornecedor(uuid);

    given()
      .when().delete("/fornecedores/" + uuid.toString())
      .then()
        .statusCode(404)
        .body("erro", equalTo("Registro Não Encontrado"))
        .body("mensagem", equalTo("Fornecedor não encontrado para o id: " + uuid.toString() + "."));
  }

  @Test
  @Order(9)
  void testPesquisarFornecedores() {
    RespostaPaginadaDTO<Fornecedor> respostaMock = new RespostaPaginadaDTO<>(
      List.of(fornecedor),
      1,
      1,
      10
    );

    when(fornecedorService.pesquisarFornecedores(anyString(), anyInt(), anyInt()))
      .thenReturn(respostaMock);

    given()
      .when().get("/fornecedores?termo=F001&pagina=1&tamanhoPagina=10")
      .then().statusCode(200);
  }
}