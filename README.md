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
- Algoritmo:
- Quantum:
- Número máximo de processos:
- Semente aleatória:
- Tempos de CPU:
- Tempos de I/O:
- Critério de geração dos processos:
- Regras de feedback:


## Como executar o projeto com Docker

### Pré-requisitos

Tenha o Docker instalado na máquina:

```bash
docker --version
```

## 1. Clone o repositório

```bash
git clone [https://github.com/matheuswemmer/escalonador-round-robin.git](https://github.com/matheuswemmer/escalonador-round-robin.git)
cd escalonador-round-robin
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
- P[X] criado -> fila ALTA: O processo X alcançou seu tempo de chegada e foi admitido na fila de prontos de maior prioridade.

- CPU executa P[X] [fila ALTA/BAIXA]...: O escalonador concedeu a posse do processador ao processo X, indicando de qual fila ele foi retirado.

- P[X] solicitou I/O [DISCO/FITA/IMPRESSORA] por Y unidades: O processo interceptou sua execução para aguardar uma operação de Entrada/Saída no dispositivo indicado pelo período Y.

- P[X] retornou do [TIPO] -> fila [ALTA/BAIXA]: A operação de E/S foi concluída e o processo foi reescalonado na fila correspondente pelas regras de Feedback.

- P[X] sofreu preempção -> fila BAIXA: O processo estourou o limite do quantum consecutivo de CPU (2 ticks) e teve seu contexto interrompido, perdendo prioridade.

- P[X] finalizou: O processo consumiu todo o seu tempo de CPU necessário e foi encerrado com sucesso.

## Limitações conhecidas

- Simulação Lógica Discreta Síncrona: O tempo é simulado de forma linear em um laço único sequencial (tempo++). Desse modo, não há paralelismo ou concorrência real baseada em Threads do sistema operacional anfitrião para gerenciar os dispositivos de E/S e CPU.

- Apenas uma Chamada de I/O por Processo: Pela modelagem atual, cada processo gerado aleatoriamente possui suporte para disparar no máximo uma única requisição de I/O durante todo o seu ciclo de vida.

- Ausência de Mecanismo de Aging (Envelhecimento): O escalonador prioriza rigorosamente a Fila Alta. Caso o sistema sofresse uma entrada infinita e ininterrupta de processos prioritários ou rápidos, os processos relegados à Fila Baixa sofreriam starvation crônico (fome de CPU), pois não há uma regra para subir a prioridade por tempo de espera.
