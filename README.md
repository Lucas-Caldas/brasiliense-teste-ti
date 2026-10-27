# Brasiliense - Teste prático F SIS-03

Projeto do candidato **Lucas Caldas**.

Stack: **Java 17**, **Spring Boot**, **Hibernate/JPA**, **SQL Server** (com **H2** embutido para rodar local sem instalar banco) e **JavaScript** (vanilla) na tela.

## O que o projeto faz

1. CRUD com detalhe das entidades: **Edifício / Apartamentos / Pagamentos de condomínio**
2. Consulta de ranking (3 maiores somatórios de condomínio no período) com `sp_ranking_condominio`
3. Stored procedure:
   - Original com erros: `sql/sp_ranking_condominio_original.sql`
   - Versão corrigida: `sql/sp_ranking_condominio_corrigida.sql`
4. Documentação das APIs no **Swagger**
5. Tela web em **HTML + CSS + JavaScript** (`src/main/resources/static/index.html`)

### Front-end (JavaScript)

A tela não usa React, Angular nem outro framework. É **JavaScript puro** no próprio `index.html`, servido pelo Spring Boot em http://localhost:8080.

O que o JS faz:
- chama a API com `fetch` (`/api/cidades`, `/api/edificios`, `/api/ranking-condominio`)
- monta dinamicamente apartamentos e pagamentos no formulário de cadastro
- lista edifícios, edita, exclui e consulta o ranking
- links no topo para **Swagger** e **H2 Console**

### Correções feitas na `sp_ranking_condominio`

| # | Tipo | Problema na original | Correção |
|---|------|----------------------|----------|
| 1 | Bug | No bloco da temp de pagamentos, faz `DROP` de `#tbl_apartamentos` | Dropa `#tbl_pagamentos_condominios` |
| 2 | Regra | `AVG` | `SUM` |
| 3 | Lógica | `TOP 3` sem `ORDER BY` | `ORDER BY VALOR_PAGAMENTO DESC` |
| 4 | Deploy | `ALTER` | `CREATE OR ALTER` |

---

## Passo a passo (Windows) — rodar com H2

Você **não precisa instalar o H2** à mão. Ele já vem no projeto (dependência do Maven) e sobe junto com a aplicação, em memória.

### 1) Instalar o Java 17

1. Baixe o JDK 17 (exemplo Azul Zulu ou Eclipse Temurin):
   - https://www.azul.com/downloads/?version=java-17-lts&os=windows&package=jdk  
   - ou https://adoptium.net/temurin/releases/?version=17
2. Instale normalmente (Next, Next, Finish).
3. Abra o **Prompt de Comando** ou **PowerShell** e digite:

```bat
java -version
```

Deve aparecer algo com `17` (ex.: `openjdk version "17.0.x"`).  
Se der erro de comando não encontrado, reinicie o terminal (ou o PC) e tente de novo.

### 2) Instalar o Maven

1. Baixe o Maven: https://maven.apache.org/download.cgi (arquivo `apache-maven-x.x.x-bin.zip`)
2. Extraia para uma pasta simples, por exemplo: `C:\apache-maven-3.9.9`
3. Adicione ao PATH do Windows:
   - Painel de Controle → Sistema → Configurações avançadas do sistema → Variáveis de Ambiente
   - Em **Path**, adicione: `C:\apache-maven-3.9.9\bin`
4. Feche e abra o terminal de novo e digite:

```bat
mvn -version
```

Deve mostrar a versão do Maven e o Java.

> Dica: o `JAVA_HOME` deve apontar para a pasta do JDK 17, por exemplo:  
> `C:\Users\SEU_USUARIO\.jdks\azul-17.0.20`  
> ou `C:\Program Files\Eclipse Adoptium\jdk-17...`

### 3) Abrir a pasta do projeto

No terminal:

```bat
cd "C:\Users\user\Projetos\brasiliense-teste-ti"
```

(Ajuste o caminho se a pasta estiver em outro lugar.)

### 4) Subir a aplicação (H2 já incluso)

```bat
mvn spring-boot:run
```

Na primeira vez o Maven baixa as bibliotecas (incluindo o **H2**). Pode demorar alguns minutos.

Quando aparecer algo como `Started BrasilienseTesteTiApplication`, está pronto.

### 5) Abrir no navegador

| O quê | Endereço |
|------|----------|
| Tela do sistema | http://localhost:8080 |
| Swagger (detalhe das APIs) | http://localhost:8080/swagger-ui.html |
| Console do H2 | http://localhost:8080/h2-console |

#### Como entrar no console H2

1. Abra http://localhost:8080/h2-console
2. Preencha:
   - **JDBC URL:** `jdbc:h2:mem:brasiliense`
   - **User Name:** `sa`
   - **Password:** (deixe em branco)
3. Clique em **Connect**

Os dados de exemplo (edifícios, apartamentos, pagamentos) já são carregados automaticamente.

### 6) Parar o sistema

No terminal onde o projeto está rodando, pressione `Ctrl + C`.

---

## Testes unitários

```bat
mvn test
```

Cobertura principal:
- `EdificioServiceTest` — CRUD com detalhe das entidades (criar, atualizar, excluir, validações)
- `RankingServiceTest` — período inválido e mapeamento do top 3
- `EdificioControllerTest` / `RankingControllerTest` — endpoints HTTP com MockMvc

---

## Endpoints úteis

- Lista de edifícios: http://localhost:8080/api/edificios
- Ranking exemplo:  
  http://localhost:8080/api/ranking-condominio?dataInicio=2011-01-01&dataFim=2011-12-31
- Cidades: http://localhost:8080/api/cidades

---

## Rodar com SQL Server

Só se quiser usar SQL Server de verdade em vez do H2:

1. Crie o database `BrasilienseTeste`
2. Ajuste usuário/senha em `src/main/resources/application.yml` (profile `sqlserver`)
3. Rode o script `sql/sp_ranking_condominio_corrigida.sql` no SQL Server
4. Suba com:

```bat
mvn spring-boot:run -Dspring-boot.run.profiles=sqlserver
```

O endpoint `/api/ranking-condominio` usa `sp_ranking_condominio`
(`app.ranking.procedure-call` no `application.yml`).

---

## Entrega

- Código-fonte deste projeto
- `sql/sp_ranking_condominio_original.sql` (original com erros)
- `sql/sp_ranking_condominio_corrigida.sql` (versão corrigida)
- PDF do F SIS-02 respondido (pasta `docs`)
