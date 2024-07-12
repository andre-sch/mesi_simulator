# Reservas de cinema

## Domínio da aplicação

Objetos: Filmes, Sessões, Salas e Assentos.
  
### Filmes

- Cada filme possui: id, titulo, descrição, duração e um conjunto de sessões disponíveis.
  Uma sessão é formada por: uma sala, um horário e um dia da semana.

### Salas

- Cada um possui uma identificação numérica { 1, 2, 3, ... } e um conjunto de
  assentos, simplificadamente distribuídos em uma matriz com m linhas (qnt_filas)
  e n colunas (qnt_assentos_por_fila).

### Assentos

- Uma posição na matriz de uma sala.
- Possui um conjunto de estados quanto a reserva { Disponível, Selecionado, Reservado }.

```mermaid
classDiagram
  namespace Cinema {
    class Filme {
      - int id
      - String título
      - String descrição
      - int duração
      - List~Sessao~ sessões
    }

    class Sessao {
      - Sala sala
      - int horário
      - int dia_da_semana
    }
    
    class Sala {
      - int id
      - int qnt_filas
      - int qnt_assentos_por_fila
      - List~Assento~ assentos
    }
    
    class Assento {
      - Estado_Reserva estado
    }
    
    class Estado_Reserva {
      <<enumeration>>
      Disponível
      Selecionado
      Reservado
    }
  }
  
  Sala o-- Assento
  Filme o-- Sessao
  Sessao *-- Sala
```

## Abstrações de baixo nível

Objetos: Usuários, Processadores, Caches e RAM.

### Usuários

Cada usuário do sistema é associado a um processador/cache.  
O usuário pode realizar as seguintes ações:

- Listar filmes e sessões*
- Listar assentos de uma sala, incluindo o estado quanto a reserva
- Selecionar um assento**
- Cancelar uma seleção
- Efetivar uma reserva

*Os dados de uma sala são filtrados: exibe-se apenas o id.  
**A seleção de um assento torna a sua reserva exclusiva a um usuário por tempo limitado.
Após um tempo de expiração, o assento volta a ser disponível.

### Processadores

As ações do usuário devem ser gerenciadas por processadores, que se comunicam com
a memória/cache para conseguir as informações necessárias.

### Caches

Uma cache deve armazenar uma porção da memória principal (RAM).  
Questões a serem consideradas:

- Função de mapeamento: aleatória — qualquer bloco da memória pode ser salvo em qualquer linha
- Algoritmo de substituição: FIFO/Queue — o primeiro bloco a ser mapeado é removido
- Gerenciamento de Tags: guardam o mapeamento linha/bloco + status de uso (protocolo MESI)
- Canal de comunicação entre caches

### RAM

Questões de projeto:

- Quantidade de endereços
- Tamanho e tipagem da palavra
- Serialização e deserialização de objetos do domínio

```mermaid
---
title: Relacionamentos
---
stateDiagram
  direction LR

  Usuário --> Processador : 1/1
  Processador --> Cache : 1/1
  Cache --> RAM : */1
```
