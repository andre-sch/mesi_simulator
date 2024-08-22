# MESI Simulator

Caches otimizam o acesso a memória principal por processadores. Armazenando informações
temporariamente, de modo a reduzir o uso do barramento de interconexão.

Em arquiteturas com multiprocessadores, a inconsistência entre caches torna-se
um problema comum. Para lidar com isso, diversas alternativas foram criadas.
Uma delas é o protocolo MESI — simulado pelo projeto.

Segundo o protocolo MESI, cada linha da cache possui um dos estados: Modificado,
Exclusivo, Compartilhado ou Invalido. Com base nesses dados é possível assegurar
a validade e a exclusividade da cópia de uma informação.

## Questões de projeto

### Aplicação

O contexto do projeto abrange um sistema de reservas de cinema. De uma forma simplificada,
um cinema é composto por salas e assentos. Cada sala é uma matriz de assentos,
e cada assento é associado a um estado: disponível ou reservado.

### Processadores

Gerenciam as ações do usuário, comunicando-se com a cache para obter as informações necessárias.  
Contém as operações:

- Listagem de salas
- Reserva de assentos

### Caches

Uma cache deve armazenar uma parte da memória principal. Ela contém um conjunto de linhas,
em que cada linha é composto por uma tag e a cópia de um bloco da memória.
A tag, por sua vez, guarda o mapeamento do bloco e o estado do protocolo.

### Memória principal

Contém um conjunto de blocos, em que cada bloco é uma coleção de dados serializados
como números decimais.

## Implementação

O projeto segue uma arquitetura cliente-servidor. Com um servidor `/api` desenvolvido
com Java e Spring, além de um cliente web `/web`, desenvolvido com Typescript, HTML e CSS.

### Requisitos

- Java Development Kit 17
- Apache Maven
- Node Package Manager

### Configuração do servidor

```sh
cd api/
mvn package
java -jar target/api-1.0.0.jar
```

### Configuração do cliente

```sh
cd web/
npm install
npm run dev
```
