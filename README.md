## 💻 Sobre o projeto

O objetivo deste projeto é realizar a criação de pedidos, envolvendo produtos e clientes. Ele foi desenvolvido em Java 1.8, utilizando o Spring Boot 2.7.17 como framework principal e o banco de dados MySQL 5.7.

Formatos de Requisição e Resposta: A API aceita requisições nos formatos JSON e XML. Certifique-se de formatar suas requisições de acordo com esses padrões.

Observações: O CORS está implementado na funcionalidade, aceitando as ORIGINs http://localhost:3000, http://localhost:8080 e https://eicon.com.br. Para testar requisições via XML ou JSON, adicione no cabeçalho (Headers) a chave "Accept" com o valor "application/json" ou "application/xml".

## 📢 Funcionalidades

- [x] Cadastrar um novo cliente.
- [x] Atualizar um cliente.
- [x] Cadastrar um novo produto.
- [x] Atualizar um produto.
- [x] Cadastrar um novo pedido associado a um cliente e produtos.
- [x] Visualizar um pedido feito buscando por ID.
- [x] Visualizar um pedido feito buscando por data.
- [x] Visualizar todos os pedido realizados.
- [x] Deletar um pedidos por ID.


## ⚠️ Regras implementadas
Ao criar clientes, a funcionalidade não aceita números de controle duplicados. Para atualização, o ID do cliente deve existir previamente.

Para atualizar um produto, é necessário que ele exista anteriormente.

Ao criar um pedido (order), tanto o cliente quanto os produtos devem existir previamente. A funcionalidade não permite mais de 10 itens por pedido. Para buscar por ID ou data, o pedido deve existir antes, e deve conter pelo menos um produto nos itens.

Essas são as regras implementadas para garantir a consistência e integridade dos dados na aplicação. Se precisar de mais informações ou ajustes, estou à disposição!

---

## 🚀 Como executar o projeto

Antes de começar, você vai precisar ter instalado em sua máquina o [DOCKER](https://docs.docker.com/engine/install/) e [DOCKER-COMPOSE](https://docs.docker.com/compose/install/).

#### 🎲 Adquirindo o repositório do projeto

```bash
Clone este repositório
$ git clone https://github.com/CharllesSilva/eicon-challenge.git
```

#### 🎲 Executando a aplicação

```bash
Rode seguinte comando no terminal (root)
$ docker compose up --build
```

#### ✅ Pronto, a api estará rodando no host: (http://localhost:8080/)

---

#### 🎲 Execute os testes

```bash
Rode o seguinte comando no terminal
$ mvn test
```

---
## Open API: http://localhost:8080/swagger-ui/index.html
---

## 🕹 Rotas

### POST - http://localhost:8080/clients
Cria um novo client

JSON
```json
{
    "number_control": 1000,
    "name": "Nome do Cliente",
    "cpf": "123.456.789-01",
    "address": "Endereço do Cliente"
}
```
XML
```xml
<client>
<number_control>1000</number_control>
<name>Nome do Cliente</name>
<cpf>123.456.789-01</cpf>
<address>Endereço do Cliente</address>
</client>
```


curl

```bash
curl --location 'http://localhost:8080/clients' \
--header 'Accept: application/xml' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data '{
    "number_control": 1000,
    "name": "Nome do Cliente",
    "cpf": "123.456.789-01",
    "address": "Endereço do Cliente"
}
'
```



### PATCH - http://localhost:8080/clients/1
Atualiza client

JSON
```json
{
    "number_control": 500,
    "name": "Antonio",
    "cpf": "123.456.789-01",
    "address": "Endereço do Cliente XPTO"
}
```

XML
```xml
<client>
<number_control>500</number_control>
<name>Antonio</name>
<cpf>123.456.789-01</cpf>
<address>Endereço do Cliente XPTO</address>
</client>
```

CURL
```bash
curl --location --request PATCH 'http://localhost:8080/clients/1' \
--header 'Accept: application/xml' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data '{
  "number_control": 500,
  "name": "Antonio",
  "cpf": "000.456.789-00",
  "address": "Endereço do Cliente XPTO"
}
'
```

### POST - http://localhost:8080/products
Cria um novo produto

JSON
```json
{
    "name": "Nome do Produto",
    "price": 50.00
}
```

XML
```xml
<product>
    <name>Nome do Produto</name>
    <price>50.00</price>
</product>
```

CURL

```bash
curl --location 'http://localhost:8080/products' \
--header 'Accept: application/xml' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Nome do Produto",
    "price": 50.00
}'
```


### PATCH - http://localhost:8080/products/1
Atualiza produto

JSON
```json
{
  "name": "Coca cola 2L",
  "price": 25.00
}
```

XML
```xml
<product>
  <name>Coca cola 2L</name>
  <price>25.00</price>
</product>
```

CURL

```bash
curl --location --request PATCH 'http://localhost:8080/products/1' \
--header 'Accept: application/xml' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data '{
  "name": "Coca cola 2L",
  "price": 25.00
}'
```

#### POST - http://localhost:8080/orders
Cria um novo pedido

JSON
```json
{
  "id_client": 1,
  "items": [
    {
      "product": {
        "id": 1
      },
      "quantity": 2
    },
    {
      "product": {
        "id": 1
      },
      "quantity": 3
    }
  ],
  "registrations_date": "2023-11-17T23:13:55.752Z"
}

```

XML
```xml
<order>
  <id_client>1</id_client>
  <items>
    <item>
      <product>
        <id>1</id>
      </product>
      <quantity>2</quantity>
    </item>
    <item>
      <product>
        <id>1</id>
      </product>
      <quantity>3</quantity>
    </item>
  </items>
  <registrations_date>2023-11-17T23:13:55.752Z</registrations_date>
</order>

```

CURL

```bash
curl --location 'http://localhost:8080/orders/' \
--header 'Accept: application/xml' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data '{
  "id_client": 1,
  "items": [
    {
      "product": {
        "id": 1
      },
      "quantity": 2
    },
    {
      "product": {
        "id": 1
      },
      "quantity": 3
    }
  ],
  "registrations_date": "2023-11-17T23:13:55.752Z"
}
'
```

#### GET - http://localhost:8080/orders/{ID}
Busca um pedido pelo ID passando o mesmo por parametro na URL
CURL

```bash
curl --location 'http://localhost:8080/orders/1'
```

#### GET - http://localhost:8080/orders
Busca todos os pedidos feitos

```bash
curl --location 'http://localhost:8080/orders'
```

#### GET - http://localhost:8080/orders?date=2023-01-01T00:00:00Z
Busca pedidos feitos nessa data (Data informada por parametro na URL)

```bash
curl --location 'http://localhost:8080/orders?date=2023-11-17T23%3A13%3A56Z'
```

#### DELETE - http://localhost:8080/orders/{ID}
Deleção feita atraves do ID informado atraves da URL

```bash
curl --location --request DELETE 'http://localhost:8080/orders/1'
```
