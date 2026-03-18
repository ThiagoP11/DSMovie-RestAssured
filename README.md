# 🎬 DSMovie - Testes com RestAssured

## 📌 Sobre o projeto

API para gerenciamento de filmes e avaliações.

---

## 📏 Regras de negócio

- Listagem de filmes é pública (sem autenticação)
- Inserir, atualizar e deletar filmes → apenas **ADMIN**
- Avaliações podem ser feitas por **CLIENT** ou **ADMIN**
- Cada avaliação:
  - Nota de 0 a 5
  - Atualiza média (`score`) e quantidade (`count`)

---

## 🛠️ Tecnologias utilizadas

- Java
- Spring Boot
- JUnit 5
- RestAssured
- Maven

---

## 🔐 Autenticação

A API utiliza **JWT Token**.

Perfis:

- **ADMIN** → acesso total
- **CLIENT** → pode avaliar filmes

---

## 🧪 Estrutura de testes

### MovieControllerRA

- findAllShouldReturnOkWhenMovieNoArgumentsGiven
- findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty
- findByIdShouldReturnMovieWhenIdExists
- findByIdShouldReturnNotFoundWhenIdDoesNotExist
- insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle
- insertShouldReturnForbiddenWhenClientLogged
- insertShouldReturnUnauthorizedWhenInvalidToken

### ScoreControllerRA

- saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist
- saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId
- saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero

---

## ▶️ Como executar

```bash
# Clonar o repositório
git clone https://github.com/seu-usuario/dsmovie.git
cd dsmovie

# Rodar o projeto
mvn spring-boot:run

# Executar os testes
mvn test
```
