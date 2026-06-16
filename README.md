# Trabalho Final de Sistemas Operacionais

## Grupo
- Aluno 1: Matheus Wemmer
- Aluno 2: Gabriel Soranzo Jardim 
- Aluno 3: Nicolas Born
- Aluno 4: Pedro Henrique dos Santos
- Aluno 5: Cauã Reinaldo
- Aluno 6: Ruancarlo Brum Lenzzi

## Linguagem utilizada
Java

## Premissas do escalonador
- Quantum:
- Número máximo de processos:
- Tempos de CPU:
- Tempos de I/O:
- Critério de geração dos processos:
- Semente aleatória, se aplicável:

## Como executar o projeto com Docker

### Pré-requisitos

Tenha o Docker instalado na máquina:

```bash
docker --version
```

## 1. Clone o repositório

```bash
git clone https://github.com/matheuswemmer/escalonador-round-robin.git
```

Entre na pasta do projeto:

```bash
cd escalonador-round-robin
```

## 2. Crie a imagem Docker

Na raiz do projeto (onde está o `Dockerfile`), execute:

```bash
docker build -t escalonador .
```

Esse comando irá:

- Baixar a imagem base do Java
- Copiar o código fonte para o container
- Compilar o arquivo `App.java`
- Criar uma imagem chamada `escalonador`

## 3. Execute o programa

```bash
docker run --rm escalonador
```

O programa será executado dentro do container.

## Estrutura do projeto

```
escalonador-round-robin/
│
├── Dockerfile
│
└── Algoritmo/
    └── src/
        └── App.java
```
## O que aparece na saída
Explique brevemente os eventos impressos pelo simulador.

## Limitações conhecidas
Liste pontos que o grupo não conseguiu implementar ou simplificações realizadas.
