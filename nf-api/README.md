# nf-api

## Requisitos

- Java 17

## Executar a aplicação em modo de desenvolvimento

Você pode executar a aplicação em modo de desenvolvimento que habilita o _live coding_ assim:

```shell script
./mvnw quarkus:dev
```

> **OBS1.:** Recomendo executar em mode de desenvolvimento para não precisar instalar o banco de dados.

> **OBS2.:** O acesso a API é nesse endereço: http://localhost:7171/api.

> **OBS3.:** O acesso ao Swagger da aplicação fica em: http://localhost:7171/swagger.

## Empacotando e executando a aplicação

A aplicação pode ser empocatada assim:

```shell script
./mvnw clean package
```

E produz um arquivo `quarkus-run.jar` dentro da pasta `target/quarkus-app/`.

A aplicação pode ser executada agora usando `java -jar target/quarkus-app/quarkus-run.jar` (`JAR` para execução em desenvolvimento).

Ou, assim `java -jar target/nf-api-1.0.0-SNAPSHOT-runner.jar` (`JAR`  tradicional).

## Opcional: Criar um executável nativo (recomendado para produção)

Voce pode criar um executável nativo usando:

```shell script
./mvnw package -Dnative
```

Ou, se não tiver o GraalVM instalado, você pode executar o executável nativo construído em container::

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

Você pode entao executar seu nativo executável em: `./target/nf-api-1.0.0-SNAPSHOT-runner`